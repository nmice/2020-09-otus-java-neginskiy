package ru.otus.atm.enums;

import lombok.Getter;

@Getter
public enum NominalType {
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    FIVE_THOUSAND(5000);

    private int value;

    NominalType(int value) {
        this.value = value;
    }
}
