package ru.otus.atm.service;

import lombok.Getter;
import lombok.Setter;
import ru.otus.atm.enums.NominalType;
import ru.otus.atm.model.MoneyStorage;

import java.util.Map;

@Getter
@Setter
public class MoneyStorageService {

    public MoneyStorageService() {
        this(new MoneyStorage());
    }

    public MoneyStorageService(MoneyStorage moneyStorage) {
        this.moneyStorage = moneyStorage;
    }

    MoneyStorage moneyStorage;

    public void acceptMoney(Map<NominalType, Integer> banknotesNumberByNominalMap) {
        moneyStorage.acceptMoney(banknotesNumberByNominalMap);
    }

    public Map<NominalType, Integer> issueMoney(int requestedSumm) {
        if (requestedSumm < 100) {
            throw new IllegalArgumentException("The amount cannot be less than 100RUR.");
        }
        if (requestedSumm % 100 != 0) {
            throw new IllegalArgumentException("The minimum denomination of available banknotes - 100RUR.");
        }
        if (requestedSumm > getBalance()) {
            throw new IllegalArgumentException("Insufficient funds at the ATM.");
        }
        return moneyStorage.issueMoney(requestedSumm);
    }

    public int getBalance() {
        return moneyStorage.getBalance();
    }
}
