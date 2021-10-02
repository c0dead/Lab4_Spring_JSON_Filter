import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.stud.person.Config;
import ru.stud.person.GuidPersonFilter;
import ru.stud.person.GuidPersonFilterByGenderAndAge;
import ru.stud.person.Person;
import ru.stud.person.mapper.PersonMapper;
import ru.stud.person.mapper.PersonMapperFromJsonImpl;

import java.util.List;

/* Задача: взять json, содержащий некоторые данные пользователей, и обработать эти данные.

Переносим json-файл в папку resources.

Для решения используем паттерн @Data.

Подключаем библиотеки Lombok (для @Data) и Jackson-Databind (для ObjectMapper, stream) (дописываем всё необходимое в файл pom.xml).
*/

public class Main {
    public static void main(String[] args) {
        /* ТЕОРИЯ ПО РЕШЕНИЮ
        Используем @Data для класса Person.
        Подключаем библиотеку Jackson-Databind (дописываем всё необходимое в файл pom.xml).

        Получаем наш json, делая import com.fasterxml.jackson.databind.ObjectMapper. Пишем

        ObjectMapper objectMapper = new ObjectMapper();

        Пишем

        objectMapper.readValue(new File(src/main/resources/generated.json), new TypeReference<List<Person>>() { })

        File -- это класс. json лежит у нас в resources, поэтому имеем право обращаться к нему по пути, потому что он будет
        таскаться с нами пакетами (?). Так же из Google узнаём, что вторым параметром нужно прописать new TypeReference<List<Person>>() { }.

        Написанная строка подчёркивается красным: жалуется, что мы не перехватываем IOException. Т. е. это ошибка ввода-вывода,
        когда, например, у нас файл не открылся или закончился слишком рано, или что-то подобное.

        Мы можем своим функциям приписывать с помощью ключевого слова throws какие исключения он должен пробрасывать наружу.
        readValue пробрасывает 3 вида исключений: throws IOException, ... (реализацию можно посмотреть, если "провалиться")

        Мы поступим самонадеянно и добавим try-catch (перехват только IOException):

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File("src/main/resources/generated.json"), new TypeReference<List<Person>>() { });
        } catch (IOException e) {
            e.printStackTrace();
        }

        А куда мы читаем наш класс? Создадим list объектов класса Person:
        List<Person> personList;

        А что такое List? List -- это не класс, это интерфейс (можно увидеть, если "провалиться"). А что есть интерфейс?
        В Java, в отличие от C и C++, множественное наследование запрещено. Т. е. слово extends ("наследуется от") можно использовать только по отношению
        к одному-единственному классу (классу, который мы наследуем). Мы наследуем все поля, методы, конструкторы.

        И у нас появляется ключевое слово super -- для того, чтобы обращаться к методам, конструкторам и полям родительского класса.

        Но множественное наследование нам нужно, но немного в другом виде. Нужна такая штука, что мы можем задавать ожидаемое поведение.
        Для это существуют интерфейсы (они позволяют это делать).

        Интерфейсы -- это описания прототипов функций, т. е. вообще ничего, кроме прототипов функций, в интерфейсе нет.
        Но интерфейс закладывает ожидаемое поведение.

        Интерфейс List имеет методы size, isEmpty, contains, iterator'ы, преобразование в массив toArray, добавить add, удалить remove
        -- многие методы, которые мы ожидаем от List'а. А реализаций List'ов очень много (например, есть LinkedList, ArrayList).
        Это реализации интерфейсов, и нам как разработчику неинтересно, пришёл нам ArrayList, LinkedList и всё остальное.
        Это определяется на уровне проектирования системы (это -- т. е. то, что мы используем), если мы оптимизируем код.

        А пользователю интересно наконец-то получить себе то, что мы вернули. Соответственно, функция readValue возвращает нам
        List<Person>. То есть сохраняем полученные данные в personList:

        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> personList;
        try {
            personList = objectMapper.readValue(new File("src/main/resources/generated.json"), new TypeReference<List<Person>>() { });
            System.out.println(personList.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Вернёт ли метод readValue LinkedList, ArrayList -- это уже на его усмотрение.
        В Java проектирование идёт в основном от интерфейсов -- для того, чтобы мы могли иметь различную реализацию одного и того же интерфейса.

        Проверим, что всё работает (выведем информацию о 0-ом человеке):
        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> personList = null;
        try {
            personList = objectMapper.readValue(new File("src/main/resources/generated.json"), new TypeReference<List<Person>>() { });
            System.out.println(personList.get(0)); // здесь вывод данных
        } catch (IOException e) {
            e.printStackTrace();
        }

        Мы эти данные (json) получили и теперь хотим их как-то обработать (получить из json guid мужчин в возрасте от 20 до 30 лет)
        -- хотим профильтровать то, что мы получили.

        В Java 7 есть такая вещь, как stream'ы. Они пришли вместе с лямбдами. Это добавление функционального программирования.
        Это даёт нам возможность писать более лакончиный код.

        Обработку данных можно полностью отдавать stream'ам. У объекта personList есть метод stream().
        Пишем (хотим получить List строк, т. к. поле guid имеет тип String):
        List<String> guidList = personList.stream();
                .filter(person -> person.getGender().equals("male")) // в скобках лямбда; вообще кроме filter есть и другие методы
                .filter(person -> person.getAge() >= 20)
                .filter(person -> person.getAge() <= 30)
                .map(Person::getGuid) // метод map позволяет трансформировать наш поток -- например, из всего Person достать только guid'ы
                                      // это всё делается через указатель на функцию лямбда
                                      // мы делаем map getPerson (сначала он отфильтрует все Person'ы), затем из всех Person достанет только guid'ы
                                      // и составит из этого поток (stream)
                .collect(Collectors.toList()) // преобразуем всё это в List с помощью collect

        Таким образом, получили наш guidList (в guidList сохранили то, что требовалось в задании). Теперь просто выведем его:
        System.out.println(guidList);

        Ещё про интерфейсы: интерфейс нельзя создать через new, но можно создать анонимный класс
        Пусть имеется интерфейс PersonMapper:
        PersonMapper personMapper = new PersonMapper() {
        @Override
        public List<Person> map() {
            return null; // null возвращаем для примера
            }
        };

        */

        // РЕШЕНИЕ

        // Создаём контекст, в качестве аргумента передаём название конфигурационного файла.
        // Класс ClassPathXmlApplicationContext откроет указанный файл и создаст нам pool (бассейн) бинов,
        // которые были описаны в указанном файле;
        // класс ClassPathXmlApplicationContext обращается к файлу applicationContext.xml, считывает его и
        // помещает все описанные в нём бины в applicationContext
        // ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // избавляемся от xml конфигурации: у нас есть конфигурационный файл Config (полностью переходим на java конфигурацию)
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        /* Spring сканирует все классы, находит классы со специальными аннотациями и автоматически создаёт бины (зависимости)
        из этих классов.

        Аннотацией @Component помечаем класс, если хотим, чтобы Spring создал бин из этого класса
        */

        // создаём интерфейс personMapper для получения данных о людях из json
        // PersonMapper personMapper = new PersonMapperFromJsonImpl();
        PersonMapper personMapper = context.getBean(PersonMapperFromJsonImpl.class);

        // данные о людях получаем с помощью метода map() personMapper'а в виде List<Person>,
        // сохраняем их в personList
        List<Person> personList = personMapper.map();

        // создаём интерфейс (фильтр) guidPersonFilter для получения выборки guid'ов конкретных людей (по параметрам gender и age)
        // GuidPersonFilter guidPersonFilter = new GuidPersonFilterByGenderAndAge();
        GuidPersonFilter guidPersonFilter = context.getBean(GuidPersonFilterByGenderAndAge.class);

        // метод filter возвращает List отфильтрованных guid'ов
        // guid'ы соответствующих требованиям людей сохраняем в guidList виде List<String>
        List<String> guidList = guidPersonFilter.filter(personList);

        // таким образом, получили наш guidList (в guidList сохранили то, что требовалось в задании)
        // выводим его
        System.out.println(guidList);
    }
}
