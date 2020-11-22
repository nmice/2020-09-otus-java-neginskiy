package ru.otus.atm.model;

import lombok.Getter;
import lombok.Setter;
import ru.otus.atm.enums.NominalType;

@Getter
@Setter
public class MoneyCell {
    private final NominalType nominalType;
    private int banknotesBalance;

    private static final int DEFAULT_BANKNOTES_BALANCE = 1000;

    public MoneyCell(NominalType nominalType, int banknotesBalance) {
        this.nominalType = nominalType;
        this.banknotesBalance = banknotesBalance;
    }

    public MoneyCell(NominalType nominalType) {
        this(nominalType, DEFAULT_BANKNOTES_BALANCE);
    }

    public void addBanknotes(Integer banknotesToAdd) {
        banknotesBalance += banknotesToAdd == null ? 0 : banknotesToAdd;
    }

    public void issueBanknotes(Integer banknotesToIssue) {
        banknotesBalance -= banknotesToIssue == null ? 0 : banknotesToIssue;
    }
}
