#!/bin/python3

import mysql.connector
import requests
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


def call_api(call_str):
    d = {}
    res = requests.get(call_str)
    data = res.json()
    return data

def insert_zones(cursor):
    query='INSERT INTO `zones`(id, name) VALUES (%i, \'%s\')'
    for i in range(1, 9):
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


def insert_gps(cursor):
    query = 'UPDATE stations SET latitude=%f, longitude=%f WHERE naptan=\'%s\''
    with open('../data_result.json', 'r') as data:
        data_dict = json.loads(''.join(data.readlines()))
        station_dict = defaultdict()
        for line in data_dict:
            for bound in data_dict[line]['outbound'].items():
                for sequence in bound[1]:
                    stopPoints = sequence['stopPoint']
                    for i in range(0, len(stopPoints)-1):
                        station_dict[stopPoints[i]['stationId']] = {
                            'latitude': stopPoints[i]['lat'],
                            'longitude': stopPoints[i]['lon']
                        }
            for stopPoint in station_dict:
                naptan = stopPoint
                stop = station_dict[stopPoint]
                final_query = query % (stop['latitude'], stop['longitude'], naptan)
                print(final_query)
                cursor.execute(final_query)

def insert_missing_gps(cursor):
    select = 'SELECT naptan FROM stations WHERE longitude=0 OR latitude=0'
    update = 'UPDATE stations SET latitude=%f, longitude=%f WHERE naptan=\'%s\''
    cursor.execute(select)
    sql_result = cursor.fetchall()
    api_query = 'https://api.tfl.gov.uk/StopPoint/%s'

    for row in sql_result:
        naptan = row[0].decode('UTF-8')
        res = call_api(api_query % (naptan))
        latitude = res['lat']
        longitude = res['lon']
        update_query = update % (latitude, longitude, naptan)
        print(update_query)
        cursor.execute(update_query)

def insert_stations_zones(cursor):
    query = 'INSERT IGNORE INTO zones_stations(station, zone) VALUES(\'%s\', %d)'
    with open('../data_result.json', 'r') as data:
        data_dict = json.loads(''.join(data.readlines()))
        station_dict = defaultdict()
        for line in data_dict:
            for bound in data_dict[line]['outbound'].items():
                for sequence in bound[1]:
                    stopPoints = sequence['stopPoint']
                    for i in range(0, len(stopPoints)-1):
                        station_dict[stopPoints[i]['stationId']] = {
                            'zone': stopPoints[i]['zone']
                        }
            for bound in data_dict[line]['inbound'].items():
                for sequence in bound[1]:
                    stopPoints = sequence['stopPoint']
                    for i in range(0, len(stopPoints)-1):
                        station_dict[stopPoints[i]['stationId']] = {
                            'zone': stopPoints[i]['zone']
                        }
            for stopPoint in station_dict:
                naptan = stopPoint
                zones = station_dict[stopPoint]['zone'].split('+')
                for zone in zones:
                    try:
                        zone = int(zone)
                    except ValueError:
                        sub_zones = zone.split('/')
                        print(sub_zones)
                        for sub_zone in sub_zones:
                            print(naptan)
                            final_query = query % (naptan, int(sub_zone))   
                            print(final_query)
                            cursor.execute(final_query) 
                        continue
                    final_query = query % (naptan, zone)
                    print(final_query)
                    cursor.execute(final_query)

def insert_missing_zones(cursor):
    api_query = 'https://api.tfl.gov.uk/StopPoint/Search?query=%s&modes=tube'
    select_sql = 'SELECT a.name, a.naptan FROM stations as a LEFT JOIN zones_stations AS b ON b.station = a.naptan WHERE b.station IS NULL'
    insert = 'INSERT INTO zones_stations(zone, station) VALUES(%d, \'%s\')'
    cursor.execute(select_sql)
    sql_result = cursor.fetchall()
    for row in sql_result:
        name = row[0].decode('UTF-8')
        naptan = row[1].decode('UTF-8')
        name = name[:-20]
        name.replace(' ', '%20')
        res = call_api(api_query % (name))
        zone = int(res['matches'][0]['zone'])
        final_query = insert % (zone, naptan)
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
            #insert_durations(cursor)
            #insert_gps(cursor)
            #insert_missing_gps(cursor)
            #insert_stations_zones(cursor)
            insert_missing_zones(cursor)
            connection.commit()
            connection.close()
    except Error as e:
        print(e)
        connection.rollback()

if __name__ == "__main__":
    #insert_durations("no_cursor")
    send_to_sql()
