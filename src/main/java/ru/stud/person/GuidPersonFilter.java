package ru.stud.person;

import java.util.List;

// фильтр для получения выборки guid'ов конкретных людей, удовлетворяющих заданным требованиям
public interface GuidPersonFilter {
    // метод filter возвращает отфильтрованные guid'ы (List строк, т. к. поле guid имеет тип String)
    List<String> filter(List<Person> people);
}
