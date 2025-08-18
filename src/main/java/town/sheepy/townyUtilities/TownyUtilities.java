package town.sheepy.townyUtilities;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import org.bukkit.plugin.java.JavaPlugin;
import town.sheepy.townyUtilities.commands.forceclaim;
import town.sheepy.townyUtilities.commands.purgenations;
import town.sheepy.townyUtilities.commands.purgetowns;
public final class TownyUtilities extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Register "/townyadmin claim" as a subcommand of Towny’s admin commands
        TownyCommandAddonAPI.addSubCommand(
                TownyCommandAddonAPI.CommandType.TOWNYADMIN,    // attach to /townyadmin
                "claim",                                       // subcommand name
                new forceclaim()                     // your CommandExecutor implementation
        );
        TownyCommandAddonAPI.addSubCommand(
                TownyCommandAddonAPI.CommandType.TOWNYADMIN,    // attach to /townyadmin
                "purgetowns",                                       // subcommand name
                new purgetowns(this)                     // your CommandExecutor implementation
        );
        TownyCommandAddonAPI.addSubCommand(
                TownyCommandAddonAPI.CommandType.TOWNYADMIN,    // attach to /townyadmin
                "purgenations",                                       // subcommand name
                new purgenations(this)                     // your CommandExecutor implementation
        );

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
