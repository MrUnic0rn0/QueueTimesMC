package fr.mrunicorn.queuetimesmc.controllers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class ParksPlaceHolder extends PlaceholderExpansion {

    private final ParkController controller;

    public ParksPlaceHolder(ParkController controller) {
        this.controller = controller;
    }

    @Override
    public @NotNull String getAuthor() {
        return "MrUnicorn";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "qt";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }


    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params != null) {
            String args[] = params.split("_");
            if (args.length == 1 && args[0].matches("-?(0|[1-9]\\d*)")) {
                return controller.getPark(Integer.parseInt(args[0])).getName();
            } else if (args.length >= 2 && args[0].matches("-?(0|[1-9]\\d*)") & args[1].matches("-?(0|[1-9]\\d*)")) {
                if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("name")) {
                        return controller.getRideName(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                    } else if (args[2].equalsIgnoreCase("status")) {
                        return controller.getRideStatus(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                    } else if (args[2].equalsIgnoreCase("time")) {
                        return controller.getQueueTimeNumber(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                    }
                }

                return controller.getQueueTime(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }
        }
        return null;
    }
}
