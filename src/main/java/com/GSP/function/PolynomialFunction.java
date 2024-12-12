package com.GSP.function;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PolynomialFunction extends Function {
    // 存储多项式的系数，index表示次数，value表示系数
    private double[] coefficients;
    
    /**
     * 构造多项式函数
     * @param coefficients 系数数组，从低次项到高次项
     * Example: 对于函数 3x^2 + 2x + 1, coefficients = [1, 2, 3]
     */
    public PolynomialFunction(double[] coefficients) {
        this.type = "polynomial";
        if (coefficients == null || coefficients.length == 0) {
            throw new IllegalArgumentException("Coefficients array cannot be null or empty");
        }
        
        // 移除高次项中的0系数
        int lastNonZero = coefficients.length - 1;
        while (lastNonZero > 0 && Math.abs(coefficients[lastNonZero]) < 1e-10) {
            lastNonZero--;
        }
        
        // 创建新的系数数组，只包含有效系数
        this.coefficients = new double[lastNonZero + 1];
        System.arraycopy(coefficients, 0, this.coefficients, 0, lastNonZero + 1);
    }
    
    /**
     * 构造多项式函数
     * @param expression 多项式表达式，例如 "x^2+2x+1" 或 "-x^3+2x^2-4x+5"
     */
    public PolynomialFunction(String expression) {
        this.type = "polynomial";
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        // 预处理表达式
        expression = preProcessExpression(expression);
        
        // 找出最高次数
        int maxDegree = findMaxDegree(expression);
        double[] coeffs = new double[maxDegree + 1];
        
        // 解析每一项
        Pattern termPattern = Pattern.compile("([+-]?\\s*(?:\\d*\\.?\\d*)?)?x\\^?(\\d*)|([+-]?\\s*\\d+\\.?\\d*)");
        Matcher matcher = termPattern.matcher(expression);
        
        while (matcher.find()) {
            // 系数
            String coeffStr = matcher.group(1);
            // 指数
            String powerStr = matcher.group(2);
            // 常数项
            String constStr = matcher.group(3);
            
            if (constStr != null) {
                // 处理常数项
                coeffs[0] += parseCoefficient(constStr);
            } else {
                // 处理含x的项
                int power = powerStr.isEmpty() ? 1 : Integer.parseInt(powerStr);
                double coeff = parseCoefficient(coeffStr);
                coeffs[power] += coeff;
            }
        }
        
        // 使用原有构造函数创建多项式
        this.coefficients = coeffs;
        this.setExpression(this.toString());
        System.out.println(this.getExpression());
    }
    
    /**
     * 预处理表达式，标准化格式
     */
    private String preProcessExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        
        // 之后可能会用到，不过现在没bug就线不加了
        // if (!expression.startsWith("+") && !expression.startsWith("-")) {
        //     expression = "+" + expression;
        // }
        
        expression = expression.replaceAll("\\+\\+", "+");
        expression = expression.replaceAll("\\+-", "-");
        expression = expression.replaceAll("-\\+", "-");
        expression = expression.replaceAll("--", "+");
        
        return expression;
    }
    
    /**
     * 解析系数
     */
    private double parseCoefficient(String coeffStr) {
        if (coeffStr == null || coeffStr.trim().isEmpty() || coeffStr.equals("+")) {
            return 1.0;
        }
        if (coeffStr.equals("-")) {
            return -1.0;
        }
        return Double.parseDouble(coeffStr);
    }
    
    /**
     * 找出多项式的最高次数
     */
    private int findMaxDegree(String expression) {
        Pattern degreePattern = Pattern.compile("x\\^?(\\d*)");
        Matcher matcher = degreePattern.matcher(expression);
        int maxDegree = 0;
        
        while (matcher.find()) {
            String degreeStr = matcher.group(1);
            int degree = degreeStr.isEmpty() ? 1 : Integer.parseInt(degreeStr);
            maxDegree = Math.max(maxDegree, degree);
        }
        
        return maxDegree;
    }
    
    /**
     * 获取多项式的次数
     * @return 多项式次数
     */
    public int getDegree() {
        return coefficients.length - 1;
    }
    
    /**
     * 获取所有系数
     * @return 系数数组的副本
     */
    public double[] getCoefficients() {
        return coefficients.clone();
    }
    
    /**
     * 获取指定次数的系数
     * @param power 次数
     * @return 对应的系数
     */
    public double getCoefficient(int power) {
        if (power < 0 || power >= coefficients.length) {
            return 0.0;
        }
        return coefficients[power];
    }
    
    // toString方法还原表达式
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        
        for (int i = coefficients.length - 1; i >= 0; i--) {
            if (Math.abs(coefficients[i]) < 1e-10) continue;
            
            if (coefficients[i] > 0 && !first) {
                sb.append("+");
            }
            
            if (Math.abs(coefficients[i] - 1.0) > 1e-10 || i == 0) {
                sb.append(coefficients[i]);
            }
            
            if (i > 0) {
                sb.append("x");
                if (i > 1) {
                    sb.append("^").append(i);
                }
            }
            
            first = false;
        }
        
        return sb.length() == 0 ? "0" : sb.toString();
    }
    
    @Override
    public double evaluate(double x) {
        
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, i);
        }
        return result;
    }
    
    @Override
    public Function derivative() {
        if (coefficients.length <= 1) {
            return new PolynomialFunction(new double[]{0}); // 常数的导数为0
        }
        
        double[] derivCoeffs = new double[coefficients.length - 1];
        for (int i = 1; i < coefficients.length; i++) {
            derivCoeffs[i - 1] = coefficients[i] * i;
        }
        
        return new PolynomialFunction(derivCoeffs);
    }



    
    // 测试用的不用管
    public static void main(String[] args) {
        PolynomialFunction f = new PolynomialFunction("3x^4 - x^3 + 2x^2 + 1");
        // for (int i = 0; i < f.coefficients.length; i++) {
        //     System.out.println(f.coefficients[i]);
        // }

        Function der = f.derivative();
        System.out.println(der.toString());

        // System.out.println(f.getExpression());
        // System.out.println(f.getId());
    }
}
