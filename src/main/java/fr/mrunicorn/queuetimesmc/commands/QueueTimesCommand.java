package fr.mrunicorn.queuetimesmc.commands;

import fr.mrunicorn.queuetimesmc.QueueTimesMC;
import fr.mrunicorn.queuetimesmc.controllers.ParkController;
import fr.mrunicorn.queuetimesmc.models.Park;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class QueueTimesCommand implements CommandExecutor, TabExecutor {

    private final ParkController controller;

    public QueueTimesCommand(ParkController controller) {

        this.controller = controller;

        QueueTimesMC.getInstance().getCommand("queuetimes").setExecutor(this);
        QueueTimesMC.getInstance().getCommand("queuetimes").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "park":
                    return commandPark(player, args);
                case "help":
                    return commandHelp(player, args);
            }
        }
        return true;
    }

    private boolean commandHelp(Player player, String[] args) {

        return true;
    }

    private boolean commandPark(Player player, String[] args) {
        if (args.length > 1) {
            if (args[1].toLowerCase().contains("list")) {
                int nb_page = 1;
                if (args.length > 2 && args[2].matches("-?(0|[1-9]\\d*)"))
                    nb_page = Integer.parseInt(args[2]);
                if(args[1].equalsIgnoreCase("listall")){
                    this.controller.listParks(player, nb_page);

                }else{
                    this.controller.listActiveParks(player, nb_page);

                }
            } else if (args[1].matches("-?(0|[1-9]\\d*)")) {
                int park_id = Integer.parseInt(args[1]);
                Park park = controller.getPark(park_id);
                if (park == null) {
                    player.sendMessage(ParkController.prefix + "§cPark not found!");
                    return true;
                }
                int nb_page = 1;
                if (args.length > 2) {
                    if (args[2].equalsIgnoreCase("set")) {
                        if (args.length > 3) {
                            if (args[3].equalsIgnoreCase("true")) {
                                controller.addActivePark(park.getId());
                                return true;
                            } else if (args[3].equalsIgnoreCase("false")) {
                                controller.removeActivePark(park.getId());
                                return true;
                            }
                        }
                    } else if (args[2].matches("-?(0|[1-9]\\d*)")) {
                        nb_page = Integer.parseInt(args[2]);
                    }
                }
                if (controller.isActivePark(park_id)) {
                    player.sendMessage(ParkController.prefix + "§e " + park.getName() + " : " + park.getRideList(nb_page));
                } else {
                    player.sendMessage(ParkController.prefix + "§cPark not activated!");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("help");
            completions.add("park");
        } else if (args.length > 1 && args[0].equalsIgnoreCase("park")) {
            if (args.length == 2) {
                completions.add("list");
                completions.add("listall");
            } else if (args[1].matches("-?(0|[1-9]\\d*)")) {
                if (!args[1].toLowerCase().contains("list")) {
                    if (args.length == 3) {
                        completions.add("set");
                    } else if (args.length == 4 && args[2].equalsIgnoreCase("set")) {
                        completions.add("true");
                        completions.add("false");
                    }
                }
            }
        }
        return completions;
    }
}
