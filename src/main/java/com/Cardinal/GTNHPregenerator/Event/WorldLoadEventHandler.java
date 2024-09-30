package com.Cardinal.GTNHPregenerator.Event;

import com.Cardinal.GTNHPregenerator.ChunkLoader.ChunkLoaderManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;

public class WorldLoadEventHandler
{
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote)
        {
            int dimensionId = event.world.provider.dimensionId;
            MinecraftServer server = MinecraftServer.getServer();
            if (!ChunkLoaderManager.instance.isGenerating() && !ChunkLoaderManager.instance.intializeFromPregeneratorFiles(server, dimensionId))
            {
                ChunkLoaderManager.instance.reset(false);
                System.out.println("No pregenerator to load for dimension Id: " + dimensionId);
            }
            else
            {
                System.out.println("Pregenerator loaded and running for dimension Id: " + dimensionId);
            }
        }
    }
}
