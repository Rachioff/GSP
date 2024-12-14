package com.GSP.function;

/*
 * 函数节点
 * 用于存储幂指对三角函数
 */
public class FunctionNode extends ExpressionNode {
    private String functionName;
    private ExpressionNode argument;
    private Integer base;  // 对数的底数

    public FunctionNode(String functionName, ExpressionNode argument) {
        this(functionName, argument, null);
    }

    public FunctionNode(String functionName, ExpressionNode argument, Integer base) {
        this.functionName = functionName;
        this.argument = argument;
        this.base = base;
    }

    @Override
    public double evaluate(double x) {
        double arg = argument.evaluate(x);
        return switch(functionName) {
            case "sin" -> Math.sin(arg);
            case "cos" -> Math.cos(arg);
            case "tan" -> Math.tan(arg);
            case "ln" -> Math.log(arg);
            case "log" -> {
                if (base == null) throw new IllegalStateException("Base not specified for log");
                yield Math.log(arg) / Math.log(base);
            }
            default -> throw new UnsupportedOperationException("Unknown function: " + functionName);
        };
    }

    @Override
    public ExpressionNode derivative() {
        return switch(functionName) {
            case "sin" -> new BinaryOperatorNode("*", 
                new FunctionNode("cos", argument), 
                argument.derivative());

            case "cos" -> new BinaryOperatorNode("*",
                new BinaryOperatorNode("*", new ConstantNode(-1), new FunctionNode("sin", argument)),
                argument.derivative());

            case "tan" -> new BinaryOperatorNode("*",
                new BinaryOperatorNode("+", new ConstantNode(1), 
                new BinaryOperatorNode("^", new FunctionNode("tan", argument), new ConstantNode(2))),
                argument.derivative());

            case "ln" -> new BinaryOperatorNode("*",
                new BinaryOperatorNode("/", new ConstantNode(1), argument),
                argument.derivative());

            case "log" -> new BinaryOperatorNode("*",
                new BinaryOperatorNode("/", new ConstantNode(1), 
                new BinaryOperatorNode("*", argument, new FunctionNode("ln", new ConstantNode(base)))),
                argument.derivative());

            default -> throw new UnsupportedOperationException("Unknown function: " + functionName);
        };
    }

    @Override
    public String toString() {
        if ("log".equals(functionName)) {
            return "log(" + base + "," + argument.toString() + ")";
        }
        return functionName + "(" + argument.toString() + ")";
    }

    public ExpressionNode getArgument() {
        return argument;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Integer getBase() {
        return base;
    }
}
