package com.Cardinal.GTNHPregenerator.Commands;

import com.Cardinal.GTNHPregenerator.ChunkLoader.ChunkLoaderManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class GeneratorStopCommand extends CommandBase
{

    private final List<String> aliases;

    public GeneratorStopCommand() {
        aliases = new ArrayList<>();
        aliases.add("pregenerateStop");
    }


    @Override
    public String getCommandName() {
        return "pregenerateStop";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.pregenerate.stop.usage";
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
            ChunkLoaderManager.instance.reset(true);
            sender.addChatMessage(new ChatComponentText("Cancelling pregeneration."));
        }
        else
        {
            sender.addChatMessage(new ChatComponentText("No generator running."));
        }
    }
}
