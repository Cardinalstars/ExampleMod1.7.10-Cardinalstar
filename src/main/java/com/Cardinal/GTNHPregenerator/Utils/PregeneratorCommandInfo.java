package com.Cardinal.GTNHPregenerator.Utils;

public class PregeneratorCommandInfo
{
    private final double xLoc;
    private final double zLoc;
    private final int radius;

    public PregeneratorCommandInfo(double xLoc, double zLoc, int radius)
    {
        this.xLoc = xLoc;
        this.zLoc = zLoc;
        this.radius = radius;
    }

    public double getXLoc() {
        return xLoc;
    }

    public double getZLoc() {
        return zLoc;
    }

    public int getRadius() {
        return radius;
    }
}
