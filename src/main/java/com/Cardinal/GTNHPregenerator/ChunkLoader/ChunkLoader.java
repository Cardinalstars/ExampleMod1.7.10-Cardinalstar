package com.Cardinal.GTNHPregenerator.ChunkLoader;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.ChunkProviderServer;
import org.apache.commons.lang3.tuple.Pair;


public class ChunkLoader
{
    public static void processLoadChunk(MinecraftServer server, int dimensionId, Pair<Integer, Integer> chunk) {
        int x = chunk.getLeft();
        int z = chunk.getRight();

        ChunkProviderServer cps = server.worldServerForDimension(dimensionId).theChunkProviderServer;
        cps.loadChunk(x, z, () -> {
            ChunkLoaderManager.instance.removeChunkFromList();
            System.out.println("Chunk at " + x + ", " + z + " loaded.");
        });
    }
}

