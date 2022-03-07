package fr.mrunicorn.queuetimemc;

import fr.mrunicorn.queuetimemc.controllers.ParkController;
import org.bukkit.plugin.java.JavaPlugin;

public final class QueueTimeMC extends JavaPlugin {
    private ParkController controller;

    @Override
    public void onEnable() {
        this.controller = new ParkController();

    }

    @Override
    public void onDisable() {

    }
}
