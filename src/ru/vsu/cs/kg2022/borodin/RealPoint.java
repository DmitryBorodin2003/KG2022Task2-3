package ru.vsu.cs.kg2022.borodin;

public class RealPoint {
    private double x, y;
    boolean isAvailable = true;
    boolean used = false;

    public RealPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public RealPoint minus(RealPoint p){
        return new RealPoint(getX() - p.getX(), getY() - p.getY());
    }

    @Override
    public String toString() {
        return "ru.vsu.cs.kg2022.borodin.ru.vsu.cs.kg2022.borodin.LineDrawers.LineDrawers.RealPoint{" +
                "x=" + x +
                ", y=" + y;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
