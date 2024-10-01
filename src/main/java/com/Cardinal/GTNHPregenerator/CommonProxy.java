package com.Cardinal.GTNHPregenerator;

import com.Cardinal.GTNHPregenerator.Commands.GeneratorProgressCommand;
import com.Cardinal.GTNHPregenerator.Commands.GeneratorStartCommand;
import com.Cardinal.GTNHPregenerator.Commands.GeneratorStopCommand;
import com.Cardinal.GTNHPregenerator.Event.ServerTickEventHandler;
import com.Cardinal.GTNHPregenerator.Event.WorldLoadEventHandler;
import com.Cardinal.GTNHPregenerator.Event.WorldUnloadEventHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        GTNHPregenerator.LOG.info(Config.greeting);
        GTNHPregenerator.LOG.info("I am MyMod at version " + Tags.VERSION);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(new ServerTickEventHandler());
        MinecraftForge.EVENT_BUS.register(new ServerTickEventHandler());
        MinecraftForge.EVENT_BUS.register(new WorldLoadEventHandler());
        MinecraftForge.EVENT_BUS.register(new WorldUnloadEventHandler());
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new GeneratorStartCommand());
        event.registerServerCommand(new GeneratorProgressCommand());
        event.registerServerCommand(new GeneratorStopCommand());
    }
}
