package ru.novik.vacationpaycalculator.service;

import ru.novik.vacationpaycalculator.dto.VacationPayResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface VacationPayCalculatorService {
    VacationPayResponse getVacationPay(BigDecimal averageSalary, int vacationDays, LocalDate vacationStart);
}
