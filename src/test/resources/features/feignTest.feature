#language: ru
@all
Функционал: Базовый тест

  Сценарий: Получение сотрудника по REST
    Когда запрос на получение сотрудника
    Тогда статус ответа 200
    И вывести заголовки ответа
    И вывести тело ответа
    И присутствует непустое поле gender