package fr.mrunicorn.queuetimesmc;

import fr.mrunicorn.queuetimesmc.controllers.ParkController;
import org.bukkit.plugin.java.JavaPlugin;

public final class QueueTimesMC extends JavaPlugin {
    private ParkController controller;

    @Override
    public void onEnable() {
        this.controller = new ParkController();

    }

    @Override
    public void onDisable() {

    }
}
