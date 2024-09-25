package com.Cardinal.GTNHPregenerator.ChunkLoader;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Vector;

public class ChunkLoaderManager
{
    int radius;
    double xCenter;
    double zCenter;
    private final static int numWorkerThreads = 5;
    private static Thread[] workerThreads = new Thread[numWorkerThreads];
    private Vector<Pair<Integer, Integer>> chunksToLoad = new Vector<>();
    public ChunkLoaderManager(int radius, double xCenter, double zCenter)
    {
        this.radius = radius;
        this.xCenter = xCenter;
        this.zCenter = zCenter;
        findChunksToLoadCircle(radius, xCenter, zCenter);
    }

    // Passed in xCenter and passed in zCenter are both in block coordinates. Be sure to transform to chunk coordinates
    // I've done a ton of testing with this. It works without duplicates and holes in the raster.
    private void findChunksToLoadCircle(int radius, double xCenter, double zCenter)
    {
        // This is a solved problem. I'll use the wikipedia entry on this: https://en.wikipedia.org/wiki/Midpoint_circle_algorithm
        int chunkXCenter = (int) Math.floor(xCenter / 16);
        int chunkZCenter = (int) Math.floor(zCenter / 16);
        double decisionTracker = 1 - radius; // This is used to tell if we need to step X down.
        int x = radius;
        int z = 0;
        int previousX = radius;
        while (x >= z)
        {
            // Add all symmetrical points
            addChunk(chunkXCenter + x, chunkZCenter + z);
            addChunk(chunkXCenter - x, chunkZCenter + z);
            if (z != x)
            {
                addChunk(chunkXCenter + z, chunkZCenter + x);
                addChunk(chunkXCenter + z, chunkZCenter - x);
            }


            if (z != 0)
            {
                addChunk(chunkXCenter + x, chunkZCenter - z);
                addChunk(chunkXCenter - x, chunkZCenter - z);
                if (z != x)
                {
                    addChunk(chunkXCenter - z, chunkZCenter + x);
                    addChunk(chunkXCenter - z, chunkZCenter - x);
                }

            }

            if(x != previousX)
            {
                addChunksBetween( x, -z, z);
                addChunksBetween(-x, -z, z);
            }
            previousX = x;

            if (x != z)
            {
                addChunksBetween( z, -x, x);
                if (z != 0)
                {
                    addChunksBetween(-z, -x, x);
                }
            }

            z++;
            if (decisionTracker < 0)
            {
                decisionTracker += 2 * z + 1;
            }
            else
            {
                x--;
                decisionTracker += 2 * (z - x) + 1;
            }
        }
        System.out.printf("Found %s chunks to load", chunksToLoad.size());
    }

    private void addChunksBetween(int xLine, int zMin, int zMax)
    {
        for (int z = zMin + 1; z <= zMax - 1; z++) {
            addChunk(xLine, z);
        }
    }

    private void addChunk(int chunkX, int chunkZ)
    {
        chunksToLoad.add(Pair.of(chunkX, chunkZ));
    }

    public void beginLoading(MinecraftServer server, int dimensionId)
    {
        for(int i = 0; i < numWorkerThreads; i++)
        {
            final int threadIndex = i; // Local copy of the thread index

            // Start each thread
            workerThreads[i] = new Thread(() -> {
                for (int j = threadIndex; j < chunksToLoad.size(); j += numWorkerThreads) {
                    Pair<Integer, Integer> chunk = chunksToLoad.get(j);
                    int x = chunk.getLeft();
                    int z = chunk.getRight();

                    ChunkProviderServer cps = server.worldServerForDimension(dimensionId).theChunkProviderServer;

                    if (!chunksExist(x, z, dimensionId)) {
                        cps.loadChunk(x, z);
                        cps.unloadChunksIfNotNearSpawn(x, z);
                    }
                }
            });
            workerThreads[i].start();

        }
    }

    private static boolean chunksExist(int x, int z, int dimensionID) {
        WorldServer world = null;
        world = DimensionManager.getWorld(dimensionID);
        return RegionFileCache.createOrLoadRegionFile(world.getChunkSaveLocation(), x, z).chunkExists(x & 0x1F, z & 0x1F);
    }
}
