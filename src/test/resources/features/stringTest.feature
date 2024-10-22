#language: ru
@all

Функциональность: Строки

  Структура сценария: Замена символа в строке
    Дано строка <givenString>
    И символ <givenSymbol>
    Когда замена на <replacer>
    Тогда результирующая строка <expected>
    Примеры:
      | givenString | givenSymbol | replacer | expected |
      | abcdefgh    | d           | f        | abcfefgh |
      | dddd        | d           | f        | ffff     |
      | dddd        | d           | c        | cccc     |