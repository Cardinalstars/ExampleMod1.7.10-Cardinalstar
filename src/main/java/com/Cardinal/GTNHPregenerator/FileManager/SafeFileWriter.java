package com.Cardinal.GTNHPregenerator.FileManager;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SafeFileWriter
{
    private final Path originalFile;
    private final Path tempFile;
    private RandomAccessFile randomAccessFile;

    public SafeFileWriter(Path filePath) throws IOException {
        this.originalFile = filePath;
        this.tempFile = Path.of(filePath + ".tmp");

        randomAccessFile = new RandomAccessFile(tempFile.toFile(), "rw");
    }

    public void writeInt(int value, boolean overwrite) throws IOException {
        if (overwrite)
        {
            randomAccessFile.seek(0);
        }
        randomAccessFile.writeInt(value);
        randomAccessFile.getChannel().force(true);
    }

    public void writeInt(int value) throws IOException {
        writeInt(value, false);
    }

    public int readInt() throws IOException {
        return randomAccessFile.readInt();
    }

    public double readDouble() throws IOException
    {
        return randomAccessFile.readDouble();
    }

    public void writeDouble(double value, boolean overwrite) throws IOException
    {
        if (overwrite)
        {
            randomAccessFile.seek(0);
        }
        randomAccessFile.writeDouble(value);
        randomAccessFile.getChannel().force(true);
    }

    public void writeDouble(double value) throws IOException {
        writeDouble(value, false);
    }
    public void commit() throws IOException {
        randomAccessFile.close();
        Files.move(tempFile, originalFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        randomAccessFile = new RandomAccessFile(tempFile.toFile(), "rw");
    }

    public void clearFile() throws IOException {
        randomAccessFile.setLength(0);
    }
    public void close() throws IOException {
        randomAccessFile.close();
        Files.deleteIfExists(tempFile); // Cleanup the temp file
    }

    public void openForWriting() throws IOException
    {
        if (randomAccessFile == null || !tempFile.toFile().exists()) {
            randomAccessFile = new RandomAccessFile(tempFile.toFile(), "rw");
        }
    }

    public void openForReading() throws IOException
    {
        if (randomAccessFile == null || !originalFile.toFile().exists()) {
            randomAccessFile = new RandomAccessFile(originalFile.toFile(), "rw");
        }
    }

    public boolean fileExists()
    {
        return originalFile.toFile().exists();
    }
}
