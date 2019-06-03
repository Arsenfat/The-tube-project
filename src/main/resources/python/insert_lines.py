#!/bin/python3

import mysql.connector
import json
from mysql.connector.connection import MySQLCursorPrepared
from mysql.connector import Error
from collections import defaultdict
import sys
import re

trans = str.maketrans({"-":  r"\-",
                                          "]":  r"\]",
                                          "\\": r"\\",
                                          "^":  r"\^",
                                          "$":  r"\$",
                                          "*":  r"\*",
                                          ".":  r"\.",
                                          "'":  r"\'"})

def insert_zones(cursor):
    query='INSERT INTO `zones`(id, name) VALUES (%i, \'%s\')'
    for i in range(1, 7):
        cursor.execute(query % (i, 'ZONE %i' % i))

def insert_lines(cursor):
    lines=('bakerloo', 'central', 'circle', 'district', 'hammersmith-city', 'jubilee',
           'metropolitan', 'northern', 'piccadilly', 'victoria', 'waterloo-city')
    query='INSERT INTO `lines` (id, name) VALUES (%i, \'%s\')'
    for i, line in enumerate(lines):
        cursor.execute(query % (i + 1, line))

def insert_stations(cursor):
    query='INSERT IGNORE INTO `stations`(naptan, name) VALUES(\'%s\', \'%s\')'
    with open('../tube_data.json', 'r') as json_data:
        data=json.loads(''.join(json_data.readlines()))
        for line in data:
            print(line)
            for station in data[line].items():
                print(station[0].translate(trans), station[1]["naptan"])
                cursor.execute(query %
                               (station[1]["naptan"], station[0].translate(trans)))


def insert_durations(cursor):
    query = 'INSERT IGNORE INTO `station_durations` VALUES(\'%s\', \'%s\', %f)'
    with open('../duration_result.json', 'r') as json_data:
        data = json.loads(''.join(json_data.readlines()))
        with open('../data_result.json', 'r') as result_data:
            result_data_dict = json.loads(''.join(result_data.readlines()))
            station_dict = defaultdict()
            station_dict["1000097"] = "940GZZLUHSC"
            for line in result_data_dict:
                for bound in result_data_dict[line]['outbound'].items():
                    for sequence in bound[1]:
                        stopPoints = sequence['stopPoint']
                        for i in range(0, len(stopPoints)-1):
                            station_dict[stopPoints[i]['icsId']] = stopPoints[i]['stationId']
                for bound in result_data_dict[line]['inbound'].items():
                    for sequence in bound[1]:
                        stopPoints = sequence['stopPoint']
                        for i in range(0, len(stopPoints)-1):
                            station_dict[stopPoints[i]['icsId']] = stopPoints[i]['stationId']
            for line in data:
                for stopPoint in data[line]:
                    departing = station_dict[stopPoint['departingPoint']]
                    arriving = station_dict[stopPoint['arrivingPoint']]
                    duration = float(stopPoint['duration'])
                    final_query = query % (departing, arriving, duration)
                    print(final_query)
                    cursor.execute(final_query)



def send_to_sql():
    try:
        connection=mysql.connector.connect(
            host = 'remotemysql.com', database = 'scrape', user = 'scrape', password = 'scrape', use_pure = True)
        if connection.is_connected():
            db_info=connection.get_server_info()
            print(db_info)
            cursor=MySQLCursorPrepared(connection = connection)
            connection.start_transaction()
            #insert_zones(cursor)
            #insert_lines(cursor)
            #insert_stations(cursor)
            insert_durations(cursor)
            connection.commit()
            connection.close()
    except Error as e:
        print(e)
        connection.rollback()

if __name__ == "__main__":
    #insert_durations("no_cursor")
    send_to_sql()