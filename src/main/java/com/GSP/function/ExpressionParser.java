package com.GSP.function;

public class ExpressionParser {
    private String expression;
    private int position;
    
    public ExpressionParser(String expression) {
        this.expression = expression.replaceAll("\\s+", "");
        this.position = 0;
    }
    
    public ExpressionNode parse() {
        ExpressionNode node = parseAddSubtract();
        if (position < expression.length()) {
            throw new IllegalArgumentException("在位置 " + position + " 发现非法字符: " + expression.charAt(position));
        }
        return node;
    }
    
    private ExpressionNode parseAddSubtract() {
        // 处理表达式开头的一元运算符
        String unaryOp = "";
        if (position < expression.length() && (expression.charAt(position) == '+' || expression.charAt(position) == '-')){
            unaryOp = String.valueOf(expression.charAt(position));
            position++;
        }

        ExpressionNode left = parseMultiplyDivide();

        if (!unaryOp.isEmpty()) {
            left = new UnaryOperatorNode(unaryOp, left);
        }

        while (position < expression.length()) {
            char op = expression.charAt(position);
            if (op != '+' && op != '-') break;
            position++;

            // 检查是否是连续的运算符，类似于(1 -- 2)
            String nextUnaryOp = "";
            if (position < expression.length() && (expression.charAt(position) == '+' || expression.charAt(position) == '-')) {
                nextUnaryOp = String.valueOf(expression.charAt(position));
                position++;
            }

            ExpressionNode right = parseMultiplyDivide();

            if (!nextUnaryOp.isEmpty()) {
                right = new UnaryOperatorNode(nextUnaryOp, right);
            }

            left = new BinaryOperatorNode(String.valueOf(op), left, right);
        }
        return left;
    }
    
    private ExpressionNode parseMultiplyDivide() {
        ExpressionNode left = parsePower();
        while (position < expression.length()) {
            char op = expression.charAt(position);
            if (op != '*' && op != '/') break;
            position++;

            String unaryOp = "";
            if (position < expression.length() && (expression.charAt(position) == '+' || expression.charAt(position) == '-')) {
                unaryOp = String.valueOf(expression.charAt(position));
                position++;
            }

            ExpressionNode right = parsePower();

            if (!unaryOp.isEmpty()) {
                right = new UnaryOperatorNode(unaryOp, right);
            }
            left = new BinaryOperatorNode(String.valueOf(op), left, right);
        }
        return left;
    }
    
    private ExpressionNode parsePower() {
        ExpressionNode left = parsePrimary();
        while (position < expression.length()) {
            char op = expression.charAt(position);
            if (op != '^') break;
            position++;

            String unaryOp = "";
            if (position < expression.length() && (expression.charAt(position) == '+' || expression.charAt(position) == '-')) {
                unaryOp = String.valueOf(expression.charAt(position));
                position++;
            }
            
            ExpressionNode right = parsePrimary();

            if (!unaryOp.isEmpty()) {
                right = new UnaryOperatorNode(unaryOp, right);
            }

            left = new BinaryOperatorNode(String.valueOf(op), left, right);
        }
        return left;
    }
    
    private ExpressionNode parsePrimary() {
        if (position >= expression.length()) {
            throw new IllegalArgumentException("表达式结尾错误");
        }
        
        char c = expression.charAt(position);

        // 处理一元运算符
        if (c == '+' || c == '-') {
            position++;
            ExpressionNode operand = parsePrimary();
            return new UnaryOperatorNode(String.valueOf(c), operand);
        }
        
        // 处理数字
        if (Character.isDigit(c) || c == '.') {
            StringBuilder number = new StringBuilder();
            while (position < expression.length() && 
                   (Character.isDigit(expression.charAt(position)) || 
                    expression.charAt(position) == '.')) {
                number.append(expression.charAt(position++));
            }
            return new ConstantNode(Double.parseDouble(number.toString()));
        }
        
        // 处理变量
        if (c == 'x') {
            position++;
            return new VariableNode();
        }
        
        // 处理括号
        if (c == '(') {
            position++;
            ExpressionNode node = parseAddSubtract();
            if (position >= expression.length() || expression.charAt(position) != ')') {
                throw new IllegalArgumentException("缺少右括号");
            }
            position++;
            return node;
        }
        
        // 处理函数
        if (Character.isLetter(c)) {
            StringBuilder funcName = new StringBuilder();
            while (position < expression.length() && Character.isLetter(expression.charAt(position))) {
                funcName.append(expression.charAt(position++));
            }
            
            if (position < expression.length() && expression.charAt(position) == '(') {
                position++; // 跳过左括号
                
                if ("log".equals(funcName.toString())) {
                    // 处理 log(base,arg) 格式
                    StringBuilder baseStr = new StringBuilder();
                    while (position < expression.length() && Character.isDigit(expression.charAt(position))) {
                        baseStr.append(expression.charAt(position++));
                    }
                    if (position < expression.length() && expression.charAt(position) == ',') {
                        position++; // 跳过逗号
                        ExpressionNode arg = parseAddSubtract();
                        if (position < expression.length() && expression.charAt(position) == ')') {
                            position++; // 跳过右括号
                            return new FunctionNode("log", arg, Integer.parseInt(baseStr.toString()));
                        }
                    }
                    throw new IllegalArgumentException("函数格式错误: " + funcName);
                } else {
                    // 处理其他函数
                    ExpressionNode arg = parseAddSubtract();
                    if (position < expression.length() && expression.charAt(position) == ')') {
                        position++; // 跳过右括号
                        return new FunctionNode(funcName.toString(), arg);
                    }
                    throw new IllegalArgumentException("Invalid function format");
                }
            }
        }
        
        throw new IllegalArgumentException("在位置 " + position + " 发现非法字符: " + c);
    }
}
