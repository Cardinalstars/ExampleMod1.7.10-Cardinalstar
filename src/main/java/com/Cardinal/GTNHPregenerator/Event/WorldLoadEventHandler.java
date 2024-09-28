package com.Cardinal.GTNHPregenerator.Event;

import com.Cardinal.GTNHPregenerator.ChunkLoader.ChunkLoaderManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WorldLoadEventHandler
{
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote)
        {
            if (ChunkLoaderManager.instance.hasValidPregeneratorFiles())
            {
                if (!ChunkLoaderManager.instance.intializeFromPregeneratorFiles())
                {
                    System.out.println("Pregenerator files was found, but somehow they are corrupted.");
                }

            }
            else
            {
                System.out.println("No pregenerator to load.");
            }
        }
    }
}
