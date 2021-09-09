package com.bobocode.fp;

public class Functions {
    /**
     * A static factory method that creates an integer function map with basic functions:
     * - abs (absolute value)
     * - sgn (signum function)
     * - increment
     * - decrement
     * - square
     *
     * @return an instance of {@link FunctionMap} that contains all listed functions
     */
    public static FunctionMap<Integer, Integer> intFunctionMap() {
        FunctionMap<Integer, Integer> intFunctionMap = new FunctionMap<>();

        // todo: add simple functions to the function map (abs, sgn, increment, decrement, square)
        intFunctionMap.addFunction("abs", Math::abs);
        intFunctionMap.addFunction("sgn", i -> Integer.compare(i, 0));
        intFunctionMap.addFunction("increment", i -> ++i);
        intFunctionMap.addFunction("decrement", i -> --i);
        intFunctionMap.addFunction("square", i -> i*i);

        return intFunctionMap;
    }
}
