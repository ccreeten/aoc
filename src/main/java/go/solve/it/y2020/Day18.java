package go.solve.it.y2020;

import go.solve.it.util.input.Input;

import java.util.function.LongBinaryOperator;

import static go.solve.it.util.string.Regex.findNext;
import static java.lang.Long.parseLong;
import static java.util.Arrays.stream;

public final class Day18 {

    public static void main(final String... args) {
        System.out.println(part1(Input.lines("y2020/day18/input")));
        System.out.println(part2(Input.lines("y2020/day18/input")));
    }

    private static long part1(final String... expressions) {
        return stream(expressions).mapToLong(expression -> evaluateNoPrecedence(expression.replaceAll(" ", ""))).sum();
    }

    private static long evaluateNoPrecedence(final String expression) {
        var result = 0L;
        var operation = (LongBinaryOperator) (left, right) -> right;
        for (var pointer = 0; pointer < expression.length(); pointer++) {
            final var token = expression.charAt(pointer);
            if (token == '*' || token == '+') {
                operation = token == '*' ? Math::multiplyExact : Math::addExact;
            } else {
                if (token == '(') {
                    final var closingPointer = findClosingIndex(expression, pointer);
                    result = operation.applyAsLong(result, evaluateNoPrecedence(expression.substring(pointer + 1, closingPointer)));
                    pointer = closingPointer;
                } else {
                    result = operation.applyAsLong(result, parseLong(findNext(expression, "(\\d+)", pointer)));
                }
            }
        }
        return result;
    }

    private static long part2(final String... expressions) {
        return stream(expressions).mapToLong(expression -> evaluateAddPrecedence(expression.replaceAll(" ", ""))).sum();
    }

    private static long evaluateAddPrecedence(final String expression) {
        var summed = 0L;
        for (var pointer = 0; pointer < expression.length(); pointer++) {
            final var token = expression.charAt(pointer);
            if (token == '*') {
                return summed * evaluateAddPrecedence(expression.substring(pointer + 1));
            }
            if (token != '+') {
                if (token == '(') {
                    final var closingPointer = findClosingIndex(expression, pointer);
                    summed += evaluateAddPrecedence(expression.substring(pointer + 1, closingPointer));
                    pointer = closingPointer;
                } else {
                    summed += parseLong(findNext(expression, "(\\d+)", pointer));
                }
            }
        }
        return summed;
    }

    private static int findClosingIndex(final String expression, final int openIndex) {
        var closingPointer = openIndex + 1;
        var balanceCount = 1;
        while (true) {
            final var t = expression.charAt(closingPointer);
            if (t == '(') {
                balanceCount++;
            }
            if (t == ')') {
                balanceCount--;
            }
            if (balanceCount == 0) {
                return closingPointer;
            }
            closingPointer++;
        }
    }
}