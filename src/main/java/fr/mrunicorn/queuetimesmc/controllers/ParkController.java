package fr.mrunicorn.queuetimesmc.controllers;

public class ParkController {

    private final static String queuetimes_api = "https://queue-times.com/parks";

    public static String getParkAPI() {
        return queuetimes_api+".json";
    }

    public static String getTimeAPI(int id) {
        return String.format("%s/%d/queue_times.json", queuetimes_api, id);
    }
}
