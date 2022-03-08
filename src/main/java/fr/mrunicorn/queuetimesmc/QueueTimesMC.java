package fr.mrunicorn.queuetimesmc;

import fr.mrunicorn.queuetimesmc.controllers.ParkController;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class QueueTimesMC extends JavaPlugin {
    private ParkController controller;
    public final static int max_items = 10;
    private static QueueTimesMC instance;

    @Override
    public void onEnable() {
        this.instance = this;
        this.controller = new ParkController();
    }

    @Override
    public void onDisable() {

    }

    public static QueueTimesMC getInstance() {
        return instance;
    }

}
