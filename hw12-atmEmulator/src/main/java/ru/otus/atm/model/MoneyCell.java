package ru.otus.atm.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyCell {

    public MoneyCell(int banknotesBalance) {
        this.banknotesBalance = banknotesBalance;
    }

    private int banknotesBalance;
}
