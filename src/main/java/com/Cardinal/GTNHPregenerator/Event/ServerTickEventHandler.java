package com.Cardinal.GTNHPregenerator.Event;

import com.Cardinal.GTNHPregenerator.ChunkLoader.ChunkLoaderManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.server.MinecraftServer;

public class ServerTickEventHandler
{
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (ChunkLoaderManager.instance.isGenerating())
            {
                ChunkLoaderManager.instance.queueChunks(2);
            }
        }
    }
}
