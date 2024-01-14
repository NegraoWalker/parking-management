package com.walker.parkingmanagement.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtilities {

    private static final double PRIMEIROS_15_MINUTES = 5.00;
    private static final double PRIMEIROS_60_MINUTES = 9.25;
    private static final double ADICIONAL_15_MINUTES = 1.75;
    private static final double DESCONTO_PERCENTUAL = 0.30;

    public static BigDecimal calculateCost(LocalDateTime entrance, LocalDateTime exit) { //Calcula o valor do estacionamento
        long minutes = entrance.until(exit, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            total = PRIMEIROS_15_MINUTES;
        } else if (minutes <= 60) {
            total = PRIMEIROS_60_MINUTES;
        } else {
            long addicionalMinutes = minutes - 60;
            Double totalParts = ((double) addicionalMinutes / 15);
            if (totalParts > totalParts.intValue()) { // 4.66 > 4
                total += PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * (totalParts.intValue() + 1));
            } else { // 4.0
                total += PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * totalParts.intValue());
            }
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calculateDiscount(BigDecimal cost, long numberOfTimes) { //Calcula o valor do desconto no estacionamento
        BigDecimal discount = ((numberOfTimes > 0) && (numberOfTimes % 10 == 0))
                ? cost.multiply(new BigDecimal(DESCONTO_PERCENTUAL))
                : new BigDecimal(0);
        return discount.setScale(2, RoundingMode.HALF_EVEN);
    }
    public static String generateReceipt() { //Método gerar recibo do estacionamento
        LocalDateTime localDateTime = LocalDateTime.now(); // Formato da data: 2023-03-16T15:23:48.616463500
        String receipt = localDateTime.toString().substring(0,19);
        return receipt.replace("-","").replace(":","").replace("T","-"); //Formato de data personalizado 20230316-152121
    }
}
