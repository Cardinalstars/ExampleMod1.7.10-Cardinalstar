package com.Cardinal.GTNHPregenerator.ChunkLoader;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ChunkLoaderManager
{
    public final static ChunkLoaderManager instance = new ChunkLoaderManager();
    private boolean isGenerating = false;
    private int dimensionID;
    private MinecraftServer serverType;
    private Vector<Pair<Integer, Integer>> chunksToLoad = new Vector<>(1000);
    private int chunkToLoadIndex;

    public void initializePregenerator(int radius, double zLoc, double xLoc, MinecraftServer server, int dimensionID)
    {
        findChunksToLoadCircle(radius, zLoc, xLoc);
        chunkToLoadIndex = chunksToLoad.size() - 1;
        this.dimensionID = dimensionID;
        this.isGenerating = true;
        this.serverType = server;
    }

    public boolean isGenerating()
    {
        return this.isGenerating;
    }
    // Passed in xCenter and passed in zCenter are both in block coordinates. Be sure to transform to chunk coordinates
    // I've done a ton of testing with this. It works without duplicates and holes in the raster.
    public void findChunksToLoadCircle(int radius, double xCenter, double zCenter)
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

    public void removeChunkFromList()
    {
        chunksToLoad.remove(chunksToLoad.size() - 1);
    }

    private void addChunk(int chunkX, int chunkZ)
    {
        chunksToLoad.add(Pair.of(chunkX, chunkZ));
    }

    public void queueChunks(int numChunksToQueue)
    {
        for (int i = 0; i < numChunksToQueue; i++)
        {
            if (!chunksToLoad.isEmpty())
            {
                ChunkLoader.processLoadChunk(this.serverType, this.dimensionID, chunksToLoad.get(chunkToLoadIndex));
                chunkToLoadIndex--;
            }
            else
            {
                isGenerating = false;
            }
        }
    }

}
