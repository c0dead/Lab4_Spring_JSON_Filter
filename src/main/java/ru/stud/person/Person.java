package ru.stud.person;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/* Класс Person -- это data class, поэтому над ним пишем @Data;
он содержит все поля, описанные в json'е; все эти поля private.

Имеем методы -- геттеры и сеттеры. Примеры:

String getGuid() {
    return guid;
}

public void setGuid(String guid) {
this.guid = guid;
}

Если в начале перед методом не написать ни одно из слов private, protected, public,
то будет подразумеваться default. default практически ничем не отличается от protected.
Т. е. по умолчанию всё, что мы создаём в наших классах, является как бы protected.

Но если в C++ protected защищает поле или метод ото всех, кроме потомков, то здесь
в этот же "разрешённый список" (для доступа к полю или методу) входит то, что входит
в тот же самый (! не выше и не ниже по уровню вложенности) пакет.

По сути, пакет -- это архитектура папок в нашем приложении. Пример пакета: ru.stud.person
Если класс находится в пакете, то в начале класса (кода) автоматически приписывается package <название пакета>.
В нашем случае класс Person находится в пакете ru.stud.person.

Если мы хотим воспользоваться классом Person где-то вне пакета, нам нужно импортировать данный класс.
Для этого можно вручную прописать в начале кода:
import ru.stud.person.Person (Person -- нужный класс)
либо
import ru.stud.person.* (тогда мы импортируем все классы в этом пакете, но статические анализаторы кода скорее всего будут на это жаловаться)
Либо класс можно импортировать автоматически при создании объекта класса Person (всё "подтянется").

Имя класса должно совпадать с именем файла! Для Java файл = класс

Ключевое слово static делает поле общим для всех классов или для всех функций. Но статические методы не имеют доступа ни к чему,
кроме static переменных (так же, как и в C++).

Мы создаём data object, т. е. объект, который служит контейнером для пересылки от источника данных к потребителю этих данных.
Писать сеттеры и геттеры не хочется. А ещё не хочется заниматься функцией equal, функцией hashCode и функцией toString.

Подключаем библиотеку (пакет) Lombok в Maven (дописываем всё необходимое в файл pom.xml).
В IntelliJ IDEA давно ожидают эту библиотеку в любом проекте. У них есть плагин. Во вкладке Refactor
мы можем найти Lombok и Delombok. Воспользуемся ими и посмотрим, что они делают.

Класс Person -- это data class, поэтому над ним пишем @Data. Что происходит? Класс Person получает реализацию всех
сеттеров и геттеров на приватные поля, equals, hashCode, toString (стандартные реализации, т. е. как это видит Lombok).
Теперь, если мы запустим main с тем же кодом, toString будет определён так (он вернёт следующее):
Person(guid=null, isActive=null, ..., tags=null, favoriteFruit=null)

Нажмём Refactor - Delombok - @Data:
плагин позволяет нам посмотреть, что же сделала эта библиотека: в классе Person (явно?) появляется реализация всего,
что она сделала, а именно она создала: конструктор, все геттеры, все сеттеры, определила equals по всем правилам,
hashCode и переопределила toString (причём @Override не написан). Их можно стереть (останутся неявно?), оставив у класса
Person лишь @Data.

@Word -- это аннотация. Аннотация -- это интерфейс. Компилятор видит аннотацию и реализует тот интерфейс, который был привязан
к этой аннтоации. Можно посмотреть реализацию @Data в External Libraries - ...:lombok:... - lombok.

Lombok приносит много разных аннотаций.

Нам нужно откуда-то получать и хранить данные. Для этого подключим библиотеку Jackson-Databind (дописываем всё необходимое в файл pom.xml).
*/

@Data
// Класс Person -- это data class, поэтому над ним пишем @Data => класс Person получает конструктор, реализацию всех
// сеттеров и геттеров на приватные поля, а также методы equals, hashCode, toString (стандартные реализации)
public class Person {
    private String guid;
    private Boolean isActive;
    private String balance;
    private Integer age;
    private String eyeColor;
    private String name;
    private String gender;
    private String company;
    private String email;
    private String phone;
    private String address;
    private String registered;
    private List<String> tags;
    private String favoriteFruit;
}
