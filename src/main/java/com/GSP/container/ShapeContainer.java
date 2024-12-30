package com.GSP.container;

import java.util.ArrayList;

import com.GSP.factory.ShapeFactory;
import com.GSP.shape.Shape;

public class ShapeContainer {
    private static final ShapeContainer container = new ShapeContainer();
    private ArrayList<Shape> shapes = new ArrayList<>();

    // 单例模式
    private ShapeContainer() {}

    public static ShapeContainer getContainer() {
        return container;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    /* 
    option说明：
    1. 椭圆（Ellipse）
    2. 双曲线（Hyperbola）
    */
    public void createShape(String type, double[] params) {
        ShapeFactory instance = ShapeFactory.getInstance();
        Shape newShape = null;

        if ("ellipse".equalsIgnoreCase(type)) {
            if (params.length != 4) {
                throw new IllegalArgumentException("Ellipse parameters should be in the format [h, k, a, b]");
            }
            newShape = instance.createEllipse(params[0],params[1],params[2],params[3]);
        } else if ("hyperbola".equalsIgnoreCase(type)) {
            if (params.length != 4) {
                throw new IllegalArgumentException("Hyperbola parameters should be in the format [h, k, a, b]");
            }
            newShape = instance.createHyperbola(params[0],params[1],params[2],params[3]);
        } else {
            throw new IllegalArgumentException("Invalid shape type: " + type);
        }

        addShape(newShape);
    }

    public void deleteShape(int id) {
        boolean found = false;
        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i).getId() == id) {
                shapes.remove(i);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Shape with ID " + id + " not found");
        }
    }
}
