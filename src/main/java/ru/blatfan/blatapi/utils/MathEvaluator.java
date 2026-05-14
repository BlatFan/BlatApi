package ru.blatfan.blatapi.utils;

import java.util.*;

@SuppressWarnings("ALL")
public class MathEvaluator {
    public interface ASTNode {
        double eval(MathContext ctx);
    }
    
    @FunctionalInterface
    public interface MathFunction {
        double apply(double... args);
    }
    
    public static class MathContext {
        public final Map<String, Double> variables = new HashMap<>();
        public final Map<String, MathFunction> functions = new HashMap<>();
        private static final Random RANDOM = new Random();
        
        public MathContext(boolean withDefSettings) {
            if (withDefSettings) defaultSettings();
        }
        
        public MathContext setVariable(String name, double value) {
            variables.put(name, value);
            return this;
        }
        
        public MathContext setVariable(String name, boolean value) {
            variables.put(name, value ? 1.0 : 0.0);
            return this;
        }
        
        public MathContext addFunction(String name, MathFunction function) {
            functions.put(name, function);
            return this;
        }
        
        private void defaultSettings() {
            addFunction("round", args -> Math.round(args[0]));
            addFunction("sin", args -> Math.sin(args[0]));
            addFunction("cos", args -> Math.cos(args[0]));
            addFunction("tan", args -> Math.tan(args[0]));
            addFunction("sqrt", args -> Math.sqrt(args[0]));
            addFunction("abs", args -> Math.abs(args[0]));
            addFunction("rand", args -> RANDOM.nextDouble());
            addFunction("randInt2", args -> RANDOM.nextInt((int) args[0], (int) args[1]));
            addFunction("min", args -> Math.min(args[0], args[1]));
            addFunction("max", args -> Math.max(args[0], args[1]));
            addFunction("clamp", args -> Math.max(args[1], Math.min(args[0], args[2])));
            addFunction("lerp", args -> args[0] + (args[1] - args[0]) * args[2]);
            addFunction("hypot", args -> Math.hypot(args[0], args[1]));
            addFunction("atan2", args -> Math.atan2(args[0], args[1]));
            
            setVariable("PI", Math.PI);
            setVariable("PI_HALF", Math.PI / 2);
            setVariable("E", Math.E);
            setVariable("true", 1.0);
            setVariable("false", 0.0);
        }
    }
    
    private record ConstantNode(double value) implements ASTNode {
        @Override public double eval(MathContext ctx) { return value; }
    }
    
    private record VariableNode(String name) implements ASTNode {
        @Override public double eval(MathContext ctx) {
            Double val = ctx.variables.get(name);
            if (val == null) throw new IllegalArgumentException("Undefined variable: " + name);
            return val;
        }
    }
    
    private record FunctionNode(String name, ASTNode[] args) implements ASTNode {
        @Override public double eval(MathContext ctx) {
            MathFunction func = ctx.functions.get(name);
            if (func == null) throw new IllegalArgumentException("Unknown function: " + name);
            double[] evaluatedArgs = new double[args.length];
            for (int i = 0; i < args.length; i++) evaluatedArgs[i] = args[i].eval(ctx);
            return func.apply(evaluatedArgs);
        }
    }
    
    private record UnaryNode(ASTNode child, char op) implements ASTNode {
        @Override public double eval(MathContext ctx) {
            double val = child.eval(ctx);
            if (op == '-') return -val;
            if (op == '!') return val == 0.0 ? 1.0 : 0.0;
            return val;
        }
    }
    
    private record BinaryNode(ASTNode left, ASTNode right, String op) implements ASTNode {
        @Override public double eval(MathContext ctx) {
            double l = left.eval(ctx);
            double r = right.eval(ctx);
            return switch (op) {
                case "+" -> l + r;
                case "-" -> l - r;
                case "*" -> l * r;
                case "/" -> l / r;
                case "%" -> l % r;
                case "^" -> Math.pow(l, r);
                case "==" -> l == r ? 1.0 : 0.0;
                case "!=" -> l != r ? 1.0 : 0.0;
                case "<" -> l < r ? 1.0 : 0.0;
                case "<=" -> l <= r ? 1.0 : 0.0;
                case ">" -> l > r ? 1.0 : 0.0;
                case ">=" -> l >= r ? 1.0 : 0.0;
                case "&&" -> (l != 0.0 && r != 0.0) ? 1.0 : 0.0;
                case "||" -> (l != 0.0 || r != 0.0) ? 1.0 : 0.0;
                default -> throw new IllegalStateException("Unknown operator: " + op);
            };
        }
    }
    
    private record TernaryNode(ASTNode cond, ASTNode trueBranch, ASTNode falseBranch) implements ASTNode {
        @Override public double eval(MathContext ctx) {
            return cond.eval(ctx) != 0.0 ? trueBranch.eval(ctx) : falseBranch.eval(ctx);
        }
    }
    
