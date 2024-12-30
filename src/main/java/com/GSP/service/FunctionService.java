package com.GSP.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;

import com.GSP.container.FunctionContainer;
import com.GSP.function.Function;

@Service
public class FunctionService {
    
    private FunctionContainer container = FunctionContainer.getContainer();
    
    @Autowired
    private ExpressionValidationService validationService;
    
    /**
     * 添加新的函数表达式
     * @param expression 函数表达式字符串
     * @throws IllegalArgumentException 当表达式验证失败时抛出
     */
    public void addFunction(String expression) throws IllegalArgumentException {
        // 先进行表达式验证
        validationService.validate(expression);
        // 验证通过后创建函数
        container.createFunction(expression);
    }
    
    /**
     * 获取所有已添加的函数
     * @return 函数ID和表达式的映射
     */
    public HashMap<Integer, String> getAllFunctions() {
        HashMap<Integer, String> functions = new HashMap<>();
        HashMap<Integer, Function> containerFunctions = container.getFunctions();
        for (Integer id : containerFunctions.keySet()) {
            functions.put(id, containerFunctions.get(id).toString());
        }
        return functions;
    }

    /**
     * 删除函数
     * @param id 要被删除的函数id
     * @throws IllegalArgumentException 当函数ID不存在时抛出
     */
    public void deleteFunction(int id) throws IllegalArgumentException {
        if (!container.getFunctions().containsKey(id)) {
            throw new IllegalArgumentException("函数ID不存在: " + id);
        }
        container.deleteFunction(id);
    }

    /**
     * 获取函数
     * @param id 要获取的函数id
     * @return 对应的函数对象
     * @throws IllegalArgumentException 当函数ID不存在时抛出
     */
    public Function getFunction(int id) throws IllegalArgumentException {
        Function function = container.getFunction(id);
        if (function == null) {
            throw new IllegalArgumentException("函数ID不存在: " + id);
        }
        return function;
    }

    /**
     * 函数求导
     * @param id 需要求导的函数id
     * @throws IllegalArgumentException 当函数ID不存在时抛出
     */
    public void deriveFunction(int id) throws IllegalArgumentException {
        Function f = getFunction(id); // 这里会检查ID是否存在
        Function derivative = f.derivative();
        container.addFunction(derivative);
    }
}