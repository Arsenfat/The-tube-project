#!/usr/bin/python
import requests
import json


lines = ('bakerloo', 'central', 'circle', 'district', 'hammersmith-city', 'jubilee', 'metropolitan', 'northern', 'piccadilly', 'victoria', 'waterloo-city') 

def call_api(call_str):
    d = {}
    res = requests.get(call_str)     
    data = res.json()
    for stop in data:
        name = stop['commonName']
        d[name] = {}
        d[name]['connections'] = []
        for line in stop['lines']:
            if line['id'] in lines:
                d[name]['connections'].append(line)
        d[name]['naptan'] = stop['stationNaptan']
    return d

if __name__ == '__main__':
    app_id = '10cb7bd3'
    api_key = '7d5fa71302afb46d76a29bcaee18abc2'


    api_call = 'https://api.tfl.gov.uk/line/{0}/stoppoints'

    lines_dict = {}

    for line in lines:
        lines_dict[line] = call_api(api_call.format(line))
    with open('result.json', 'w') as f:
        f.write(json.dumps(lines_dict))

