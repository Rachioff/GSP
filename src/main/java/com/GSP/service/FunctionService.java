package com.GSP.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;

import com.GSP.container.FunctionContainer;
import com.GSP.function.Function;

@Service
public class FunctionService {
    private FunctionContainer container = FunctionContainer.getContainer();
    
    /**
     * 添加新的函数表达式
     * @param expression 函数表达式字符串
     */
    public void addFunction(String expression) {
        container.createFunction(expression);
    }
    
    /**
     * 获取所有已添加的函数
     * @return 函数表达式列表
     */
    public HashMap<Integer, String> getAllFunctions() {
        HashMap<Integer, String> functions = new HashMap<>();
        HashMap<Integer, Function> containerFunction = container.getFunctions();
        for (Integer id : containerFunction.keySet()) {
            functions.put(id, containerFunction.get(id).toString());
        }
        return functions;
    }

    /**
     * 删除函数
     * @param id 要被删除的函数id
     */
    public void deleteFunction(int id) {
        container.deleteFunction(id);
    }

    /**
     * 获取函数
     * @param id 要被获取的函数id
     */
    public Function getFunction(int id) {
        return container.getFunctions().get(id);
    }

    /**
     * 函数求导
     * @param id 需要求导的函数id
     */
    public void deriveFunction(int id) {
        Function f = container.getFunction(id);
        Function derive = f.derivative();
        container.addFunction(derive);
    }
}
