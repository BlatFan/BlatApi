package ru.blatfan.blatapi.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("ALL")
public class MathEvaluator {
    private final String expression;
    private int pos;
    private char currentChar;
    private final Map<String, Double> variables = new HashMap<>();;
    private final Map<String, Function<Double, Number>> functions = new HashMap<>();
    
    public MathEvaluator(String expression) {
        this(expression, true);
    }
    
    public MathEvaluator(String expression, boolean withDefSettings){
        this.expression = expression.replaceAll("\\s", "");
        this.pos = 0;
        this.currentChar = !expression.isEmpty() ? expression.charAt(0) : '\0';
        if(withDefSettings) defaultSettings();
    }
    
    public MathEvaluator defaultSettings(){
        return addFunction("round", Math::round)
            .addFunction("sin", Math::sin)
            .addFunction("asin", Math::asin)
            .addFunction("cos", Math::cos)
            .addFunction("acos", Math::acos)
            .addFunction("tan", Math::tan)
            .addFunction("sqrt", Math::sqrt)
            .addFunction("abs", Math::abs)
            .addFunction("log", Math::log)
            .setVariable("PI", Math.PI)
            .setVariable("PI_HALF", MathUtils.PI_HALF)
            .setVariable("PI2", MathUtils.PI2);
    }
    
    public MathEvaluator addFunction(String name, Function<Double, Number> function){
        functions.put(name, function);
        return this;
    }
    public MathEvaluator removeFunction(String name){
        functions.remove(name);
        return this;
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
        if(functions.containsKey(funcName))
            return functions.get(funcName).apply(arg).doubleValue();
        else
            throw new IllegalArgumentException("Unknown function: " + funcName);
    }
}