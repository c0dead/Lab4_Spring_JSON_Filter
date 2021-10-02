package ru.stud.person;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/* @Configuration: говорим, что этот файл -- конфигурация: это надо для того, чтобы Spring понимал, что это конфигурационный файл, в котором есть
условия сканирования, и в нём будут задаваться бины */
@Configuration
@ComponentScan("ru") // указываем конфигурационному файлу, где ему искать другие компоненты; позволяет подтянуть @Component,
// посмотреть все его поля и понять, что и куда надо подставлять; @ComponentScan подставляет @Autowired в наш бин personList
public class Config {
    // делаем ComponentScan, у нас создаётся бин personList: List<Person> personList = personMapper.map();
    // т. е. мы получаем объекты класса Person из json в виде списка
    @Bean
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
