package ru.test.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest
public class StepDefs {
    private static Integer firstNumber;
    private static Integer secondNumber;
    private static Integer result;
    private static String givenString;
    private static String replacedSymbol;

    @Given("^(первое|второе) число (\\d+)$")
    public void setNumber(String numberType, Integer numberValue) {
        switch (numberType) {
            case "первое":
                firstNumber = numberValue;
                break;
            case "второе":
                secondNumber = numberValue;
                break;
            default:
                break;
        }
    }

    @When("^сумма чисел$")
    public void getSum() {
        result = firstNumber + secondNumber;
        System.out.println(result);
    }

    @Then("^результат (.+)$")
    public void checkResult(Integer actual) {
        assertThat(actual).isEqualTo(result);
    }

    @Given("^строка (.+)$")
    public void setGivenString(String givenString) {
        StepDefs.givenString = givenString;
    }

    @And("^символ (.)$")
    public void setReplacedSymbol(String givenSymbol) {
        replacedSymbol = givenSymbol;
    }

    @When("^замена на (.)$")
    public void replace(String replacer) {
        givenString = givenString.replace(replacedSymbol, replacer);
    }

    @Then("^результирующая строка (.+)$")
    public void checkString(String expected) {
        assertThat(givenString).isEqualTo(expected);
    }

    private Person person;
    private List<Person> people;

    @Given("^сотрудник (.+)$")
    public void givenPerson(String fileName) {
        person = getObjectFromFile(fileName, new TypeReference<>() {
        });
    }

    @Given("^сотрудники (.+)$")
    public void givenPeople(String fileName) {
        people = getObjectFromFile(fileName, new TypeReference<>() {
        });
    }

    @Given("^сотрудник$")
    public void givenPersonTable(DataTable dataTable) {
        var row = dataTable.row(0);
        person = new Person(
                row.get(0),
                row.get(1),
                row.get(2),
                Integer.parseInt(row.get(3))
        );
    }


    @Given("^сотрудники$")
    public void givenPeopleTable(DataTable dataTable) {
        people = new ArrayList<>();
        var rows = dataTable.rows(1);
        for (List<String> row: rows.asLists()) {
            var entry = new Person(
                    row.get(0),
                    row.get(1),
                    row.get(2),
                    Integer.parseInt(row.get(3))
            );
            people.add(entry);
        }
    }


    @Then("^вывести информацию$")
    public void printInfo() {
        System.out.println(person);
    }

    @Then("^вывести информацию о сотрудниках$")
    public void printInfoMany() {
        System.out.println(people);
    }

    @SneakyThrows
    private String getJsonFromFile(String fileName) {
        String filePath = String.format("data/%s", fileName);
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + filePath);
        return new String(Files.readAllBytes(file.toPath()));
    }

    @SneakyThrows
    private <T> T getObjectFromFile(String fileName, TypeReference<T> type) {
        return new ObjectMapper()
                .readValue(getJsonFromFile(fileName), type);
    }
}
