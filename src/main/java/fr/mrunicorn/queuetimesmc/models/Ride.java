package fr.mrunicorn.queuetimesmc.models;

public class Ride {
    private final Integer id;
    private final String name;
    private int wait_time;

    public Ride(int id, String name, int wait_time) {
        this.id = id;
        this.name = name;
        this.wait_time = wait_time;
    }

    public void setWaitTime(int wait_time) {
        this.wait_time = wait_time;
    }

    public int getWaitTime() {
        return this.wait_time;
    }
}
