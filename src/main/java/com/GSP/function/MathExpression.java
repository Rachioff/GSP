package com.GSP.function;

/*
 * 用于存放表达式
 */
public class MathExpression {
    private ExpressionNode root;
    
    public MathExpression(String expression) {
        ExpressionParser parser = new ExpressionParser(expression);
        
        // 不能解析空字符串，但是求导的时候会传入，所以也不能直接异常处理。。。
        if (!expression.equals("")) {
            this.root = parser.parse();  
            this.root = ExpressionSimplifier.simplify(this.root);
        }
    }
    
    public double evaluate(double x) {
        return root.evaluate(x);
    }
    
    public MathExpression derivative() {
        // 创建一个新的MathExpression对象，但不通过解析而是直接设置其根节点
        MathExpression result = new MathExpression("");
        result.root = root.derivative();
        result.root = ExpressionSimplifier.simplify(result.root);
        return result;
    }
    
    @Override
    public String toString() {
        return root.toString();
    }

    /*
     * 用于测试
     */

    public static void main(String[] args) {
        try {
            MathExpression expr = new MathExpression("x^2 - x + 1");
            MathExpression der = expr.derivative();
            System.out.println("derive of " + expr.toString() + " is " + der.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
