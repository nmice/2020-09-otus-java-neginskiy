package ru.otus.atm;

import com.google.common.collect.ImmutableMap;
import ru.otus.atm.enums.NominalType;
import ru.otus.atm.model.AtmEmulator;

import java.util.Map;

import static ru.otus.atm.enums.NominalType.*;

public class Main {
    public static void main(String[] args) {
        AtmEmulator atmEmulator = new AtmEmulator();

        System.out.println("***START BALANCE***");
        atmEmulator.getBalance();
        System.out.println();

        System.out.println("***ISSUE MONEY***");
        atmEmulator.issueMoney(20000);
        System.out.println();

        System.out.println("***BALANCE AFTER ISSUE***");
        atmEmulator.getBalance();
        System.out.println();

        System.out.println("***ACCEPT MONEY***");
        Map<NominalType, Integer> map = ImmutableMap.of(FIVE_THOUSAND, 10,
                THOUSAND, 34,
                FIVE_HUNDRED, 5,
                HUNDRED, 1);
        atmEmulator.acceptMoney(map);
        System.out.println();

        System.out.println("***BALANCE AFTER ACCEPT***");
        atmEmulator.getBalance();
        System.out.println();

        //atmEmulator.issueMoney(10000800); //exception overlimit
        //atmEmulator.issueMoney(80);       //exception minimum
        //atmEmulator.issueMoney(180);      //exception banknote
    }
}
