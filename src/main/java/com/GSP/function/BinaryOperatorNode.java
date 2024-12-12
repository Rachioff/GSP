package com.GSP.function;

/*
 * 二元运算符节点
 */
public class BinaryOperatorNode extends ExpressionNode {
    private String operator;
    private ExpressionNode left, right;

    public BinaryOperatorNode(String operator, ExpressionNode left, ExpressionNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public double evaluate(double x) {
        double leftVal = left.evaluate(x);
        double rightVal = right.evaluate(x);

        return switch (operator) {
            case "+" -> leftVal + rightVal;
            case "-" -> leftVal - rightVal;
            case "*" -> leftVal * rightVal;
            case "/" -> {
                if (Math.abs(rightVal) < 1e-10) {
                    throw new ArithmeticException("Division by Zero");
                }
                yield leftVal / rightVal;
            }
            case "^" -> Math.pow(leftVal, rightVal);
            default -> throw new UnsupportedOperationException("Unknown operator: " + operator);
        };
    }

    @Override
    public ExpressionNode derivative() {
        return switch(operator) {
            case "+" -> new BinaryOperatorNode("+", left.derivative(), right.derivative());

            case "-" -> new BinaryOperatorNode("-", left.derivative(), right.derivative());

            case "*" -> new BinaryOperatorNode("+",
                new BinaryOperatorNode("*", left.derivative(), right),
                new BinaryOperatorNode("*", left, right.derivative()));

            case "/" -> new BinaryOperatorNode("/",
                    new BinaryOperatorNode("-",
                        new BinaryOperatorNode("*", left.derivative(), right),
                new BinaryOperatorNode("*", left, right.derivative())),
                new BinaryOperatorNode("^", right, new ConstantNode(2)));
                
            case "^" -> new BinaryOperatorNode("*",
                new BinaryOperatorNode("^", left, right),
                new BinaryOperatorNode("+",
                    new BinaryOperatorNode("*", right.derivative(), new FunctionNode("ln", left)),
                    new BinaryOperatorNode("*", right, new BinaryOperatorNode("/", left.derivative(), left))));
                    
            default -> throw new UnsupportedOperationException("Unknown operator: " + operator);
        };
    }

    @Override
    public String toString() {
        return "(" + left.toString() + operator + right.toString() + ")";
    }
}
