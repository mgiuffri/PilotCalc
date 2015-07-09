package com.marianogiuffrida.pilotcalc.model.Calculator;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Mariano on 20/01/2015.
 */
public class ShuntingYardEvaluator {

    public static final String WELL_FORMED_EXPRESSION_REGEX = "-?(\\d+)([+-÷*]-?\\d+)*";

    private enum TokenType{
        Number, Operator
    }

    private enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

    private static Map<Character, Operator> ops = new HashMap<Character, Operator>() {{
        put('+', Operator.ADD);
        put('-', Operator.SUBTRACT);
        put('*', Operator.MULTIPLY);
        put('d', Operator.DIVIDE);
    }};

    public static Double calculate(String expression) throws IOException, IllegalArgumentException{
        if (!isWellFormedExpression(expression)){
            throw new IllegalArgumentException("Expression is not well formed.");
        }
        return evaluate(expression);
    }

    protected static boolean isWellFormedExpression(String s){
       return s.matches(WELL_FORMED_EXPRESSION_REGEX);
    }

    protected static Double evaluate(String input) throws IOException {
        String z = input.replaceAll("÷", "d");
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(z));
        tokenizer.resetSyntax();
        tokenizer.parseNumbers();
        Stack<Double> numberStack = new Stack<>();
        Stack<Operator> operators = new Stack<>();
        TokenType lastToken = null;
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            switch (tokenizer.ttype) {
                case StreamTokenizer.TT_NUMBER:
                    if(lastToken == TokenType.Number && tokenizer.nval < 0.0) {
                        handleOperator(numberStack,operators, Operator.ADD);
                    }
                    numberStack.push(tokenizer.nval);
                    lastToken = TokenType.Number;
                    break;
                default:
                    Operator op = ops.get((char) tokenizer.ttype);
                    handleOperator(numberStack, operators, op);
                    lastToken = TokenType.Operator;
                    break;
            }
        }
        while (!operators.empty()) {
            Double op2 = numberStack.pop();
            Double op1 = numberStack.pop();
            Operator op = operators.pop();
            numberStack.push(calculate(op1, op2, op));
        }
        return numberStack.pop();
    }

    private static void handleOperator(Stack<Double> numberStack, Stack<Operator> operators, Operator op) {
        if (!operators.empty() && op.precedence < operators.peek().precedence) {
            Double op2 = numberStack.pop();
            Double op1 = numberStack.pop();
            Operator oper = operators.pop();
            numberStack.push(calculate(op1, op2, oper));
        }
        operators.push(op);
    }

    private static Double calculate(Double op1, Double op2, Operator op) {
        Double result;
        switch (op) {
            case ADD:
                result = op1 + op2;
                break;
            case SUBTRACT:
                result = op1 - op2;
                break;
            case MULTIPLY:
                result = op1 * op2;
                break;
            case DIVIDE:
                result = op1 / op2;
                break;
            default:
                return 0.0;
        }
        return result;
    }

}
