package com.GSP.service;

import org.springframework.stereotype.Service;
import com.GSP.factory.FunctionFactory;
import com.GSP.function.Function;

@Service
public class ExpressionValidationService {
    
    /**
     * 验证表达式是否合法
     * 通过尝试创建函数对象的方式进行验证，复用现有的解析逻辑
     * @param expression 需要验证的表达式
     * @throws IllegalArgumentException 当表达式不合法时抛出，包含具体的错误信息
     */
    public void validate(String expression) throws IllegalArgumentException {
        // 基本空值检查
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("表达式不能为空");
        }

        try {
            // 利用现有的函数工厂创建函数，如果创建成功则说明表达式语法合法
            FunctionFactory factory = FunctionFactory.getInstance();
            Function testFunction = factory.createComplexFunction(expression);
            
            // 对函数进行基本的计算测试，检查是否可以正常求值
            testBasicCalculation(testFunction);
            
        } catch (IllegalArgumentException e) {
            // 直接抛出原始的解析错误信息
            throw e;
        } catch (Exception e) {
            // 包装其他类型的异常
            throw new IllegalArgumentException("表达式验证失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试函数的基本计算
     * 在一些关键点进行测试，验证函数的基本计算是否正常
     * @param function 要测试的函数
     * @throws IllegalArgumentException 当函数在测试点无法正常计算时抛出
     */
    private void testBasicCalculation(Function function) {
        try {
            // 测试一些基本点的计算
            // 选择 0 作为测试点因为它是一个特殊值
            function.evaluate(0);
            // 选择 1 因为它在很多函数中是特殊点（比如指数、对数）
            function.evaluate(1);
            // 选择负数测试点以确保函数在负区间也能正常工作
            function.evaluate(-1);
            // 选择一个小数点测试非整数情况
            function.evaluate(0.5);
            
        } catch (ArithmeticException e) {
            // 忽略除零等数学运算错误，因为这些错误可能是正常的
            // 例如 1/x 在 x=0 时的情况
        } catch (Exception e) {
            throw new IllegalArgumentException("函数在测试点无法正常计算: " + e.getMessage());
        }
    }
}