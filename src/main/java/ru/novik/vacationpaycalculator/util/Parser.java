package ru.novik.vacationpaycalculator.util;

import ru.novik.vacationpaycalculator.model.Day;

import java.util.List;

public interface Parser {
    List<Day> parseCalendar();
}
