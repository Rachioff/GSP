package com.GSP.function;

/*
 * 单元运算符
 * 这是用于处理如 -x, sin(-x), -ln(x)等表达式的类
 */
public class UnaryOperatorNode extends ExpressionNode {
    private String operator;
    private ExpressionNode operand;
    
    public UnaryOperatorNode(String operator, ExpressionNode operand) {
        this.operator = operator;
        this.operand = operand;
    }
    
    @Override
    public double evaluate(double x) {
        double val = operand.evaluate(x);
        return switch(operator) {
            case "-" -> -val;
            case "+" -> val;
            default -> throw new UnsupportedOperationException("Unknown unary operator: " + operator);
        };
    }
    
    @Override
    public ExpressionNode derivative() {
        return switch(operator) {
            case "-" -> new UnaryOperatorNode("-", operand.derivative());
            case "+" -> operand.derivative();
            default -> throw new UnsupportedOperationException("Unknown unary operator: " + operator);
        };
    }
    
    @Override
    public String toString() {
        return operator + operand.toString();
    }
}
