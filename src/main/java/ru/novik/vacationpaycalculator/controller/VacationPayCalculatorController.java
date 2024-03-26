package ru.novik.vacationpaycalculator.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.novik.vacationpaycalculator.dto.VacationPayResponse;
import ru.novik.vacationpaycalculator.service.VacationPayCalculatorService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class VacationPayCalculatorController {

    private final VacationPayCalculatorService calculatorService;

    @GetMapping("/calculate")
    public VacationPayResponse getVacationPay(@Positive @RequestParam("salary") BigDecimal averageSalary,
                                              @Positive @RequestParam(value = "days") Integer vacationDays,
                                              @RequestParam(value = "date", required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate vacationDate) {

        log.info("Received GET request to get vacation pay at the period {} days with average salary {}",
                vacationDays, averageSalary);
        return calculatorService.getVacationPay(averageSalary, vacationDays, vacationDate);
    }
}
