package ru.stud.person.mapper;

import ru.stud.person.Person;

import java.util.List;

/* этот интерфейс map'ит наших Person (людей) в List<Person> (т. е. сохраняет их в List<Person>) -- это его ожидаемое поведение
(другими словами, этот интерфейс позволяет получить объекты класса Person в виде списка)
информацию о людях мы можем получать откуда угодно -- например, из json, из БД, из REST-запроса, из какой-нибудь очереди */
public interface PersonMapper {
    // метод map() возвращает данные о людях откуда угодно; эти данные должны возвращаться в виде List<Person>, остальное нас не интересует
    List<Person> map();
}
