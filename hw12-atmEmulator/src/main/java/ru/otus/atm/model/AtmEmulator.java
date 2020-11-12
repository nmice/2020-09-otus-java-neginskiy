package ru.otus.atm.model;

import lombok.Getter;
import ru.otus.atm.enums.NominalType;
import ru.otus.atm.service.MoneyStorageService;

import java.util.Map;

public class AtmEmulator {

    public AtmEmulator() {
        this(new MoneyStorageService());
    }

    public AtmEmulator(MoneyStorageService moneyStorageService) {
        this.moneyStorageService = moneyStorageService;
    }

    private final MoneyStorageService moneyStorageService;

    public void acceptMoney(Map<NominalType, Integer> banknotesNumberByNominalMap) {
        moneyStorageService.acceptMoney(banknotesNumberByNominalMap);
        System.out.println("MONEY WAS SUCCESSFULLY ACCEPTED.");
        banknotesNumberByNominalMap.forEach((k, v) -> System.out.println(k.getValue() + " - ACCEPTED " + v + " BANKNOTES"));
        System.out.println(banknotesNumberByNominalMap);
    }

    public void issueMoney(int requestedSumm) {
        Map<NominalType, Integer> map = moneyStorageService.issueMoney(requestedSumm);
        System.out.println("MONEY WAS SUCCESSFULLY ISSUED.");
        map.forEach((k, v) -> System.out.println(k.getValue() + " - ISSUED " + v + " BANKNOTES"));
        System.out.println(map);
    }

    public int getBalance() {
        int atmBalance = moneyStorageService.getBalance();
        System.out.println("BALANCE IS " + atmBalance + "RUR.");
        return atmBalance;
    }

}
