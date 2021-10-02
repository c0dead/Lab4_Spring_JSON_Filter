package ru.stud.person.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.stud.person.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component // хотим, чтобы Spring создал бин из этого класса
// данный класс позволяет получить объекты класса Person из json в виде списка
public class PersonMapperFromJsonImpl implements PersonMapper {
    // реализуем все методы интерфейса
    @Override // проверка, что метод интерфейса действительно переопределяется
    // метод map() возвращает данные о людях (людей, т. е. объекты класса Person) из json; эти данные возвращаются в виде List<Person>
    public List<Person> map() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/resources/generated.json"), new TypeReference<List<Person>>() { });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
