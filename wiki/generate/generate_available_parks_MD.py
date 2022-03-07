# -*- coding: utf-8 -*-

import json
import pycountry

f = open("../available_parks.md", "w",encoding="utf-8")
f.write("# :roller_coaster: Available parks")



with open('parks.json', encoding='utf-8') as json_file:
    data = json.load(json_file)
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
           f.write("\n<li><strong>" +str(park["id"]) +"</strong> : " +  park["name"]+"</li>" )
        f.write("\n</details>")
        
        
f.close()
