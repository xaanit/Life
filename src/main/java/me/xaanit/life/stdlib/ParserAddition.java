package me.xaanit.life.stdlib;

import me.xaanit.life.internal.annotations.LifeExecutable;

public class ParserAddition {

    @LifeExecutable
    public static String concat(String str, String str2) {
        return str + str2;
    }

    @LifeExecutable
    public static int add(int one, int two) {
        return one + two;
    }
}
