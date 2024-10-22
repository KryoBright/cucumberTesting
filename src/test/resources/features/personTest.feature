#language: ru
@all
Функционал: Базовый тест

  Сценарий: Получение сотрудника
    Дано сотрудник person.json
    Тогда вывести информацию

  Сценарий: Получение сотрудника
    Дано сотрудник
      | SomeName | SomeSurname | SomePatronymic | 4 |
    Тогда вывести информацию

  Сценарий: Получение сотрудников
    Дано сотрудники people.json
    Тогда вывести информацию о сотрудниках

  Сценарий: Получение сотрудников
    Дано сотрудники
      | name      | last name    | patronymic      | age |
      | SomeName  | SomeSurname  | SomePatronymic  | 4   |
      | SomeName1 | SomeSurname1 | SomePatronymic1 | 41  |
      | SomeName2 | SomeSurname1 | SomePatronymic2 | 42  |
    Тогда вывести информацию о сотрудниках