#language: ru
@all
Функционал: Шаблон расписания

  Сценарий: Создание шаблона
    Когда выполнен запрос на создание шаблона расписания
    И получен успешный ответ
    То если выполнен запрос на получение по id из ответа
    И получен успешный ответ
    И если выполнен повторный запрос
    То ответ совпадает
    И в базе есть шаблон с id из ответа
    И дата создания недавняя