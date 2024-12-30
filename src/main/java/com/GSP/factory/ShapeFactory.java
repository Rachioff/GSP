package com.GSP.factory;

import com.GSP.shape.Ellipse;
import com.GSP.shape.Hyperbola;
import com.GSP.shape.Shape;

public class ShapeFactory {
    private static final ShapeFactory instance = new ShapeFactory();

    private ShapeFactory() {}

    public static ShapeFactory getInstance() {
        return instance;
    }

    // 通过参数数组创建 Shape 对象
    public Shape createShape(String type, double[] params) {
        if (params == null || params.length != 4) {
            throw new IllegalArgumentException("Shape parameters must be an array of four values: [h, k, a, b]");
        }

        double h = params[0];
        double k = params[1];
        double a = params[2];
        double b = params[3];

        switch (type.toLowerCase()) {
            case "ellipse":
                return createEllipse(h, k, a, b);
            case "hyperbola":
                return createHyperbola(h, k, a, b);
            default:
                throw new IllegalArgumentException("Unsupported shape type: " + type);
        }
    }

    // 创建椭圆
    public Ellipse createEllipse(double h, double k, double a, double b) {
        return new Ellipse(h, k, a, b);
    }

    // 创建双曲线
    public Hyperbola createHyperbola(double h, double k, double a, double b) {
        return new Hyperbola(h, k, a, b);
    }
}
