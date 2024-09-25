package com.Cardinal.GTNHPregenerator.Commands;

import com.Cardinal.GTNHPregenerator.ChunkLoader.ChunkLoaderManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class GeneratorCommand extends CommandBase {

    private final List<String> aliases;

    public GeneratorCommand() {
        aliases = new ArrayList<>();
        aliases.add("pregenerate");
    }

    @Override
    public String getCommandName() {
        return "pregenerate";
    }

    // x, z can be ~ to be around the calling player.
    // /pregenerate <x> <z> <radius>
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.pregenerate.usage";
    }

    @Override
    public List<String> getCommandAliases()
    {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {

        if (args.length > 2)
        {
            double xLoc, zLoc;
            if (args[0].equals("~"))
            {
                xLoc = sender.getPlayerCoordinates().posX;
            }
            else
            {
                xLoc = parseDoubleBounded(sender, args[0], -30000000.0D, 30000000.0D);
            }

            if (args[1].equals("~"))
            {
                zLoc = sender.getPlayerCoordinates().posZ;
            }
            else
            {
                zLoc = parseDoubleBounded(sender, args[1], -30000000, 30000000);
            }

            int radius = parseIntBounded(sender, args[2], 1, 10000);
            ChunkLoaderManager CLM = new ChunkLoaderManager(radius, xLoc, zLoc);
            CLM.beginLoading(MinecraftServer.getServer(), sender.getEntityWorld().provider.dimensionId);
            System.out.println("xLoc is: " + xLoc);
            System.out.println("zLoc is: " + zLoc);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
