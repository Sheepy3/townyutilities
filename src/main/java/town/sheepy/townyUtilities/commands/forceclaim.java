package town.sheepy.townyUtilities.commands;

import com.palmergames.bukkit.towny.exceptions.AlreadyRegisteredException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.*;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.World;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownyUniverse;
import org.bukkit.inventory.meta.FireworkMeta;

public class forceclaim implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Expect: /townyadmin claim <town> <x> <z>
        if (args.length < 3) {
            TownyMessaging.sendErrorMsg(sender, "Usage: /townyadmin claim <town> <x> <z> <flag>");
            return true;
        }

        String townName = args[0];
        Town town = TownyUniverse.getInstance().getTown(townName);
        if (town == null) {
            TownyMessaging.sendErrorMsg(sender, "Town not found: " + townName);
            return true;
        }

        TownyWorld world = TownyAPI.getInstance().getTownyWorld("world");
        int chunkX,chunkZ;
        int coordx,coordy;

        if (args.length == 4 && "-w".equalsIgnoreCase(args[3])) {
            chunkX = Integer.parseInt(args[1]);
            chunkZ = Integer.parseInt(args[2]);
        }else{
            coordx = Integer.parseInt(args[1]);
            coordy = Integer.parseInt(args[2]);
            WorldCoord worldCoord = WorldCoord.parseWorldCoord(String.valueOf(world), coordx, coordy);
            Coord c = worldCoord.getCoord();
            chunkX = c.getX();
            chunkZ = c.getZ();
        }
        Coord coord = new Coord(chunkX, chunkZ);

        try {
            TownBlock oldBlock = world.getTownBlock(coord);
            Town oldTown = oldBlock.getTown();

            // Unregister from the old town and global maps
            oldTown.removeTownBlock(oldBlock);
            TownyUniverse.getInstance().removeTownBlock(oldBlock);
            //oldBlock.clear();  // remove from disk

            TownyMessaging.sendMsg(sender,
                    String.format("Removed claim at %s from town %s.", coord, oldTown.getName())
            );
        } catch (NotRegisteredException ignored) {
            // no existing claim — nothing to remove
        }



        // Create & register the claim
        TownyMessaging.sendMsg(sender,
                String.format("registering claim..."+coord.toString()));

        TownBlock newBlock = new TownBlock(chunkX, chunkZ, world);
        newBlock.setTown(town);
        newBlock.setType(TownBlockType.RESIDENTIAL);

        TownyUniverse.getInstance().addTownBlock(newBlock);
        newBlock.save();  // Persist immediately
        spawnFirework(chunkX,chunkZ,world);
        TownyMessaging.sendMsg(sender,
                String.format("Chunk (%d, %d) force‑claimed for town %s.", chunkX, chunkZ, town.getName())
        );

        // Refresh protections immediately
        Towny.getPlugin().resetCache();

        return true;
    }


    private boolean spawnFirework(int cx,  int cz, TownyWorld townyworld){
        int x = cx*16 + 8;
        int z = cz*16 + 8;
        World world = townyworld.getBukkitWorld();
        int y = world.getHighestBlockYAt(x,z);
        Location fireloc = new Location(world,x+0.5,y+3,z+0.5);
        Firework rocket = world.spawn(fireloc, Firework.class);

        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.STAR)
                .withColor(Color.fromRGB(0xFF9A75))
                .withFade(Color.fromRGB(0xFFA880))
                .trail(true)
                .flicker(true)
                .build();
        FireworkMeta meta = rocket.getFireworkMeta();
        meta.addEffect(effect);
        meta.setPower(1);
        rocket.setFireworkMeta(meta);
        return true;
    }




}
