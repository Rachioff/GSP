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
        String leftStr = left.toString();
        String rightStr = right.toString();
        
        // 优化括号的使用
        boolean needLeftParen = left instanceof BinaryOperatorNode &&
            (getPrecedence(((BinaryOperatorNode) left).operator) < getPrecedence(operator) ||
             (getPrecedence(((BinaryOperatorNode) left).operator) == getPrecedence(operator) && 
              !isAssociative(operator)));
              
        boolean needRightParen = right instanceof BinaryOperatorNode &&
            (getPrecedence(((BinaryOperatorNode) right).operator) < getPrecedence(operator) ||
             getPrecedence(((BinaryOperatorNode) right).operator) == getPrecedence(operator));
        
        return (needLeftParen ? "(" : "") + leftStr + (needLeftParen ? ")" : "") + 
               operator + 
               (needRightParen ? "(" : "") + rightStr + (needRightParen ? ")" : "");
    }

    // 获取运算符优先级
    private int getPrecedence(String op) {
        return switch(op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> 0;
        };
    }

    // 判断运算符是否满足结合律
    private boolean isAssociative(String op) {
        return switch(op) {
            case "+", "*" -> true;
            case "-", "/", "^" -> false;
            default -> false;
        };
    }

    public ExpressionNode getLeft() {
        return left;
    }
    
    public ExpressionNode getRight() {
        return right;
    }

    public String getOperator() {
        return operator;
    }
}
