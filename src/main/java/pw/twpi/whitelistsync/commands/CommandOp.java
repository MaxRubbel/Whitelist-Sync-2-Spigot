package pw.twpi.whitelistsync.commands;

import org.bukkit.command.ConsoleCommandSender;
import pw.twpi.whitelistsync.Utilities;
import pw.twpi.whitelistsync.service.BaseService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandOp implements CommandExecutor {

    private JavaPlugin plugin;
    private BaseService service;
    private Server server;

    private final String WL_OP_MANAGE = "whitelistsync.wlop.manage";
    private final String WL_OP_VIEW = "whitelistsync.wlop.view";

    public CommandOp(JavaPlugin plugin, BaseService service) {
        this.plugin = plugin;
        this.service = service;
        this.server = plugin.getServer();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (plugin.getConfig().getBoolean("general.sync-ops")) {
            if (args.length > 0) {
                //Action for showing list
                if (args[0].equalsIgnoreCase("list")) {

                    if(!(sender instanceof ConsoleCommandSender) && !sender.hasPermission(WL_OP_VIEW)) {
                        sender.sendMessage("You do not have permission to use this command.");
                        return true;
                    }

                    sender.sendMessage(Utilities.FormatOppedPlayersOutput(service.getOppedPlayersFromDatabase()));

                    return true;
                } else if (args[0].equalsIgnoreCase("op")) {
                    // Actions for adding a player to whitelist
                    if(!(sender instanceof ConsoleCommandSender) && !sender.hasPermission(WL_OP_MANAGE)) {
                        sender.sendMessage("You do not have permission to use this command.");
                        return true;
                    }

                    if (args.length > 1) {

                        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

                        if (player != null) {

                            if (service.addOppedPlayer(player)) {
                                player.setOp(true);
                                sender.sendMessage(player.getName() + " opped!");
                            } else {
                                sender.sendMessage("Error opping " + player.getName() + "!");
                            }

                        } else {
                            sender.sendMessage("User " + args[1] + " not found!");
                        }

                    } else {
                        sender.sendMessage("You must specify a name to op!");
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("deop")) {
                    // Actions for removing player from whitelist
                    if(!(sender instanceof ConsoleCommandSender) && !sender.hasPermission(WL_OP_MANAGE)) {
                        sender.sendMessage("You do not have permission to use this command.");
                        return true;
                    }

                    if (args.length > 1) {

                        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

                        if (player != null) {

                            if (service.removeOppedPlayer(player)) {
                                player.setOp(false);
                                sender.sendMessage(player.getName() + " de-opped!");
                            } else {
                                sender.sendMessage("Error de-opping " + player.getName() + "!");
                            }

                        } else {
                            sender.sendMessage("User " + args[1] + " not found!");
                        }

                    } else {
                        sender.sendMessage("You must specify a valid name to deop!");
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("sync")) {
                    // Sync server to database
                    if(!(sender instanceof ConsoleCommandSender) && !sender.hasPermission(WL_OP_MANAGE)) {
                        sender.sendMessage("You do not have permission to use this command.");
                        return true;
                    }

                    if (service.copyDatabaseOppedPlayersToLocal(server)) {
                        sender.sendMessage("Local up to date with database!");
                    } else {
                        sender.sendMessage("Error syncing local to database!");
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("copyservertodatabase")) {
                    // Sync server to database
                    if(!(sender instanceof ConsoleCommandSender) && !sender.hasPermission(WL_OP_MANAGE)) {
                        sender.sendMessage("You do not have permission to use this command.");
                        return true;
                    }

                    if (service.copyLocalOppedPlayersToDatabase()) {
                        sender.sendMessage("Pushed local to database!");
                    } else {
                        sender.sendMessage("Error pushing local to database!");
                    }

                    return true;
                }
            }
        } else {
            sender.sendMessage("Whitelist Sync Op management is not enabled. You must enable it in the config.");
        }

        return false;
    }
}
