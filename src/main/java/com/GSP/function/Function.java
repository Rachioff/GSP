package com.GSP.function;

import java.util.Objects;

public abstract class Function {

    private static int idCounter = 1;
    private int id;

    private String expression;

    protected String type;

    public Function() {
        id = idCounter++;
    }

    public int getId() {
        return id;
    }

    // 计算函数某一点值
    public abstract double evaluate(double x);

    // 求导
    public abstract Function derivative();

    public String getExpression() {
        return expression;
    }

    public double[][] getPoints(double start, double end, int points) {
        if (points < 2) {
            throw new IllegalArgumentException("Number of points must be at least 2");
        }
        if (start > end) {
            throw new IllegalArgumentException("Start must be less than or equal to end");
        }
        
        double[][] result = new double[points][2];
        double step = (end - start) / (points - 1);
        
        for (int i = 0; i < points; i++) {
            double x = start + i * step;
            result[i][0] = x;
            try {
                result[i][1] = evaluate(x);
            } catch (Exception e) {
                result[i][1] = Double.NaN; // 处理不连续点或未定义点
            }
        }
        
        return result;
    }

    protected void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Function other = (Function) obj;
        return Objects.equals(expression, other.expression);
    }

    public static void main(String[] args) {
        ComplexFunction f = new ComplexFunction("x+1");
        Function der = f.derivative();
        System.out.println(der.toString());
        System.out.println(der.evaluate(2));
    }
}
