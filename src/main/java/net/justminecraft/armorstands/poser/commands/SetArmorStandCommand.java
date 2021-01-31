package net.justminecraft.armorstands.poser.commands;

import net.justminecraft.armorstands.poser.ArmorStandPoserPlugin;
import net.justminecraft.armorstands.poser.LookingAtArmorstand;
import net.justminecraft.plots.JustPlots;
import net.justminecraft.plots.Plot;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SetArmorStandCommand implements CommandExecutor {

    private final ArmorStandPoserPlugin plugin;
    private LookingAtArmorstand lookingAtArmorstand = new LookingAtArmorstand();

    public SetArmorStandCommand(ArmorStandPoserPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        Plot plot = JustPlots.getPlotAt(player.getLocation());
        if (plot == null || (!plot.isAdded(player.getUniqueId()) && !sender.isOp())) {
            player.sendMessage(ChatColor.RED + "You do not have permission to build here.");
            return false;
        }

        Entity armorStand = lookingAtArmorstand.getEntities(player);

        if(armorStand == null) {
            player.sendMessage(ChatColor.RED + "You are not looking at an armor stand.");
            lookingAtArmorstand.highlightArmorStands(player);
            return false;
        }

        lookingAtArmorstand.highlightArmorStand((ArmorStand) armorStand);

        player.sendMessage(ChatColor.GREEN + "Click here to set armor stand:");
        String s = plugin.armorStandWeb.createHandler(armorStand);
        player.spigot().sendMessage(new ComponentBuilder(s).color(net.md_5.bungee.api.ChatColor.WHITE)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, s)).create());

        return true;
    }
}
