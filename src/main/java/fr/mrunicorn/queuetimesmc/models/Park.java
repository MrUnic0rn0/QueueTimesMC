package fr.mrunicorn.queuetimesmc.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.mrunicorn.queuetimesmc.QueueTimesMC;
import fr.mrunicorn.queuetimesmc.controllers.ParkController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Park {
    private final int id;
    //see https://github.com/MrUnic0rn0/QueueTimesMC/blob/main/wiki/parks_available.md
    private final String name;
    private final HashMap<Integer, Ride> rides;

    public Park(int id, String name) {
        this.id = id;
        this.name = name.trim();
        this.rides = new HashMap<>();
    }

    public void update() {
        JsonElement park = ParkController.getJson(ParkController.getTimeAPI(getId()));
        if (park == null || !park.isJsonObject())
            return;
        JsonObject park_obj = park.getAsJsonObject();
        for (JsonElement land : park_obj.getAsJsonArray("lands")) {
            for (JsonElement ride : land.getAsJsonObject().getAsJsonArray("rides")) {
                updateRide(ride);
            }
        }

        for (JsonElement ride : park_obj.getAsJsonArray("rides")) {
            updateRide(ride);
        }
    }

    private void updateRide(JsonElement ride) {

        if (ride.isJsonNull())
            return;
        JsonObject ride_obj = ride.getAsJsonObject();

        int ride_id = ride_obj.get("id").getAsInt();
        int wait_time = ride_obj.get("wait_time").getAsInt();
        boolean is_open = ride_obj.get("is_open").getAsBoolean();

        if (!rides.containsKey(ride_id)) {
            String ride_name = ride_obj.get("name").getAsString();
            Ride new_ride = new Ride(ride_id, ride_name, is_open ? wait_time : -1);
            rides.put(ride_id, new_ride);
        } else {
            rides.get(ride_id).setWaitTime(is_open ? wait_time : -1);
        }

    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Ride getRide(int id) {
        return rides.get(id);
    }

    public Collection<Ride> getRides() {
        return rides.values();
    }

    @Override
    public String toString() {
        return getId() + " : " + getName();
    }

    public String getRideList(int nb_page) {
        int max_page = rides.size() / QueueTimesMC.max_items + ((rides.size() % QueueTimesMC.max_items != 0) ? 1 : 0);

        if (nb_page > max_page)
            nb_page = max_page;
        if (nb_page < 1)
            nb_page = 1;
        String message = nb_page + "/" + max_page;
        List<Ride> list = new ArrayList<Ride>(rides.values());
        for (int i = (nb_page - 1) * QueueTimesMC.max_items; i < nb_page * QueueTimesMC.max_items; i++) {
            if (i < list.size()) {
                message += "\n §a* " + list.get(i).toString();
            }
        }
        return message;
    }
}
