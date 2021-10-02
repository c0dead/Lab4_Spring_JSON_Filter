package ru.stud.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component // хотим, чтобы Spring создал бин из этого класса
// по заданию нужно получить из json guid мужчин в возрасте от 20 до 30 лет;
// это фильтр для получения выборки guid'ов мужчин в возрасте от 20 до 30 лет
public class GuidPersonFilterByGenderAndAge implements GuidPersonFilter {
    @Autowired // Spring сам ищет подходящий бин для List<Person> people и автоматически внедряет его
    @Override // проверка, что метод интерфейса действительно переопределяется
    // метод filter возвращает отфильтрованные guid'ы
    public List<String> filter(List<Person> people) {
        // у объекта people есть метод stream()
        return people.stream()
                // методу filter передаётся лямбда; кроме filter есть и другие методы
                .filter(person -> person.getGender().equals("male")) // отбираем мужчин
                .filter(person -> person.getAge() >= 20) // в возрасте от 20 до 30 лет
                .filter(person -> person.getAge() <= 30)
                .map(Person::getGuid) // метод map позволяет трансформировать наш поток -- например, из всего Person достать только guid'ы;
                // сначала map отфильтрует все Person'ы, а затем из всех Person достанет только guid'ы
                // и составит из этого поток (stream)
                .collect(Collectors.toList()); // преобразуем всё это в List с помощью collect
        // таким образом, мы возвращаем требуемую информацию (List<String>, т. е. List guid'ов)
    }
}
