package com.GSP.controller;

import com.GSP.dto.FunctionRequest;
import com.GSP.function.Function;
import com.GSP.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/functions")
@CrossOrigin(origins = "http://localhost:8080") // 允许前端开发服务器访问
public class FunctionController {

    @Autowired
    private static FunctionService functionService = new FunctionService();

    @PostMapping
    public ResponseEntity<?> addFunction(@RequestBody FunctionRequest request) {
        try {
            functionService.addFunction(request.getExpression());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // @PostMapping("{/id}")
    // public ResponseEntity<?> deriveFunction(@PathVariable int id) {
    //     try {
    //         functionService.deriveFunction(id);
    //         return ResponseEntity.ok().build();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.internalServerError().build();
    //     }
    // }

    @GetMapping
    public ResponseEntity<HashMap<Integer, String>> getFunctions() {
        try {
            HashMap<Integer, String> functions = functionService.getAllFunctions();
            return ResponseEntity.ok(functions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFunction(@PathVariable int id) {
        try {
            functionService.deleteFunction(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<double[][]> getFunctionPoints(
            @PathVariable int id,
            @RequestParam(defaultValue = "-10") double start,
            @RequestParam(defaultValue = "10") double end,
            @RequestParam(defaultValue = "200") int count) {
        try {
            Function function = functionService.getFunction(id);
            if (function == null) {
                return ResponseEntity.notFound().build();
            }
            double[][] points = function.getPoints(start, end, count);
            return ResponseEntity.ok(points);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

