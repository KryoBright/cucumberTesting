package ru.test.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		//Статический неизменяемый список
		List<Integer> constantList = List.of(1, 2, 3);

		//Список на динамическом массиве
		List<Integer> dynamicList = new ArrayList<>();
		dynamicList.add(1);
		dynamicList.add(2);
		dynamicList.add(3);

		/*
		for (int i=0; i<10; i=i+1) {
			constantList.add(i); <-- Вызовет ошибку
		}
		*/

		//Добавть элементы от 1 до 10
		for (int i=0; i<10; i=i+1) {
			dynamicList.add(i);
		}

		//Вывод с синтаксисом while
		int index = 0;
		while (index< dynamicList.size()) {
			System.out.println(dynamicList.get(index));
			index++;
		}

		//Вывод с синтаксисом foreach
		for(Integer item:dynamicList) {
			System.out.println(item);
		}
	}

}
