package com.GSP.function;

public class ExpressionSimplifier {
    
    public static ExpressionNode simplify(ExpressionNode node) {
        if (node == null) return null;
        
        // 递归简化子节点
        if (node instanceof BinaryOperatorNode) {
            BinaryOperatorNode bNode = (BinaryOperatorNode) node;
            ExpressionNode left = simplify(((BinaryOperatorNode) node).getLeft());
            ExpressionNode right = simplify(((BinaryOperatorNode) node).getRight());
            
            // 常数折叠
            if (left instanceof ConstantNode && right instanceof ConstantNode) {
                double result = node.evaluate(0); // 常数表达式求值与x无关
                return new ConstantNode(result);
            }
            
            // 加法和乘法的特殊处理
            switch (bNode.getOperator()) {
                case "+":
                    // 0 + x = x
                    if (left instanceof ConstantNode && Math.abs(((ConstantNode) left).getValue()) < 1e-10) {
                        return right;
                    }
                    // x + 0 = x
                    if (right instanceof ConstantNode && Math.abs(((ConstantNode) right).getValue()) < 1e-10) {
                        return left;
                    }
                    break;
                    
                case "*":
                    // 1 * x = x
                    if (left instanceof ConstantNode && Math.abs(((ConstantNode) left).getValue() - 1) < 1e-10) {
                        return right;
                    }
                    // x * 1 = x
                    if (right instanceof ConstantNode && Math.abs(((ConstantNode) right).getValue() - 1) < 1e-10) {
                        return left;
                    }
                    // 0 * x = 0
                    if ((left instanceof ConstantNode && Math.abs(((ConstantNode) left).getValue()) < 1e-10) ||
                        (right instanceof ConstantNode && Math.abs(((ConstantNode) right).getValue()) < 1e-10)) {
                        return new ConstantNode(0);
                    }
                    break;
                    
                case "^":
                    // x^0 = 1
                    if (right instanceof ConstantNode && Math.abs(((ConstantNode) right).getValue()) < 1e-10) {
                        return new ConstantNode(1);
                    }
                    // x^1 = x
                    if (right instanceof ConstantNode && Math.abs(((ConstantNode) right).getValue() - 1) < 1e-10) {
                        return left;
                    }
                    break;
            }
            
            return new BinaryOperatorNode(bNode.getOperator(), left, right);
        }
        
        // 函数节点简化
        if (node instanceof FunctionNode) {
            FunctionNode fNode = (FunctionNode) node;
            ExpressionNode simplifiedArg = simplify(fNode.getArgument());
            
            // 如果参数是常数，直接计算函数值
            if (simplifiedArg instanceof ConstantNode) {
                double result = node.evaluate(0);
                return new ConstantNode(result);
            }
            
            return new FunctionNode(fNode.getFunctionName(), simplifiedArg, fNode.getBase());
        }
        
        // 一元运算符节点简化
        if (node instanceof UnaryOperatorNode) {
            UnaryOperatorNode uNode = (UnaryOperatorNode) node;
            ExpressionNode simplifiedOperand = simplify(uNode.getOperand());
            
            // 如果操作数是常数，直接计算结果
            if (simplifiedOperand instanceof ConstantNode) {
                double result = node.evaluate(0);
                return new ConstantNode(result);
            }
            
            // --x = x
            if (uNode.getOperator().equals("-") && simplifiedOperand instanceof UnaryOperatorNode) {
                UnaryOperatorNode innerNode = (UnaryOperatorNode) simplifiedOperand;
                if (innerNode.getOperator().equals("-")) {
                    return simplify(innerNode.getOperand());
                }
            }
            
            return new UnaryOperatorNode(uNode.getOperator(), simplifiedOperand);
        }
        
        return node;
    }
}


// 真的昏头了，比数分还难