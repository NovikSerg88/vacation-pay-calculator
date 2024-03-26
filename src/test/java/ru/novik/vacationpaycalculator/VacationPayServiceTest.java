package ru.novik.vacationpaycalculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.novik.vacationpaycalculator.dto.VacationPayResponse;
import ru.novik.vacationpaycalculator.model.Day;
import ru.novik.vacationpaycalculator.service.VacationPayCalculatorServiceImpl;
import ru.novik.vacationpaycalculator.util.CalendarParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VacationPayServiceTest {

    private static final BigDecimal AVERAGE_MONTH_DAYS = new BigDecimal("29.3");
    private static final int SCALE = 3;

    @Mock
    private CalendarParser calendarParser;

    @InjectMocks
    private VacationPayCalculatorServiceImpl calculator;


    @Test
    void getVacationPayWithoutStartDateTest() {
        BigDecimal averageSalary = BigDecimal.valueOf(3000);
        int vacationDays = 10;
        LocalDate vacationStart = null;

        BigDecimal expectedVacationPay = averageSalary.divide(AVERAGE_MONTH_DAYS, SCALE, RoundingMode.DOWN).multiply(BigDecimal.valueOf(vacationDays));

        VacationPayResponse response = calculator.getVacationPay(averageSalary, vacationDays, vacationStart);

        assertEquals(expectedVacationPay, response.getVacationPay());
    }

    @Test
    void getVacationPayWithStartDateTest() {
        BigDecimal averageSalary = BigDecimal.valueOf(3000);
        int vacationDays = 10;
        LocalDate vacationStart = LocalDate.of(2024, 3, 1);

        List<Day> workDays = Arrays.asList(
                new Day(LocalDate.of(2024, 3, 1), true, "рабочий"),
                new Day(LocalDate.of(2024, 3, 4), true, "рабочий"),
                new Day(LocalDate.of(2024, 3, 5), true, "рабочий"),
                new Day(LocalDate.of(2024, 3, 6), true, "рабочий"),
                new Day(LocalDate.of(2024, 3, 7), true, "предпраздничный")
        );
        when(calendarParser.parseCalendar()).thenReturn(workDays);

        BigDecimal expectedVacationPay = averageSalary.divide(AVERAGE_MONTH_DAYS, SCALE, RoundingMode.DOWN).multiply(BigDecimal.valueOf(workDays.size()));

        VacationPayResponse response = calculator.getVacationPay(averageSalary, vacationDays, vacationStart);

        assertEquals(expectedVacationPay, response.getVacationPay());
    }
}
