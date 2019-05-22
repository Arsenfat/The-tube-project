#!/bin/python3

import mysql.connector
import json
from mysql.connector.connection import MySQLCursorPrepared
from mysql.connector import Error

def insert_zones(cursor):
    query = 'INSERT INTO `zones`(id, name) VALUES (%i, \'%s\')'
    for i in range(1, 7):
        cursor.execute(query % (i, 'ZONE %i' % i))

def insert_lines(cursor):
    lines = ('bakerloo', 'central', 'circle', 'district', 'hammersmith-city', 'jubilee', 'metropolitan', 'northern', 'piccadilly', 'victoria', 'waterloo-city') 
    query = 'INSERT INTO `lines` (id, name) VALUES (%i, \'%s\')'
    for i, line in enumerate(lines):
        cursor.execute(query % (i, line))

def insert_stations(cursor):
    with open('tube_data', 'r') as json_data:
        data = json.loads(json_data)
        for line in data:
            print(line)
            for bound in data[line]['inbound'].items():
                for sequence in bound[1]:
                    stopPoints = sequence['stopPoint']

try:
    connection = mysql.connector.connect(host='remotemysql.com', database='5qFaDYUMfJ', user='5qFaDYUMfJ', password='MqeVaLKxuj', use_pure=True)
    if connection.is_connected():
        db_info = connection.get_server_info()
        print(db_info)
        cursor = MySQLCursorPrepared(connection=connection)
        connection.start_transaction()
        insert_lines(cursor)
        connection.commit()
        connection.close()
except Error as e:
    print(e)
    connection.rollback()
