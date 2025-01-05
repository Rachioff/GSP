package com.GSP.function;

public class ExpressionSimplifier {
    private static final double EPSILON = 1e-10;  // 用于浮点数比较的误差阈值
    
    public static ExpressionNode simplify(ExpressionNode node) {
        if (node == null) return null;
        
        // 首先递归简化所有子节点
        ExpressionNode simplified = simplifyNode(node);
        
        // 应用代数简化规则
        simplified = applyAlgebraicRules(simplified);
        
        return simplified;
    }
    
    private static ExpressionNode simplifyNode(ExpressionNode node) {
        if (node instanceof BinaryOperatorNode) {
            return simplifyBinaryNode((BinaryOperatorNode) node);
        } else if (node instanceof UnaryOperatorNode) {
            return simplifyUnaryNode((UnaryOperatorNode) node);
        } else if (node instanceof FunctionNode) {
            return simplifyFunctionNode((FunctionNode) node);
        }
        return node;
    }
    
    private static ExpressionNode simplifyBinaryNode(BinaryOperatorNode node) {
        ExpressionNode left = simplify(node.getLeft());
        ExpressionNode right = simplify(node.getRight());
        
        // 常数折叠
        if (left instanceof ConstantNode && right instanceof ConstantNode) {
            try {
                double result = node.evaluate(0);
                return new ConstantNode(result);
            } catch (Exception e) {
                // 处理可能的计算错误，如除以零
                return new BinaryOperatorNode(node.getOperator(), left, right);
            }
        }
        
        // 基于运算符的特殊规则
        switch (node.getOperator()) {
            case "+":
                if (isZero(left)) return right;
                if (isZero(right)) return left;
                // a + (-b) = a - b
                if (right instanceof UnaryOperatorNode && 
                    ((UnaryOperatorNode) right).getOperator().equals("-")) {
                    return new BinaryOperatorNode("-", left, 
                        ((UnaryOperatorNode) right).getOperand());
                }
                break;
                
            case "-":
                if (isZero(right)) return left;
                if (isZero(left)) return new UnaryOperatorNode("-", right);
                // 检查是否是减去一个负数：a - (-b) = a + b
                if (right instanceof UnaryOperatorNode && 
                    ((UnaryOperatorNode) right).getOperator().equals("-")) {
                    return new BinaryOperatorNode("+", left, 
                        ((UnaryOperatorNode) right).getOperand());
                }
                break;
                
            case "*":
                if (isZero(left) || isZero(right)) return new ConstantNode(0);
                if (isOne(left)) return right;
                if (isOne(right)) return left;
                if (isNegativeOne(left)) return new UnaryOperatorNode("-", right);
                if (isNegativeOne(right)) return new UnaryOperatorNode("-", left);
                break;
                
            case "/":
                if (isZero(left)) return new ConstantNode(0);
                if (isOne(right)) return left;
                if (nodesAreEqual(left, right)) return new ConstantNode(1);
                break;
                
            case "^":
                if (isZero(right)) return new ConstantNode(1);
                if (isOne(right)) return left;
                if (isZero(left)) return new ConstantNode(0);
                if (isOne(left)) return new ConstantNode(1);
                // x^(-n) = 1/(x^n)
                if (right instanceof UnaryOperatorNode && 
                    ((UnaryOperatorNode) right).getOperator().equals("-")) {
                    return new BinaryOperatorNode("/", 
                        new ConstantNode(1),
                        new BinaryOperatorNode("^", left, 
                            ((UnaryOperatorNode) right).getOperand()));
                }
                break;
        }
        
        return new BinaryOperatorNode(node.getOperator(), left, right);
    }
    
    private static ExpressionNode simplifyUnaryNode(UnaryOperatorNode node) {
        ExpressionNode operand = simplify(node.getOperand());
        
        if (operand instanceof ConstantNode) {
            double result = node.evaluate(0);
            return new ConstantNode(result);
        }
        
        // --x = x
        if (node.getOperator().equals("-") && 
            operand instanceof UnaryOperatorNode && 
            ((UnaryOperatorNode) operand).getOperator().equals("-")) {
            return ((UnaryOperatorNode) operand).getOperand();
        }
        
        // +(+x) = x
        if (node.getOperator().equals("+")) {
            return operand;
        }
        
        return new UnaryOperatorNode(node.getOperator(), operand);
    }
    
    private static ExpressionNode simplifyFunctionNode(FunctionNode node) {
        ExpressionNode argument = simplify(node.getArgument());
        
        // 如果参数是常数，计算函数值
        if (argument instanceof ConstantNode) {
            try {
                double result = node.evaluate(0);
                return new ConstantNode(result);
            } catch (Exception e) {
                // 处理可能的计算错误
                return new FunctionNode(node.getFunctionName(), argument, node.getBase());
            }
        }
        
        // 特殊函数规则
        switch (node.getFunctionName()) {
            case "ln":
                if (argument instanceof FunctionNode) {
                    FunctionNode argFunc = (FunctionNode) argument;
                    // ln(e^x) = x
                    if (argFunc.getFunctionName().equals("exp")) {
                        return argFunc.getArgument();
                    }
                }
                break;
                
            case "log":
                // log_b(b^x) = x
                if (argument instanceof BinaryOperatorNode) {
                    BinaryOperatorNode argBin = (BinaryOperatorNode) argument;
                    if (argBin.getOperator().equals("^") && 
                        argBin.getLeft() instanceof ConstantNode &&
                        Math.abs(((ConstantNode) argBin.getLeft()).getValue() - node.getBase()) < EPSILON) {
                        return argBin.getRight();
                    }
                }
                break;
        }
        
        return new FunctionNode(node.getFunctionName(), argument, node.getBase());
    }
    
    private static ExpressionNode applyAlgebraicRules(ExpressionNode node) {
        if (!(node instanceof BinaryOperatorNode)) {
            return node;
        }
        
        BinaryOperatorNode bNode = (BinaryOperatorNode) node;
        
        // 合并同类项
        if (bNode.getOperator().equals("+") || bNode.getOperator().equals("-")) {
            return combineTerms(bNode);
        }
        
        return node;
    }
    
    private static ExpressionNode combineTerms(BinaryOperatorNode node) {
        // 这里可以添加更复杂的合并同类项逻辑
        // 例如：2x + 3x = 5x, x^2 + x^2 = 2x^2 等
        return node;
    }
    
    // 辅助函数
    private static boolean isZero(ExpressionNode node) {
        return node instanceof ConstantNode && 
               Math.abs(((ConstantNode) node).getValue()) < EPSILON;
    }
    
    private static boolean isOne(ExpressionNode node) {
        return node instanceof ConstantNode && 
               Math.abs(((ConstantNode) node).getValue() - 1) < EPSILON;
    }
    
    private static boolean isNegativeOne(ExpressionNode node) {
        return node instanceof ConstantNode && 
               Math.abs(((ConstantNode) node).getValue() + 1) < EPSILON;
    }
    
    private static boolean nodesAreEqual(ExpressionNode node1, ExpressionNode node2) {
        return node1.toString().equals(node2.toString());
    }
}


// 真的昏头了，比数分还难