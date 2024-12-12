package com.GSP.container;

import java.util.HashMap;

import com.GSP.function.Function;
import com.GSP.factory.FunctionFactory;

public class FunctionContainer {
    private static final FunctionContainer container = new FunctionContainer();
    private HashMap<Integer, Function> functions = new HashMap<Integer, Function>();

    // 单例模式
    private FunctionContainer() {}

    public static FunctionContainer getContainer() {
        return container;
    }

    public HashMap<Integer, Function> getFunctions() {
        return functions;
    }

    public void addFunction(Function f) {
        functions.put(f.getId(), f);
    }

    public void createFunction(String expression) {
        FunctionFactory instance = FunctionFactory.getInstance();
        Function newFunction = instance.createComplexFunction(expression);

        for (Integer i : functions.keySet()) {
            System.out.println(functions.get(i).toString());
        }
        addFunction(newFunction);
    }

    public Function getFunction(int id) {
        return container.functions.get(id);
    }

    public void deleteFunction(int id) throws IndexOutOfBoundsException {
        functions.remove(id);
    }
}