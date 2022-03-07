package fr.mrunicorn.queuetimesmc.models;

public class Ride {
    private final Integer id;
    private final String name;
    private int wait_time;

    public Ride(int id, String name, int wait_time) {
        this.id = id;
        name = name.replace('\u00A0', ' ').trim();
        this.name = name;
        this.wait_time = wait_time;
    }

    public void setWaitTime(int wait_time) {
        this.wait_time = wait_time;
    }

    public int getWaitTime() {
        return this.wait_time;
    }

    @Override
    public String toString() {
        return id + " : " + name + " > " + getStrWaitTime();
    }

    public String getStrWaitTime() {
        if (wait_time < 0) {
            return "Closed";
        }
        return wait_time + "min";
    }

    public String getName() {
        return this.name;
    }

    public String getStatus() {
        if (wait_time < 0) {
            return "Closed";
        }
        return "Open";
    }
}
