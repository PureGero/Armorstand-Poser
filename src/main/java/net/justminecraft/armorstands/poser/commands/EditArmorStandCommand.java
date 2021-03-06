package net.justminecraft.armorstands.poser.commands;

import net.justminecraft.armorstands.poser.ArmorStandData;
import net.justminecraft.armorstands.poser.LookingAtArmorstand;
import net.justminecraft.armorstands.poser.NBTHandler;
import net.justminecraft.plots.JustPlots;
import net.justminecraft.plots.Plot;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class EditArmorStandCommand extends SubCommand {
    private Player player;
    private LookingAtArmorstand lookingAtArmorstand = new LookingAtArmorstand();
    private NBTHandler nbtHandler = new NBTHandler();

    private EditTypes editTypes = new EditTypes();

    private ArmorStand armorStand;

    public EditArmorStandCommand() {
        super("/ase boolean <option> [boolean]","boolean","Edit boolean armorstand data", "boolean", "bool");
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command.");
            return false;
        }

        player = (Player) sender;

        Plot plot = JustPlots.getPlotAt(player.getLocation());
        if ((plot == null && !sender.hasPermission("ase.editanywhere")) || (!plot.isAdded(player.getUniqueId()) && !sender.hasPermission("ase.edit.other"))) {
            player.sendMessage(ChatColor.RED + "You do not have permission to build here.");
            return false;
        }

        Entity armorStand = lookingAtArmorstand.getEntities(player);

        if(armorStand == null) {
            player.sendMessage(ChatColor.RED + "You are not looking at an armor stand.");
            return false;
        }

        Plot armorStandPlot = JustPlots.getPlotAt(armorStand);

        if(!sender.hasPermission("ase.editanywhere") && (armorStandPlot == null || armorStandPlot != plot)) {
            player.sendMessage(ChatColor.RED + "You cannot edit this Armor Stand.");
            return false;
        }

        this.armorStand = (ArmorStand) armorStand;

        int argsLength = args.length;

        if(argsLength > 0) {
            String var = args[0].toLowerCase();
            switch (editTypes.getType(var)) {
                case "boolean": {
                    boolean setTo = getBoolean(var, args);
                    editBoolean(var, setTo);
                    System.out.println(var +" "+ setTo);
                }
            }
        }

        return true;
    }

    public boolean getBoolean(String var, String[] args) {
        ArmorStandData as = new ArmorStandData(armorStand);
        if(args.length > 1) {
            if(args[1] == "true") return true;
            if(args[1] == "false") return false;
        }
        System.out.println(var);
        switch (var) {
            case "visible": {
                System.out.println("visible " + as.visible);
                if(as.visible) return false;
                break;
            }
            case "small": {
                System.out.println("small " + as.small);
                if(as.small) return false;
                break;
            }
            case "baseplate": {
                System.out.println("baseplate " + as.basePlate);
                if(as.basePlate) return false;
                break;
            }
            case "gravity": {
                System.out.println("gravity " + as.gravity);
                if(as.gravity) return false;
                break;
            }
            case "showarms": {
                System.out.println("showarms " + as.showArms);
                if(as.showArms) return false;
                break;
            }
            case "invulnerable": {
                System.out.println("invulnerable " + as.invulnerable);
                if(as.invulnerable) return false;
                break;
            }
        }
        System.out.println("Why is this being run?");
        return true;
    }

    public void editBoolean(String var, boolean out) {
        switch (var) {
            case "visible": {
                armorStand.setVisible(out);
                break;
            }
            case "small": {
                armorStand.setSmall(out);
                break;
            }
            case "baseplate": {
                armorStand.setBasePlate(out);
                break;
            }
            case "gravity": {
                armorStand.setGravity(out);
                break;
            }
            case "showarms": {
                armorStand.setArms(out);
                break;
            }
            case "invulnerable": {
                armorStand.setInvulnerable(out);
                break;
            }
        }
        String tf = out ? "true" : "false";
        sendMsg(var, tf, true);
    }

    private void sendMsg(String var, String out, boolean good) {
        if(good) {
            player.sendMessage(ChatColor.GREEN + "Successfully set \"" + var + "\" to \"" + out + "\".");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to set \"" + var + "\" to \"" + out + "\".");
        }
    }


    @Override
    public void onTabComplete(CommandSender sender, String[] args, List<String> tabCompletion) {
        if(args.length == 1) {
            tabCompletion.add("visible");
            tabCompletion.add("small");
            tabCompletion.add("basePlate");
            tabCompletion.add("gravity");
            tabCompletion.add("showArms");
            tabCompletion.add("invulnerable");
        }
        if(args.length == 2) {
            if(editTypes.getType(args[0].toLowerCase()) == "boolean") {
                tabCompletion.add("true");
                tabCompletion.add("false");
            }
        }
    }

    @Override
    public String getPermission() {
        return getPerm();
    }

    private static String getPerm() {
        return "ase.edit";
    }
}
