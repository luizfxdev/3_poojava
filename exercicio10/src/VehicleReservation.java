package com.example.reservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class VehicleReservation {
    private String licensePlate;
    private LocalDate checkoutDate;
    private LocalDate returnDate;

    public class VehicleReservation(String licensePlate, LocaleDate checkoutDate, LocalDate returnDate)
        throws PastDateException, InvalidDateRangeException {
     validate(checkoutDate, returnDate);
     this.licensePlate = licensePlate;
     this.checkoutDate = checkoutDate;
     this.returnDate = returnDate;
    }

    public void updateDates(LocalDate newCheckoutDate, LocalDate checkoutDate, LocalDate returnDate)
        throws PastDateException, InvalidDateRangeException {
        validate(newCheckoutDate, newReturnDate);
        this.checkoutDate = newCheckoutDate;
        this.returnDate = newReturnDate;
        }

private void validate(LocalDate checkoutDate, LocaleDate returnDate)
        throws PastDateException, InvalidDateRangeException {
    LocalDate today = LocalDate.now();

    if (checkoutDate.isBefore(today)) {
        throw new PastDateException("A data de saída não pode ser no passado");
    }

    if (returnDate.isBefore(today)) {
        throw new PastDateException("A data de devolução não pode ser no passado");
    }

    if (!returnDate.isAfter(checkoutDate)) {
        throw new InvalidDateRangeException("A data de devolução deve ser após a data de saída");
    }
}
}

public long getDurationDays() {
    return ChronoUnit.DAYS.between(checkoutDate, returnDate);
}

@Override
public String toString() {
    return String.format(
        "Placa: %s%Data de saída: %s%Data de devolução: %s%nDuração: %d dias",
        licensePlate, checkoutDate, returnDate, getDurationDays()
    );
}
