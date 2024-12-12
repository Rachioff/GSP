package com.GSP.function;

/*
 * 存放自变量节点
 */
public class VariableNode extends ExpressionNode {

    /*
     * 这东西看起来很蠢
     */
    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public ExpressionNode derivative() {
        return new ConstantNode(1);
    }

    @Override
    public String toString() {
        return "x";
    }
}
