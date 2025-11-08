package ru.blatfan.blatapi.utils;

import lombok.experimental.UtilityClass;

import java.util.*;

@SuppressWarnings("ALL")
public class MathEvaluator {
    private final String expression;
    private int pos;
    private char currentChar;
    private final Map<String, Double> variables;
    
    public MathEvaluator(String expression) {
        this.expression = expression.replaceAll("\\s", "");
        this.pos = 0;
        this.currentChar = !expression.isEmpty() ? expression.charAt(0) : '\0';
        this.variables = new HashMap<>();
        setVariable("PI", Math.PI);
    }
    
    public MathEvaluator setVariable(String name, double value) {
        variables.put(name, value);
        return this;
    }
    public double evaluate() {
        double result = parseExpression();
        if (pos < expression.length()) {
            throw new IllegalArgumentException("Unexpected symbols: " + expression.substring(pos));
        }
        return result;
    }
    
    private void nextChar() {
        pos++;
        currentChar = (pos < expression.length()) ? expression.charAt(pos) : '\0';
    }
    
    private boolean consume(char expected) {
        if (currentChar == expected) {
            nextChar();
            return true;
        }
        return false;
    }
    
    private double parseExpression() {
        double result = parseTerm();
        
        while (currentChar == '+' || currentChar == '-') {
            char op = currentChar;
            nextChar();
            double term = parseTerm();
            if (op == '+') {
                result += term;
            } else {
                result -= term;
            }
        }
        return result;
    }
    
    private double parseTerm() {
        double result = parseFactor();
        
        while (currentChar == '*' || currentChar == '/' || currentChar == '%') {
            char op = currentChar;
            nextChar();
            double factor = parseFactor();
            if (op == '*') {
                result *= factor;
            } else {
                if (factor == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                if(op == '/') result /= factor;
                else result %= factor;
            }
        }
        return result;
    }
    
    private double parseFactor() {
        if (consume('-')) return -parseFactor();
        if (consume('+')) return parseFactor();
        double result;
        
        if (consume('(')) {
            result = parseExpression();
            if (!consume(')')) throw new IllegalArgumentException("Expected ')'");
            return result;
        }
        if (Character.isLetter(currentChar)) {
            StringBuilder name = new StringBuilder();
            while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                name.append(currentChar);
                nextChar();
            }
            if (consume('(')) {
                double arg = parseExpression();
                if (!consume(')')) throw new IllegalArgumentException("Expected ')' after the function argument");
                return evaluateFunction(name.toString(), arg);
            } else {
                String varName = name.toString();
                if (!variables.containsKey(varName))
                    throw new IllegalArgumentException("An undefined variable: " + varName);
                return variables.get(varName);
            }
        }
        if (Character.isDigit(currentChar) || currentChar == '.') return parseNumber();
        throw new IllegalArgumentException("Unexpected symbols: " + currentChar);
    }
    
    private double parseNumber() {
        StringBuilder sb = new StringBuilder();
        
        while (Character.isDigit(currentChar) || currentChar == '.') {
            sb.append(currentChar);
            nextChar();
        }
        
        try {
            return Double.parseDouble(sb.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Incorrect number: " + sb);
        }
    }
    
    private double evaluateFunction(String funcName, double arg) {
        return switch (funcName) {
            case "sin"   -> Math.sin(arg);
            case "cos"   -> Math.cos(arg);
            case "tan"   -> Math.tan(arg);
            case "sqrt"  -> Math.sqrt(arg);
            case "abs"   -> Math.abs(arg);
            case "log"   -> Math.log(arg);
            case "exp"   -> Math.exp(arg);
            default      -> throw new IllegalArgumentException("Unknown function: " + funcName);
        };
    }
}