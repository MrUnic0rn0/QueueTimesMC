package fr.mrunicorn.queuetimesmc.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.mrunicorn.queuetimesmc.QueueTimesMC;
import fr.mrunicorn.queuetimesmc.commands.QueueTimesCommand;
import fr.mrunicorn.queuetimesmc.models.Park;
import fr.mrunicorn.queuetimesmc.models.Ride;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ParkController {

    private final static String queuetimes_api = "https://queue-times.com/parks";
    private final HashMap<Integer, Park> parks;
    private final ParksPlaceHolder pph;

    public static String prefix = "§1[§9QueueTimesMC§1] ";

    public ParkController() {

        parks = new HashMap<>();

        new QueueTimesCommand(this);
        pph = new ParksPlaceHolder(this);

        new BukkitRunnable() {
            @Override
            public void run() {
                initParks();
            }
        }.runTaskAsynchronously(QueueTimesMC.getInstance());
    }

    private void initParks() {
        JsonElement parks_d = getJson(getParkAPI());
        if (parks_d == null || !parks_d.isJsonArray()) return;
        for (JsonElement company_d : parks_d.getAsJsonArray()) {
            for (JsonElement park_d : company_d.getAsJsonObject().getAsJsonArray("parks")) {
                addPark(park_d);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                pph.register();
            }
        }.runTaskLater(QueueTimesMC.getInstance(), 0L);
        createUpdateParks();
    }

    private void createUpdateParks() {
        long delay = 6000L / (ConfFile.active_parks.size());
        int n = 1;
        for (int i : ConfFile.active_parks) {
            Park park = getPark(i);
            if (park == null) continue;
            park.update();
            new BukkitRunnable() {
                @Override
                public void run() {
                    park.update();
                }
            }.runTaskTimerAsynchronously(QueueTimesMC.getInstance(), n * delay, 6000L);
            n += 1;
        }
    }

    private void addPark(JsonElement park_d) {
        if (park_d == null || !park_d.isJsonObject()) return;
        JsonObject park = park_d.getAsJsonObject();
        int park_id = park.get("id").getAsInt();
        String park_name = park.get("name").getAsString();
        Park nPark = new Park(park_id, park_name);
        parks.put(park_id, nPark);
    }


    public static String getParkAPI() {
        return queuetimes_api + ".json";
    }

    public static String getTimeAPI(int id) {
        return String.format("%s/%d/queue_times.json", queuetimes_api, id);
    }

    public static JsonElement getJson(String urlS) {
        JsonElement root;
        try {
            URL url = new URL(urlS);

            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            root = jp.parse(new InputStreamReader((InputStream) request.getContent(), StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
            root = null;
        }
        return root;
    }

    public void listParks(Player player, int nb_page) {
        int max_page = ConfFile.active_parks.size() / QueueTimesMC.max_items + ((ConfFile.active_parks.size() % QueueTimesMC.max_items != 0) ? 1 : 0);
        if (nb_page < 1) nb_page = 1;
        if (nb_page > max_page) nb_page = max_page;
        StringBuilder message = new StringBuilder(prefix + "§eActivated Parks : §9" + nb_page + "/" + max_page);
        for (int i = (nb_page - 1) * QueueTimesMC.max_items; i < nb_page * QueueTimesMC.max_items; i++) {
            if (i < ConfFile.active_parks.size()) {
                message.append("\n §a* ").append(parks.get(ConfFile.active_parks.get(i)).toString());
            }
        }
        player.sendMessage(message.toString());
    }

    public Park getPark(int park_id) {
        return parks.get(park_id);
    }

    public boolean isActivePark(int park_id) {
        return ConfFile.active_parks.contains(park_id);
    }

    public String getQueueTime(int park_id, int ride_id) {
        if (isActivePark(park_id)) {
            Ride ride = getPark(park_id).getRide(ride_id);
            if (ride != null) return ride.getStrWaitTime();
            return "Ride " + ride_id + " Not Found";
        }
        return "Park " + park_id + " Not Active/Found";
    }

    public String getRideName(int park_id, int ride_id) {
        if (isActivePark(park_id)) {
            Ride ride = getPark(park_id).getRide(ride_id);
            if (ride != null) return ride.getName();
            return "Ride " + ride_id + " Not Found";
        }
        return "Park " + park_id + " Not Active/Found";
    }

    public String getRideStatus(int park_id, int ride_id) {
        if (isActivePark(park_id)) {
            Ride ride = getPark(park_id).getRide(ride_id);
            if (ride != null) return ride.getStatus();
            return "Ride " + ride_id + " Not Found";
        }
        return "Park " + park_id + " Not Active/Found";
    }

    public String getQueueTimeNumber(int park_id, int ride_id) {
        if (isActivePark(park_id)) {
            Ride ride = getPark(park_id).getRide(ride_id);
            if (ride != null) {
                int wait_time = ride.getWaitTime();
                return Integer.toString(wait_time < 0 ? ConfFile.default_time : wait_time);
            }
            return "Ride " + ride_id + " Not Found";
        }
        return "Park " + park_id + " Not Active/Found";
    }
}
