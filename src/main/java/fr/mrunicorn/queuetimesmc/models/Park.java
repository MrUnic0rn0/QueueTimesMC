package fr.mrunicorn.queuetimesmc.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.mrunicorn.queuetimesmc.controllers.ParkController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;

public class Park {
    private final int id;
    //see https://github.com/MrUnic0rn0/QueueTimesMC/blob/main/wiki/parks_available.md
    private final String name;
    private final HashMap<Integer, Ride> rides;

    public Park(int id, String name) {
        this.id = id;
        this.name = name;
        this.rides = new HashMap<>();
    }

    protected void update() {
        JsonObject park_obj;
        try {
            URL url = new URL(ParkController.getTimeAPI(getId()));

            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            park_obj = root.getAsJsonObject();

        } catch (IOException e) {
            e.printStackTrace();
            park_obj = null;
        }
        if (park_obj == null)
            return;

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
        return getName();
    }
}
