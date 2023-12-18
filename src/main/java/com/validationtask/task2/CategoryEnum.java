package com.validationtask.task2;

import java.util.Optional;

public enum CategoryEnum {
    ELECTRONICS,
    CLOTHING,
    BOOKS;

    public static CategoryEnum from(String value) {
        return Optional.of(CategoryEnum.valueOf(value.toUpperCase())).orElseThrow(() -> new IllegalArgumentException("{ValidCategory}"));
    }
}
