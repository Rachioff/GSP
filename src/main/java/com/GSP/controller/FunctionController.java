package com.GSP.controller;

import com.GSP.dto.FunctionRequest;
import com.GSP.dto.ErrorResponse;
import com.GSP.function.Function;
import com.GSP.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/functions")
@CrossOrigin(origins = "http://localhost:8080")
public class FunctionController {

    @Autowired
    private FunctionService functionService;

    @PostMapping
    public ResponseEntity<?> addFunction(@RequestBody FunctionRequest request) {
        try {
            functionService.addFunction(request.getExpression());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse("服务器内部错误: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<HashMap<Integer, String>> getFunctions() {
        try {
            HashMap<Integer, String> functions = functionService.getAllFunctions();
            return ResponseEntity.ok(functions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFunction(@PathVariable int id) {
        try {
            functionService.deleteFunction(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse("服务器内部错误"));
        }
    }

    // 函数求导
    @PostMapping("/{id}/derivative")
    public ResponseEntity<?> deriveFunction(@PathVariable int id) {
        try {
            functionService.deriveFunction(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse("计算导数时发生错误"));
        }
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<?> getFunctionPoints(
            @PathVariable int id,
            @RequestParam(defaultValue = "-10") double start,
            @RequestParam(defaultValue = "10") double end,
            @RequestParam(defaultValue = "200") int count) {
        try {
            Function function = functionService.getFunction(id);
            double[][] points = function.getPoints(start, end, count);
            return ResponseEntity.ok(points);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body(new ErrorResponse("计算函数点时发生错误"));
        }
    }
}