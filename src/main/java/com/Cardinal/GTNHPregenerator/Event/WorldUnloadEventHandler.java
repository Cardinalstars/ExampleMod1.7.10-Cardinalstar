package com.Cardinal.GTNHPregenerator.Event;

import com.Cardinal.GTNHPregenerator.ChunkLoader.ChunkLoaderManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WorldUnloadEventHandler
{
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (!event.world.isRemote)
        {
            if (event.world.provider.dimensionId == ChunkLoaderManager.instance.getDimensionID() && ChunkLoaderManager.instance.isGenerating())
            {
                ChunkLoaderManager.instance.reset(false);
            }
        }
    }
}
