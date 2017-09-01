package me.xaanit.life.stdlib;

import me.xaanit.life.internal.annotations.LifeExecutable;

@SuppressWarnings("unused")
public class ParserEquality {

    private ParserEquality() {
        // Not to be used.
    }


    @LifeExecutable
    public static boolean equals(int arg1, int arg2) {
        return arg1 == arg2;
    }


    @LifeExecutable
    public static boolean equals(float arg1, float arg2) {
        return arg1 == arg2;
    }


    @LifeExecutable
    public static boolean equals(double arg1, double arg2) {
        return arg1 == arg2;
    }


    @LifeExecutable
    public static boolean equals(long arg1, long arg2) {
        return arg1 == arg2;
    }


    @LifeExecutable
    public static boolean equals(char arg1, char arg2) {
        return arg1 == arg2;
    }


    @LifeExecutable
    public static boolean equals(String arg1, String arg2, boolean ignoreCase) {
        if (arg1 == null || arg2 == null)
            return arg1 == null && arg2 == null;
        return ignoreCase ? arg1.equalsIgnoreCase(arg2) : arg1.equals(arg2);
    }

    @LifeExecutable
    public static boolean greaterThan(int arg1, int arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(float arg1, int arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(double arg1, int arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(long arg1, int arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(int arg1, float arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(float arg1, float arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(double arg1, float arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(long arg1, float arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(int arg1, double arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(float arg1, double arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(double arg1, double arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(long arg1, double arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(int arg1, long arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(float arg1, long arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(double arg1, long arg2) {
        return arg1 > arg2;
    }


    @LifeExecutable
    public static boolean greaterThan(long arg1, long arg2) {
        return arg1 > arg2;
    }

    @LifeExecutable
    public static boolean lessThan(int arg1, int arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(float arg1, int arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(double arg1, int arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(long arg1, int arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(int arg1, float arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(float arg1, float arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(double arg1, float arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(long arg1, float arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(int arg1, double arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(float arg1, double arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(double arg1, double arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(long arg1, double arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(int arg1, long arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(float arg1, long arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(double arg1, long arg2) {
        return arg1 < arg2;
    }


    @LifeExecutable
    public static boolean lessThan(long arg1, long arg2) {
        return arg1 < arg2;
    }

    @LifeExecutable
    public static boolean lessOrEqual(int arg1, int arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(float arg1, int arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(double arg1, int arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(long arg1, int arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(int arg1, float arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(float arg1, float arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(double arg1, float arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(long arg1, float arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(int arg1, double arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(float arg1, double arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(double arg1, double arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(long arg1, double arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(int arg1, long arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(float arg1, long arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(double arg1, long arg2) {
        return arg1 <= arg2;
    }


    @LifeExecutable
    public static boolean lessOrEqual(long arg1, long arg2) {
        return arg1 <= arg2;
    }

    @LifeExecutable
    public static boolean greaterOrEqual(int arg1, int arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(float arg1, int arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(double arg1, int arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(long arg1, int arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(int arg1, float arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(float arg1, float arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(double arg1, float arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(long arg1, float arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(int arg1, double arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(float arg1, double arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(double arg1, double arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(long arg1, double arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(int arg1, long arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(float arg1, long arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(double arg1, long arg2) {
        return arg1 >= arg2;
    }


    @LifeExecutable
    public static boolean greaterOrEqual(long arg1, long arg2) {
        return arg1 >= arg2;
    }


}
