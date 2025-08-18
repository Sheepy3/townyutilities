package town.sheepy.townyUtilities.commands;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import town.sheepy.townyUtilities.TownyUtilities;

import java.util.Collection;

public class purgenations implements CommandExecutor {

    private final TownyUtilities plugin;

    public purgenations(TownyUtilities plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Collection<Nation> nations = TownyUniverse.getInstance().getNations();

        for (Nation nation : nations) {
            String nationName = nation.getName();

            String delCmd = "townyadmin nation " + nationName + " delete";
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
