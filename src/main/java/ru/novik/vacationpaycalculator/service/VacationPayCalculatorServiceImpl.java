package ru.novik.vacationpaycalculator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.novik.vacationpaycalculator.dto.VacationPayResponse;
import ru.novik.vacationpaycalculator.model.Day;
import ru.novik.vacationpaycalculator.util.CalendarParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationPayCalculatorServiceImpl implements VacationPayCalculatorService {

    private final CalendarParser calendarParser;
    private static final BigDecimal AVERAGE_MONTH_DAYS = new BigDecimal("29.3");
    private static final int SCALE = 3;

    @Override
    public VacationPayResponse getVacationPay(BigDecimal averageSalary, int vacationDays, LocalDate vacationStart) {
        VacationPayResponse response = new VacationPayResponse();
        BigDecimal vacationPay;
        if (vacationStart == null) {
            vacationPay = averageSalary.divide(AVERAGE_MONTH_DAYS, SCALE, RoundingMode.DOWN).multiply(BigDecimal.valueOf(vacationDays));
        } else {
            List<LocalDate> workDays = calendarParser.parseCalendar().stream()
                    .filter(day -> day.getDate().isEqual(vacationStart) || day.getDate().isAfter(vacationStart))
                    .limit(vacationDays)
                    .filter(Day::isWorkDay)
                    .map(Day::getDate)
                    .toList();
            int paidDays = workDays.size();
            vacationPay = averageSalary.divide(AVERAGE_MONTH_DAYS, SCALE, RoundingMode.DOWN).multiply(BigDecimal.valueOf(paidDays));
        }
        response.setVacationPay(vacationPay);
        return response;
    }
}

