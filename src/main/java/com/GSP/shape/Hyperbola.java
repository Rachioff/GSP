package com.GSP.shape;

public class Hyperbola extends Shape {
    private double h, k, a, b; // 中心点坐标 (h, k)，实轴长度 a，虚轴长度 b

    public Hyperbola(double h, double k, double a, double b) {
        this.type = "hyperbola";
        this.h = h;
        this.k = k;
        this.a = a;
        this.b = b;
        this.setExpression(getStandardizedExpression());
    }

    @Override
    public boolean isPointOnShape(double x, double y) {
        // 判断给定点 (x, y) 是否位于双曲线上
        double normalizedX = (x - h) / a;
        double normalizedY = (y - k) / b;
        return Math.abs(normalizedX * normalizedX - normalizedY * normalizedY - 1) < 1e-6;
    }

    @Override
    public double[] calculateCoordinates(double x) {
        // 基于双曲线方程计算 y 坐标
        // (y-k)^2 = b^2 * ((x - h)^2 / a^2 - 1)
        double y1 = k + Math.sqrt((Math.pow(x - h, 2) / Math.pow(a, 2) - 1) * Math.pow(b, 2));
        double y2 = k - Math.sqrt((Math.pow(x - h, 2) / Math.pow(a, 2) - 1) * Math.pow(b, 2));

        return new double[]{y1, y2}; // 返回两个 y 值
    }

    @Override
    public String getStandardizedExpression() {
        if(h>0 && k>0) return String.format("(x+%.2f)^2/%.2f^2 - (y+%.2f)^2/%.2f^2 = 1", h, a, k, b);
        else if(h<0 && k<0) return String.format("(x%.2f)^2/%.2f^2 - (y%.2f)^2/%.2f^2 = 1", h, a, k, b);
        else if(h>0 && k<0) return String.format("(x+%.2f)^2/%.2f^2 - (y%.2f)^2/%.2f^2 = 1", h, a, k, b);
        else if(h<0 && k>0) return String.format("(x%.2f)^2/%.2f^2 - (y+%.2f)^2/%.2f^2 = 1", h, a, k, b);
        else if(h==0 && k<0) return String.format("x^2/%.2f^2 - (y%.2f)^2/%.2f^2 = 1", a, k, b);
        else if(h==0 && k>0) return String.format("x^2/%.2f^2 - (y+%.2f)^2/%.2f^2 = 1", a, k, b);
        else if(k==0 && h<0) return String.format("(x%.2f)^2/%.2f^2 - y^2/%.2f^2 = 1", h, a, b);
        else if(k==0 && h>0) return String.format("(x+%.2f)^2/%.2f^2 - y^2/%.2f^2 = 1", h, a, b);
        else return String.format("x^2/%.2f^2 - y^2/%.2f^2 = 1",a,b);
    }
    @Override
    public double[][] getPoints(double start, double end, int points) {
        if (points < 2) {
            throw new IllegalArgumentException("Number of points must be at least 2");
        }
        if (start > end) {
            throw new IllegalArgumentException("Start must be less than or equal to end");
        }
        double[][] result = new double[points * 2][2];
        double step = (end - start) / (points - 1);
        int index = 0;
        for (int i = 0; i < points; i++) {
            double x = start + i * step;
            try {
                double[] coordinates = calculateCoordinates(x); // 计算对应的 y1 和 y2
                result[index][0] = x;      // x
                result[index][1] = coordinates[0]; // y1 (上支)
                index++;
                result[index][0] = x;      // x
                result[index][1] = coordinates[1]; // y2 (下支)
                index++;
            } catch (Exception e) {
                result[index][0] = x;
                result[index][1] = Double.NaN;
                index++;

                result[index][0] = x;
                result[index][1] = Double.NaN;
                index++;
            }
        }
        return result;
    }
    @Override
    public double[][] getParametricPoints(int points) {
        // 双曲线需要分别处理左右两支
        int pointsPerBranch = points / 2;
        double[][] result = new double[points][2];
        
        // 参数方程 x = ±a*cosh(t), y = b*sinh(t)
        // t 从 -2 到 2 足够表示主要部分
        
        // 右支
        for (int i = 0; i < pointsPerBranch; i++) {
            double t = 4.0 * i / (pointsPerBranch - 1) - 2;
            result[i][0] = h + a * Math.cosh(t);
            result[i][1] = k + b * Math.sinh(t);
        }
        
        // 左支
        for (int i = 0; i < pointsPerBranch; i++) {
            double t = 4.0 * i / (pointsPerBranch - 1) - 2;
            result[i + pointsPerBranch][0] = h - a * Math.cosh(t);
            result[i + pointsPerBranch][1] = k + b * Math.sinh(t);
        }
        
        return result;
    }
}
