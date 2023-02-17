package me.swerve.hub.util;

import lombok.Getter;

public class Pair {
    @Getter private final Object valueOne;
    @Getter private final Object valueTwo;

    public Pair(Object valueOne, Object valueTwo) {
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }
}