    private final String expression;
    private int pos = -1;
    private char currentChar;
    
    private MathEvaluator(String expression) {
        this.expression = expression;
    }
    
    public static ASTNode compile(String expression) {
        MathEvaluator parser = new MathEvaluator(expression);
        parser.nextChar();
        ASTNode root = parser.parseExpression();
        if (parser.pos < parser.expression.length()) {
            throw new IllegalArgumentException("Unexpected symbols at index " + parser.pos + ": " + parser.currentChar);
        }
        return root;
    }
    
    private void nextChar() {
        pos++;
        currentChar = (pos < expression.length()) ? expression.charAt(pos) : '\0';
        while (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r') {
            pos++;
            currentChar = (pos < expression.length()) ? expression.charAt(pos) : '\0';
        }
    }
    
    private boolean consume(char expected) {
        if (currentChar == expected) {
            nextChar();
            return true;
        }
        return false;
    }
    
    private ASTNode parseExpression() {
        ASTNode node = parseLogicalOr();
        if (consume('?')) {
            ASTNode trueBranch = parseExpression();
            if (!consume(':')) throw new IllegalArgumentException("Expected ':'");
            ASTNode falseBranch = parseExpression();
            return new TernaryNode(node, trueBranch, falseBranch);
        }
        return node;
    }
    
    private ASTNode parseLogicalOr() {
        ASTNode node = parseLogicalAnd();
        while (currentChar == '|') {
            nextChar(); if (consume('|')) node = new BinaryNode(node, parseLogicalAnd(), "||");
            else throw new IllegalArgumentException("Expected '||'");
        }
        return node;
    }
    
    private ASTNode parseLogicalAnd() {
        ASTNode node = parseEquality();
        while (currentChar == '&') {
            nextChar(); if (consume('&')) node = new BinaryNode(node, parseEquality(), "&&");
            else throw new IllegalArgumentException("Expected '&&'");
        }
        return node;
    }
    
    private ASTNode parseEquality() {
        ASTNode node = parseRelational();
        while (currentChar == '=' || currentChar == '!') {
            char op = currentChar;
            nextChar();
            if (consume('=')) node = new BinaryNode(node, parseRelational(), op + "=");
            else throw new IllegalArgumentException("Expected '==' or '!='");
        }
        return node;
    }
    
    private ASTNode parseRelational() {
        ASTNode node = parseAdditive();
        while (currentChar == '<' || currentChar == '>') {
            char op = currentChar;
            nextChar();
            if (consume('=')) node = new BinaryNode(node, parseAdditive(), op + "=");
            else node = new BinaryNode(node, parseAdditive(), String.valueOf(op));
        }
        return node;
    }
    
    private ASTNode parseAdditive() {
        ASTNode node = parseTerm();
        while (currentChar == '+' || currentChar == '-') {
            char op = currentChar;
            nextChar();
            node = new BinaryNode(node, parseTerm(), String.valueOf(op));
        }
        return node;
    }
    
    private ASTNode parseTerm() {
        ASTNode node = parsePower();
        while (currentChar == '*' || currentChar == '/' || currentChar == '%') {
            char op = currentChar;
            nextChar();
            node = new BinaryNode(node, parsePower(), String.valueOf(op));
        }
        return node;
    }
    
    private ASTNode parsePower() {
        ASTNode node = parseFactor();
        while (consume('^'))
            node = new BinaryNode(node, parseFactor(), "^");
        return node;
    }
    
    private ASTNode parseFactor() {
        if (consume('-')) return new UnaryNode(parseFactor(), '-');
        if (consume('+')) return parseFactor();
        if (consume('!')) return new UnaryNode(parseFactor(), '!');
        
        if (consume('(')) {
            ASTNode node = parseExpression();
            if (!consume(')')) throw new IllegalArgumentException("Expected ')'");
            return node;
        }
        
        if (Character.isLetter(currentChar)) {
            StringBuilder name = new StringBuilder();
            while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                name.append(currentChar);
                nextChar();
            }
            
            if (consume('(')) {
                List<ASTNode> args = new ArrayList<>();
                if (!consume(')')) {
                    do { args.add(parseExpression()); } while (consume(','));
                    if (!consume(')')) throw new IllegalArgumentException("Expected ')' after args");
                }
                return new FunctionNode(name.toString(), args.toArray(new ASTNode[0]));
            } else {
                return new VariableNode(name.toString());
            }
        }
        
        if (Character.isDigit(currentChar) || currentChar == '.') {
            StringBuilder sb = new StringBuilder();
            while (Character.isDigit(currentChar) || currentChar == '.') {
                sb.append(currentChar);
                nextChar();
            }
            return new ConstantNode(Double.parseDouble(sb.toString()));
        }
        
        throw new IllegalArgumentException("Unexpected symbol: " + currentChar);
    }
}