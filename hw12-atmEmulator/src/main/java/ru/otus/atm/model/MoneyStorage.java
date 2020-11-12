package ru.otus.atm.model;

import lombok.Getter;
import ru.otus.atm.enums.NominalType;

import java.util.HashMap;
import java.util.Map;

import static ru.otus.atm.enums.NominalType.*;

@Getter
public class MoneyStorage {

    private final Map<NominalType, MoneyCell> moneyCellByNominalMap;

    {
        moneyCellByNominalMap = new HashMap<>();
        moneyCellByNominalMap.put(HUNDRED, new MoneyCell(1000));
        moneyCellByNominalMap.put(FIVE_HUNDRED, new MoneyCell(1000));
        moneyCellByNominalMap.put(THOUSAND, new MoneyCell(1000));
        moneyCellByNominalMap.put(FIVE_THOUSAND, new MoneyCell(1000));
    }

    public void acceptMoney(Map<NominalType, Integer> banknotesNumberByNominalMap) {
        banknotesNumberByNominalMap.forEach((key, value) -> moneyCellByNominalMap.get(key)
                .setBanknotesBalance(moneyCellByNominalMap.get(key).getBanknotesBalance() + value));
    }

    public Map<NominalType, Integer> issueMoney(int requestedSumm) {
        int balance = requestedSumm;

        int fiveThousandRequired = balance / FIVE_THOUSAND.getValue();
        int fiveThousandAvailable = moneyCellByNominalMap.get(FIVE_THOUSAND).getBanknotesBalance();
        if (fiveThousandAvailable < fiveThousandRequired) {
            balance -= FIVE_THOUSAND.getValue() * fiveThousandAvailable;
            fiveThousandRequired = FIVE_THOUSAND.getValue();
        } else {
            balance = balance % FIVE_THOUSAND.getValue();
        }

        int thousandRequired = balance / THOUSAND.getValue();
        int thousandAvailable = moneyCellByNominalMap.get(THOUSAND).getBanknotesBalance();
        if (thousandAvailable < thousandRequired) {
            balance -= THOUSAND.getValue() * thousandAvailable;
            thousandRequired = THOUSAND.getValue();
        } else {
            balance = balance % THOUSAND.getValue();
        }

        int fiveHundredRequired = balance / FIVE_HUNDRED.getValue();
        int fiveHundredAvailable = moneyCellByNominalMap.get(FIVE_HUNDRED).getBanknotesBalance();
        if (fiveHundredAvailable < fiveHundredRequired) {
            balance -= FIVE_HUNDRED.getValue() * fiveHundredAvailable;
        } else {
            balance = balance % FIVE_HUNDRED.getValue();
        }

        int hundredRequired = balance / HUNDRED.getValue();
        int hundredAvailable = moneyCellByNominalMap.get(HUNDRED).getBanknotesBalance();
        if (hundredAvailable < hundredRequired) {
            balance -= HUNDRED.getValue() * hundredAvailable;
        } else {
            balance = balance % HUNDRED.getValue();
        }

        if (balance > 0) {
            throw new IllegalArgumentException("Unfortunately, this amount is not available for issuing, " +
                    "and there are not enough banknotes to exchange.");
        }
        Map<NominalType, Integer> resultMap = new HashMap<>();
        resultMap.put(FIVE_THOUSAND, fiveThousandRequired);
        resultMap.put(THOUSAND, thousandRequired);
        resultMap.put(FIVE_HUNDRED, fiveHundredRequired);
        resultMap.put(HUNDRED, hundredRequired);
        reduceRemainingValues(resultMap);
        return resultMap;
    }

    private void reduceRemainingValues(Map<NominalType, Integer> resultMap) {
        resultMap.forEach((key, value) -> moneyCellByNominalMap.get(key).setBanknotesBalance(
                moneyCellByNominalMap.get(key).getBanknotesBalance() - value));
    }

    public int getBalance() {
        int balance = 0;
        for (NominalType nominalType : NominalType.values()) {
            balance += moneyCellByNominalMap.get(nominalType).getBanknotesBalance()
                    * nominalType.getValue();
        }
        return balance;
    }
}
