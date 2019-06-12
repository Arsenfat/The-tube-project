#!/usr/bin/python3
import json
import requests

lines = {}
total_calls = 0
with open('/home/wellan/IdeaProjects/The-tube-project/src/main/resources/data_result.json') as json_data:

    data = json.load(json_data)
    for line in data:
        print(line)
        for bound in data[line]['inbound'].items():
            for sequence in bound[1]:
                stopPoints = sequence['stopPoint']
                for i in range(0, len(stopPoints)-1):
                    if not line in lines:
                        lines[line] = []
                    stop_point_dict = {}
                    departing = stopPoints[i]
                    arriving = stopPoints[i+1]
                    stop_point_dict['departingPoint'] = departing['icsId']
                    stop_point_dict['arrivingPoint'] = arriving['icsId']
                    res = requests.get('https://api.tfl.gov.uk/journey/journeyresults/{0}/to/{1}'.format(departing['icsId'], arriving['icsId']))
                    total_calls += 1
                    print('TOTAL CALL: {0}'.format(total_calls))
                    res = res.json()
                    tot = 0
                    if 'journeys' not in res:
                        print('NO JOURNEYS')
                        continue
                    len_journey = len(res['journeys'])
                    for journey in res['journeys']:
                        if int(journey['duration']) == 0 or int(journey['duration']) > 15:
                            len_journey -= 1
                            continue
                        tot += int(journey['duration'])
                    if len_journey == 0:
                        continue
                    stop_point_dict['duration'] = tot/len_journey
                    lines[line].append(stop_point_dict)
        with open(line+'.json', 'w') as file:
            if line in lines:
                file.write(json.dumps(lines[line]))

with open('duration_result.json', 'w') as file:
    file.write(json.dumps(lines))

