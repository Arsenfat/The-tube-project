#!/usr/bin/python3
import requests
import json


lines = ('bakerloo', 'central', 'circle', 'district', 'hammersmith-city', 'jubilee', 'metropolitan', 'northern', 'piccadilly', 'victoria', 'waterloo-city') 

def call_api(call_str):
    d = {}
    res = requests.get(call_str)     
    data = res.json()
    d['sequences'] = data['stopPointSequences']
    return d

if __name__ == '__main__':
    app_id = '10cb7bd3'
    api_key = '7d5fa71302afb46d76a29bcaee18abc2'


    api_call = 'https://api.tfl.gov.uk/line/{0}/route/sequence/{1}'

    lines_dict = {}

    for line in lines:
        lines_dict[line] = {}
        lines_dict[line]['outbound'] = call_api(api_call.format(line, 'outbound'))
        lines_dict[line]['inbound'] = call_api(api_call.format(line, 'inbound'))

    with open('data_result.json', 'w') as f:
        f.write(json.dumps(lines_dict))

