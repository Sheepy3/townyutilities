package town.sheepy.townyUtilities.commands;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import town.sheepy.townyUtilities.TownyUtilities;

import java.util.Collection;
import java.util.Set;

public class purgetowns implements CommandExecutor {

    private final TownyUtilities plugin;

    public purgetowns(TownyUtilities plugin){
        this.plugin = plugin;
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        Collection<Town> towns= TownyUniverse.getInstance().getTowns();

        for (Town town : towns) {
            String townName = town.getName();

            String delCmd = "townyadmin town " + townName + " delete";
            plugin.getServer().dispatchCommand(
                    plugin.getServer().getConsoleSender(),
                    delCmd
            );
            plugin.getServer().dispatchCommand(
                    plugin.getServer().getConsoleSender(),
                    "confirm"
            );

        }
        return true;
    }
}

