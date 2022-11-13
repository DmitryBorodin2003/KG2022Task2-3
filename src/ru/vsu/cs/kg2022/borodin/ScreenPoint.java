package ru.vsu.cs.kg2022.borodin;

public class ScreenPoint {
    private final int r;
    private final int c;

    public ScreenPoint(int c, int r) {
        this.r = r;
        this.c = c;
    }

    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }
}
