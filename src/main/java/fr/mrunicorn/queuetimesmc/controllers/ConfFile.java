package fr.mrunicorn.queuetimesmc.controllers;

import fr.mrunicorn.queuetimesmc.QueueTimesMC;

import java.util.ArrayList;
import java.util.Collection;

public class ConfFile {

    public static String open="Open", close = "Closed",min="min";
    public static int default_time = 5;
    public static ArrayList<Integer> active_parks = new ArrayList<Integer>();

    public static void load_conf(){
        QueueTimesMC.getInstance().saveDefaultConfig();
        ConfFile.open = QueueTimesMC.getInstance().getConfig().getString("open");
        ConfFile.close = QueueTimesMC.getInstance().getConfig().getString("close");
        ConfFile.min = QueueTimesMC.getInstance().getConfig().getString("min");
        ConfFile.default_time = QueueTimesMC.getInstance().getConfig().getInt("default_time");

        active_parks.clear();
        active_parks.addAll((Collection<? extends Integer>) QueueTimesMC.getInstance().getConfig().getList("active_parks"));
    }

    public static void save_conf(){
        QueueTimesMC.getInstance().getConfig().set("active_parks",active_parks);
        QueueTimesMC.getInstance().saveConfig();
    }

    public static boolean update_park(int park_id, boolean add){
        if(add && !active_parks.contains(park_id)){
            active_parks.add(park_id);
            return true;
        }else if(!add && active_parks.contains(park_id)){
            active_parks.remove(active_parks.indexOf(park_id));
            return true;
        }
        return false;
    }

}
