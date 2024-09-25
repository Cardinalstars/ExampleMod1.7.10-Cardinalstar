package com.Cardinal.GTNHPregenerator.Event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;

public class ChatEventHandler
{
    @SubscribeEvent
    public void onCommandExecuted(CommandEvent event) {
        ICommandSender sender = event.sender;
        String command = event.command.getCommandName();
        String[] args = event.parameters;

        System.out.println("Command executed: /" + command + " by " + sender.getCommandSenderName());
    }

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        System.out.println("Chat message: " + event.message);
    }
}
