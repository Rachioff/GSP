package com.GSP.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.GSP.container.ShapeContainer;
import com.GSP.shape.Shape;

@Service
public class ShapeService {
    private ShapeContainer shapeContainer = ShapeContainer.getContainer();

    /**
     * 添加新的图形表达式
     * @param expression 图形表达式字符串
     */
    public void addShape(String type, double[] params) {
        shapeContainer.createShape(type, params);
    }
    /**
     * 获取所有已添加的图形
     * @return 图形表达式列表
     */
    public HashMap<Integer, String> getAllShapes() {
        HashMap<Integer, String> shapes = new HashMap<>();
        for (Shape shape : shapeContainer.getShapes()) {
            shapes.put(shape.getId(), shape.getExpression());
        }
        return shapes;
    }

    /**
     * 删除图形
     * @param id 要被删除的图形id
     */
    public void deleteShape(int id) {
        shapeContainer.deleteShape(id);
    }

    /**
     * 获取图形
     * @param id 要被获取的图形id
     * @return 图形对象
     */
    public Shape getShape(int id) {
        for (Shape shape : shapeContainer.getShapes()) {
            if (shape.getId() == id) {
                return shape;
            }
        }
        return null;
    }
}
