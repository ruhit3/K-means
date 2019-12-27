package com.example.canvasdemo;

public class Point {

    private double x = 0;
    private double y = 0;
    private int color = 0;
    private int cluster_number = 0;

    // constructor
    public Point(double x, double y, int color) {
        this.setX(x);
        this.setY(y);
        this.setColor(color);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return this.y;
    }

    public void setCluster(int n) {
        this.cluster_number = n;
    }

    public int getCluster() {
        return this.cluster_number;
    }

    public void setColor(int n) {
        this.color = n;
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return "(" + (int) this.x + "," + (int) this.y + "," + (int) this.color + ")";
    }

    public double distance(Point p) {
        return Math.sqrt(Math.pow(p.getX() - this.getX(), 2) + Math.pow(p.getY() - this.getY(), 2));
    }

}
