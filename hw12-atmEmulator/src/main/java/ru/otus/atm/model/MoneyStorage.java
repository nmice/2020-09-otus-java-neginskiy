package ru.otus.atm.model;

import lombok.Getter;
import ru.otus.atm.enums.NominalType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.otus.atm.enums.NominalType.*;

@Getter
public class MoneyStorage {

    private final List<MoneyCell> moneyCellList = Arrays.asList(new MoneyCell(HUNDRED),
            new MoneyCell(FIVE_HUNDRED),
            new MoneyCell(THOUSAND),
            new MoneyCell(FIVE_THOUSAND));

    public void acceptMoney(Map<NominalType, Integer> banknotesNumberByNominalMap) {
        for (MoneyCell mc : moneyCellList) {
            mc.addBanknotes(banknotesNumberByNominalMap.get(mc.getNominalType()));
        }
    }

    public Map<NominalType, Integer> issueMoney(int requestedSumm) {
        if (checkBanknotesBalance(requestedSumm)) {
            return reduceBanknotesBalance(requestedSumm);
        } else {
            throw new IllegalArgumentException("Unfortunately, this amount is not available for issuing, " +
                    "and there are not enough banknotes to exchange.");
        }
    }

    private boolean checkBanknotesBalance(int requestedSumm) {
        List<NominalType> ntList = Arrays.asList(NominalType.values());
        ntList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int balance = requestedSumm;

        for (NominalType nt : ntList) {
            int required = balance / nt.getValue();
            int available = getCellByNominal(nt).getBanknotesBalance();
            if (available < required) {
                balance -= nt.getValue() * available;
            } else {
                balance = balance % nt.getValue();
            }
        }
        return balance == 0;
    }

    private Map<NominalType, Integer> reduceBanknotesBalance(int requestedSumm) {
        List<NominalType> ntList = Arrays.asList(NominalType.values());
        ntList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int balance = requestedSumm;

        Map<NominalType, Integer> resultMap = new HashMap<>();
        for (NominalType nt : ntList) {
            int required = balance / nt.getValue();
            int available = getCellByNominal(nt).getBanknotesBalance();
            if (available < required) {
                balance -= nt.getValue() * available;
                getCellByNominal(nt).setBanknotesBalance(0);
                resultMap.put(nt, available);
            } else {
                balance = balance % nt.getValue();
                getCellByNominal(nt).setBanknotesBalance(available - required);
                resultMap.put(nt, required);
            }
        }
        return resultMap;
    }

    public int getBalance() {
        int balance = 0;
        for (MoneyCell moneyCell : moneyCellList) {
            balance += moneyCell.getNominalType().getValue() * moneyCell.getBanknotesBalance();
        }
        return balance;
    }

    private MoneyCell getCellByNominal(NominalType nominalType) {
        return moneyCellList.stream().filter(mc -> mc.getNominalType() == nominalType).findFirst().orElse(null);
    }
}
