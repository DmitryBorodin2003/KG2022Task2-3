package ru.vsu.cs.kg2022.borodin;

public class BresenhamArcDrawer implements ArcDrawer{
    private PixelDrawer pd;

    public BresenhamArcDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }
}
