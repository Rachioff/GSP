package com.GSP.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.GSP.dto.ShapeRequest;
import com.GSP.service.ShapeService;
import com.GSP.shape.Shape;

@RestController
@RequestMapping("/api/shapes")
@CrossOrigin(origins = "http://localhost:8080")
public class ShapeController {

    @Autowired
    private ShapeService shapeService;
    // 添加图形
    @PostMapping
    public ResponseEntity<?> addShape(@RequestBody ShapeRequest request) {
        try {
            String expression = request.getExpression();
            double[] params = Shape.parseExpression(expression);
            String shapeType = determineShapeType(expression);
            shapeService.addShape(shapeType, params);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    // 根据表达式判断是椭圆还是双曲线
    private String determineShapeType(String expression){
        int ystart=expression.indexOf("y");
        String str=expression.substring(ystart-2, ystart);
        if(str.contains("+")){
            return "Ellipse";
        }else{
            return "Hyperbola";
        }
    }

    // 获取所有图形
    @GetMapping
    public ResponseEntity<HashMap<Integer, String>> getShapes() {
        try {
            HashMap<Integer, String> shapes = shapeService.getAllShapes();
            return ResponseEntity.ok(shapes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    // 删除指定 ID 的图形
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShape(@PathVariable int id) {
        try {
            shapeService.deleteShape(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    // 获取指定图形的离散点
    @GetMapping("/{id}/points")
    public ResponseEntity<double[][]> getShapePoints(
            @PathVariable int id,
            @RequestParam(defaultValue = "-10") double start,
            @RequestParam(defaultValue = "10") double end,
            @RequestParam(defaultValue = "200") int count) {
        try {
            Shape shape = shapeService.getShape(id);
            if (shape == null) {
                return ResponseEntity.notFound().build();
            }
            double[][] points = shape.getPoints(start, end, count);
            return ResponseEntity.ok(points);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/api/shapes/{id}/parametric-points")
    public double[][] getParametricPoints(@PathVariable int id, 
                                        @RequestParam(defaultValue = "100") int count) {
        Shape shape = shapeService.getShape(id);
        return shape.getParametricPoints(count);
    }
}
