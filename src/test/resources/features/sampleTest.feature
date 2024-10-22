#language: ru
@all
Функционал: Базовый тест

  Сценарий: Сложение1
    Дано первое число 2
    И второе число 3
    Когда сумма чисел
    Тогда результат 5

  Структура сценария: Сложение
    Дано первое число <numberValue>
    И второе число <numberValue1>
    Когда сумма чисел
    Тогда результат <actual>
    Примеры:
      | numberValue | numberValue1 | actual |
      | 2           | 3            | 5      |
      | 1           | 3            | 4      |
      | 2           | 4            | 6      |
      | 2           | 76           | 5      |