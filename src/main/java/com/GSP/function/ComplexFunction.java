package com.GSP.function;

public class ComplexFunction extends Function {
    
    private MathExpression expression;
    private String expressionString;

    public ComplexFunction() {

    }

    public ComplexFunction(String expression) {
        this.expression = new MathExpression(expression);
        expressionString = this.expression.toString();
    }

    @Override
    public double evaluate(double x) {
        return expression.evaluate(x);
    }

    @Override
    public Function derivative() {
        MathExpression deriExpression = this.expression.derivative();
        ComplexFunction deri = new ComplexFunction();
        deri.expression = deriExpression;
        deri.expressionString = deriExpression.toString();
        return deri;
    }

    @Override
    public String toString() {
        return expressionString;
    }
}
