package com.Cardinal.GTNHPregenerator.Event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WorldUnloadEventHandler
{
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (!event.world.isRemote)
        {

        }
    }
}
