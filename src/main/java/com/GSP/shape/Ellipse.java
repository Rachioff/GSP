package com.GSP.shape;

public class Ellipse extends Shape {
    private double h, k, a, b; // 中心点坐标 (h, k)，半长轴 a，半短轴 b

    public Ellipse(double h, double k, double a, double b) {
        this.type = "ellipse";
        this.h = h;
        this.k = k;
        this.a = a;
        this.b = b;
        this.setExpression(getStandardizedExpression());
    }

    @Override
    public boolean isPointOnShape(double x, double y) {
        // 检查给定点是否在椭圆上
        double normalizedX = (x - h) / a;
        double normalizedY = (y - k) / b;
        return Math.abs(normalizedX * normalizedX + normalizedY * normalizedY - 1) < 1e-6;
    }

    @Override
    public double[] calculateCoordinates(double x) {
        // 基于椭圆方程计算 y 坐标
        if (Math.abs(x - h) > a) {
            throw new IllegalArgumentException("x is out of range for this ellipse.");
        }

        // y^2 = b^2 * (1 - (x - h)^2 / a^2)
        double y1 = k + Math.sqrt((1 - Math.pow(x - h, 2) / Math.pow(a, 2)) * Math.pow(b, 2));
        double y2 = k - Math.sqrt((1 - Math.pow(x - h, 2) / Math.pow(a, 2)) * Math.pow(b, 2));

        return new double[]{y1, y2}; // 返回两个y值
    }

    @Override
    public String getStandardizedExpression() {
        if(h>0 && k>0) return String.format("(x+%.2f)^2/%.2f^2 + (y+%.2f)^2/%.2f^2 = 1", h, a, k, b);
        else if(h<0 && k<0) return String.format("(x%.2f)^2/%.2f^2 + (y%.2f)^2/%.2f^2 = 1", h, a, k, b);
        else if(h>0 && k<0) return String.format("(x+%.2f)^2/%.2f^2 + (y%.2f)^2/%.2f^2 = 1", h, a, k, b);
        else if(h<0 && k>0) return String.format("(x%.2f)^2/%.2f^2 + (y+%.2f)^2/%.2f^2 = 1", h, a, k, b);
        else if(h==0 && k<0) return String.format("x^2/%.2f^2 + (y%.2f)^2/%.2f^2 = 1", a, k, b);
        else if(h==0 && k>0) return String.format("x^2/%.2f^2 + (y+%.2f)^2/%.2f^2 = 1", a, k, b);
        else if(k==0 && h<0) return String.format("(x%.2f)^2/%.2f^2 + y^2/%.2f^2 = 1", h, a, b);
        else if(k==0 && h>0) return String.format("(x+%.2f)^2/%.2f^2 + y^2/%.2f^2 = 1", h, a, b);
        else return String.format("x^2/%.2f^2 + y^2/%.2f^2 = 1",a,b);
    }
    @Override
    public double[][] getPoints(double start, double end, int points) {
        if (points < 2) {
            throw new IllegalArgumentException("Number of points must be at least 2");
        }
        if (start > end) {
            throw new IllegalArgumentException("Start must be less than or equal to end");
        }
        double[][] result = new double[2 * points][2]; 
        double step = (end - start) / (points - 1);
        int index = 0;
        for (int i = 0; i < points; i++) {
            double x = start + i * step;
            result[index][0] = x;
        try {
            double[] coordinates = calculateCoordinates(x); // 计算对应的两个 y 值
            result[index][1] = coordinates[0]; // 上支 y1
            index++;
            result[index][0] = x;
            result[index][1] = coordinates[1]; // 下支 y2
            index++;
        } catch (Exception e) {
            result[index][1] = Double.NaN; // 如果计算失败，设置为 NaN
            index++;
            result[index][1] = Double.NaN;
            index++;
        }
        }
        return result;
    }
    @Override
    public double[][] getParametricPoints(int points) {
        double[][] result = new double[points][2];
        // 使用参数方程 x = h + a*cos(t), y = k + b*sin(t)
        // t 从 0 到 2π
        for (int i = 0; i < points; i++) {
            double t = 2 * Math.PI * i / (points - 1);
            result[i][0] = h + a * Math.cos(t);
            result[i][1] = k + b * Math.sin(t);
        }
        return result;
    }
}