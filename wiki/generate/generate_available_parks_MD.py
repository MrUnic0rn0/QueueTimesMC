# -*- coding: utf-8 -*-

import json
import os

import pycountry
import requests

def generate_park(park_name,park_id):

    r = requests.get("https://queue-times.com/parks/"+str(park_id)+"/queue_times.json")
    data = r.json()
    
    f = open("../parks/"+park_name+".md", "w",encoding="utf-8")
    f.write('<a href="../parks_available.md">&laquo; Back</a>')
    f.write("\n# "+park_name + " : " + str(park_id))

    for land in data["lands"]:
        f.write("\n - **" +str(land["name"]) +"** " )
        for ride in land["rides"]:
            f.write("\n   - " +str(ride["name"]) +" `%qt_"+str(park_id)+"_"+str(ride["id"])+"%`" )
        f.write("\n---")

    if len(data["rides"])>0:
        f.write("\n - **Others** " )
        for ride in data["rides"]:
            f.write("\n   - " +str(ride["name"]) +" `%qt_"+str(park_id)+"_"+str(ride["id"])+"%`" )


if not os.path.isdir('../parks'):
    os.makedirs('../parks')

f = open("../parks_available.md", "w",encoding="utf-8")
f.write('<a href="../README.md">&laquo; Back</a>')
f.write("# :roller_coaster: Parks Available")

r = requests.get("https://queue-times.com/parks.json")
data = r.json()

working_data = {}
for company in data:
    for park in company["parks"]:
        country = park["country"].strip()
        if country not in working_data:
            search = country
            if search == "England":
                search = "United Kingdom"
            code = pycountry.countries.lookup(search)
            working_data[country] = {}
            working_data[country]["flag"] = code.flag
            working_data[country]["name"] = code.name
            working_data[country]["parks"]  = []
                
        working_data[country]["parks"].append(park)
    
sort_key = sorted(working_data)
for country in sort_key:
    cur = working_data[country];
    f.write("\n<details>")
    f.write("\n<summary>"+cur["flag"]+" "+cur["name"]+"</summary>")
    parks = sorted(cur["parks"], key=lambda x: x["name"])
    for park in parks:
        f.write("\n<li><strong>" +str(park["id"]) +"</strong> : <a href=\"parks/"+ park["name"] +".md\">"+  park["name"]+"</a></li>" )

        generate_park(park["name"],park["id"])
    f.write("\n</details>")    
        
f.close()
