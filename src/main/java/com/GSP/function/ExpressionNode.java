package com.GSP.function;

/*
 * 万物归宗的表达式基本节点抽象类
 * 非常非常抽象，的类
 * 长得像一个接口
 */
public abstract class ExpressionNode {
    public abstract double evaluate(double x);
    public abstract ExpressionNode derivative();
    public abstract String toString();
}
