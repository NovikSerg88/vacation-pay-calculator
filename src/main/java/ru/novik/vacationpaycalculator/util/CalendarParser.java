package ru.novik.vacationpaycalculator.util;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.novik.vacationpaycalculator.model.Day;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CalendarParser implements Parser {

    public List<Day> parseCalendar() {

        List<Day> days = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("src/main/resources/static/calendar2024.xml");
            doc.getDocumentElement().normalize();
            NodeList zapList = doc.getElementsByTagName("ZAP");
            for (int i = 0; i < zapList.getLength(); i++) {
                Element zapElement = (Element) zapList.item(i);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(zapElement.getElementsByTagName("DATE").item(0).getTextContent(), formatter);
                int wDay = Integer.parseInt(zapElement.getElementsByTagName("WORKDAY").item(0).getTextContent());
                String dType = zapElement.getElementsByTagName("DAY_TYPE").item(0).getTextContent();
                Day day = Day.builder()
                        .date(date)
                        .workDay(wDay != 0)
                        .type(dType)
                        .build();
                days.add(day);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }
}
