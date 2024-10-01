package com.Cardinal.GTNHPregenerator.Commands;

import com.Cardinal.GTNHPregenerator.ChunkLoader.ChunkLoaderManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class GeneratorProgressCommand extends CommandBase
{
    private final List<String> aliases;

    public GeneratorProgressCommand() {
        aliases = new ArrayList<>();
        aliases.add("pregenerateProgress");
    }

    @Override
    public String getCommandName() {
        return "pregenerateProgress";
    }


    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.pregenerate.progress.usage";
    }

    @Override
    public List<String> getCommandAliases()
    {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (ChunkLoaderManager.instance.isGenerating())
        {
            int totalChunksToLoad = ChunkLoaderManager.instance.getTotalChunksToLoad();
            int chunksLeftToLoad = ChunkLoaderManager.instance.getChunkToLoadSize();
            int chunksLoaded = totalChunksToLoad - chunksLeftToLoad;
            double percentage = (double) chunksLoaded / totalChunksToLoad * 100;
            sender.addChatMessage(new ChatComponentText(
                String.format("Loaded %d chunks of a total of %d. %.1f%% done.", chunksLoaded, totalChunksToLoad, percentage)
            ));
        }
        else
        {
            sender.addChatMessage(new ChatComponentText("No generator running."));
        }
    }

}
