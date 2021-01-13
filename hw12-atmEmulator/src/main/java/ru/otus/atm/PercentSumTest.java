package ru.otus.atm;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PercentSumTest {
    public static void main(String[] args) {
        List<BigDecimal> percentSumList = Arrays.asList(
                new BigDecimal("22310.06"),
                new BigDecimal("22240.39"),
                new BigDecimal("20750.92"),
                new BigDecimal("6419.77"),
                new BigDecimal("4674.00"),
                new BigDecimal("10904.61"),
                new BigDecimal("21243.56"),
                new BigDecimal("1412.02"),
                new BigDecimal("20377.49"),
                new BigDecimal("19674.82"),
                new BigDecimal("1400.56"),
                new BigDecimal("14705.90"),
                new BigDecimal("3480.33"),
                new BigDecimal("3473.84"),
                new BigDecimal("8337.21"),
                new BigDecimal("6003.00"),
                new BigDecimal("4412.51"),
                new BigDecimal("2772.10"),
                new BigDecimal("20781.87"),
                new BigDecimal("8976.62"),
                new BigDecimal("6905.02"),
                new BigDecimal("2069.27"),
                new BigDecimal("3439.26"),
                new BigDecimal("6190.66"),
                new BigDecimal("2730.43"),
                new BigDecimal("6820.62"),
                new BigDecimal("1361.15"),
                new BigDecimal("3398.50"),
                new BigDecimal("3398.51"));
        BigDecimal sum = percentSumList.stream().reduce(BigDecimal::add).get();
        System.out.println(sum);
    }
}