package com.GSP.factory;

import com.GSP.function.ComplexFunction;
import com.GSP.function.Function;
import com.GSP.function.PolynomialFunction;

public class FunctionFactory {
    private static final FunctionFactory instance = new FunctionFactory();

    private FunctionFactory() {}

    // 返回工厂实例
    public static FunctionFactory getInstance() {
        return instance;
    }

    /**
     * 通过表达式创建函数
     * 目前支持的格式：
     * 1. 多项式：polynomial:1,2,3 (表示 3x^2 + 2x + 1)
     * 2. 复杂函数式
     *
     * @param expression 函数表达式
     * @return 对应的函数对象
     * @throws IllegalArgumentException 如果表达式格式不正确
     */
    public Function createPolynomialFunction(String expression) throws IllegalArgumentException {
        return new PolynomialFunction(expression);
    }

    public Function createComplexFunction(String expression) throws IllegalArgumentException {
        return new ComplexFunction(expression);
    }
}
