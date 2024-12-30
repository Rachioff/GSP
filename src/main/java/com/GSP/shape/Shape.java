package com.GSP.shape;
public abstract class Shape {
    private static int idCounter = 1; // ID计数器
    private final int id; // 每个Shape的唯一ID

    protected  String expression; // 存储图形方程的字符串
    protected String type; // 图形的类型，例如 "Ellipse", "Hyperbola"

    // 构造函数，初始化唯一ID
    public Shape() {
        this.id = idCounter++;
    }

    // 获取Shape的唯一ID
    public int getId() {
        return id;
    }

    // 获取图形的表达式
    public String getExpression() {
        return expression;
    }

    // 设置图形的表达式
    protected void setExpression(String expression) {
        this.expression = expression;
    }

    // 获取图形的类型
    public String getType() {
        return type;
    }

    /**
     * 检查给定点(x, y)是否在图形上
     * 子类需要根据具体图形的特性来实现此方法
     * @param x 给定的x坐标
     * @param y 给定的y坐标
     * @return 如果点在图形上返回true，否则返回false
     */
    public abstract boolean isPointOnShape(double x, double y);

    /**
     * 计算给定x坐标下的y坐标或其他相关坐标
     * 子类需要实现具体的计算方法
     * @param x 给定的x坐标
     * @return 对应的y坐标或其他相关坐标
     */
    public abstract double[] calculateCoordinates(double x);

    /**
     * 返回形状的标准化表达式
     * 每个子类会根据需要实现具体的标准化方法
     * @return 标准化后的形状表达式
     */
    public abstract String getStandardizedExpression();

        /**
     * 生成一个区间内均匀分布的点的数组，计算每个点的坐标
     * @param start 起始点
     * @param end 结束点
     * @param points 需要生成的点的数量
     * @return 返回二维数组，每个元素包含一个 (x, y1, y2) 坐标
     */
    public abstract double[][] getPoints(double start, double end, int points);
    public abstract double[][] getParametricPoints(int points);

    /**
     * 解析并标准化图形表达式
     * 每个子类会根据需要实现具体的解析方法
     * @param expression 图形的表达式
     * @return 标准化后的参数数组 [h, k, a, b]
     */
    public static double[] parseExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        //参数数组[h, k, a, b]
        double[] params = new double[4];
        if (!expression.matches("^\\(x.*\\)\\^2/\\d+\\^2[\\+\\-]\\(y.*\\)\\^2/\\d+\\^2=1$")) {
            throw new IllegalArgumentException("Invalid expression format: " + expression);
        }
        int xStart = expression.indexOf("(x");
        int xEnd = expression.indexOf(")", xStart);
        int yStart = expression.indexOf("y");
        int yEnd = expression.indexOf(")", yStart);

        String xPart = expression.substring(xStart + 2, xEnd);  // 提取 x 部分
        String yPart = expression.substring(yStart + 2, yEnd);  // 提取 y 部分    
        // 提取 h 和 k
        params[0] = parseNumber(xPart); // h
        params[1] = parseNumber(yPart); // k
        // 查找 a 和 b
        int xDivStart = expression.indexOf("^2/", xEnd)+3; 
        int yDivStart = expression.indexOf("^2/", yEnd)+3;
        // 提取 a 和 b
        params[2] = parseValue(expression, xDivStart);  // a
        params[3] = parseValue(expression, yDivStart);  // b
        return params;
    }

    // 解析数字（如 -3 或 3）
    private static double parseNumber(String part) {
        if (part.isEmpty() || part.equals("+")) {
            return 0;
        } else if (part.equals("-")) {
            return 0;
        } else {
            return Double.parseDouble(part);
        }
    }

    // 解析值（a 或 b）
    private static double parseValue(String expression, int start) {
        int end = expression.indexOf("^2", start);
        String valueStr = expression.substring(start, end);
        return Double.parseDouble(valueStr);
    }


    public static void main(String[] args) {
        // 测试样例
        String expression = "(x+0)^2/4^2+(y+0)^2/3^2=1";
        try {
            double[] params = parseExpression(expression);
            System.out.println("h: " + params[0]);
            System.out.println("k: " + params[1]);
            System.out.println("a: " + params[2]);
            System.out.println("b: " + params[3]);
            Shape e=new Hyperbola(params[0],params[1],params[2],params[3]);
            System.out.println(e.getExpression());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}


