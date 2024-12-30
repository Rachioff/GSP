package com.GSP.function;

/*
 * 本类用于存放常数节点
 */
public class ConstantNode extends ExpressionNode {
    private double value;
    private String specialConstant;

    public ConstantNode(double value) {
        this.value = value;
        this.specialConstant = null;
    }

    public ConstantNode(String constant) {
        this.specialConstant = constant;
        this.value = switch(constant) {
            case "pi" -> Math.PI;
            case "e" -> Math.E;
            default -> throw new IllegalArgumentException("Unknown constant: " + constant);
        };
    }

    @Override
    public double evaluate(double x) {
        return value;
    }

    @Override
    public ExpressionNode derivative() {
        return new ConstantNode(0);
    }

    @Override
    public String toString() {
        if (specialConstant != null) {
            return specialConstant;
        }
        // if (value == 0) return "";  // 如果是0返回空字符串
        return String.valueOf(value);
    }

    public double getValue() {
        return value;
    }
}
