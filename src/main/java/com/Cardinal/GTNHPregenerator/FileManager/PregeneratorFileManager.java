package com.Cardinal.GTNHPregenerator.FileManager;

import com.Cardinal.GTNHPregenerator.Utils.PregeneratorCommandInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.SaveHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class PregeneratorFileManager
{
    boolean fileAIsMostRecent = true;

    private final SafeFileWriter commandWriter;
    private final SafeFileWriter iterationWriter;
    public PregeneratorFileManager(MinecraftServer server, double xLoc, double zLoc, int radius) throws IOException
    {
        Path temporaryFileSaveFolder = getWorldFolderPath(server).resolve("pregenerationFiles");
        if (!Files.exists(temporaryFileSaveFolder)) {
            Files.createDirectories(temporaryFileSaveFolder);
        }
        Path fileIterationPath = temporaryFileSaveFolder.resolve("fileIteration.txt");
        Path fileCommandPath = temporaryFileSaveFolder.resolve("fileCommand.txt");

        commandWriter = new SafeFileWriter(fileCommandPath);
        commandWriter.clearFile();
        commandWriter.writeDouble(xLoc);
        commandWriter.writeDouble(zLoc);
        commandWriter.writeInt(radius);
        commandWriter.close();

        this.iterationWriter = new SafeFileWriter(fileIterationPath);
    }


    public boolean canResumeCommand()
    {
        return commandWriter.fileExists() && iterationWriter.fileExists();
    }

    public Optional<PregeneratorCommandInfo> getCommandInfo()
    {
        try
        {
            commandWriter.openForReading();
            return Optional.of(new PregeneratorCommandInfo(commandWriter.readDouble(), commandWriter.readDouble(), commandWriter.readInt()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void saveIteration(int iteration)
    {
        try
        {
            iterationWriter.writeInt(iteration, true);
            iterationWriter.commit();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void closeAndRemoveAllFiles()
    {
        try
        {
            iterationWriter.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private Path getWorldFolderPath(MinecraftServer server) {
        return Paths.get((server.getActiveAnvilConverter().getSaveLoader(server.getFolderName(), false)).getWorldDirectoryName());
    }
}
