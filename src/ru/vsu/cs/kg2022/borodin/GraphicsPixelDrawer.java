package ru.vsu.cs.kg2022.borodin;

import ru.vsu.cs.kg2022.borodin.PixelDrawer;

import java.awt.*;

public class GraphicsPixelDrawer implements PixelDrawer {
    private Graphics2D g2d;

    public GraphicsPixelDrawer(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawPixel(int x, int y) {
        g2d.fillRect(x,y,1,1);
    }
}
