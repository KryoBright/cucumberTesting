package ru.test.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.То;
import io.cucumber.java.ru.Тогда;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest
@RequiredArgsConstructor
public class StepDefs {
    private static Integer firstNumber;
    private static Integer secondNumber;
    private static Integer result;
    private static String givenString;
    private static String replacedSymbol;

    private final RandomUserFeignClient feignClient;
    private final ScheduleServiceFeignClient serviceFeignClient;
    private final ScheduleTemplateRepository scheduleTemplateRepository;

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

    private ResponseEntity<Map<String, Object>> response;

    @When("запрос на получение сотрудника")
    public void getUsersRest() {
        response = feignClient.getUsers();
    }

    @Then("статус ответа {int}")
    public void checkResponseStatus(int status) {
        assertThat(response.getStatusCode().value()).isEqualTo(status);
    }

    @Then("вывести заголовки ответа")
    public void printHeaders() {
        System.out.println(response.getHeaders());
    }

    @Then("вывести тело ответа")
    public void printBody() {
        System.out.println(response.getBody());
    }

    @Then("^присутствует непустое поле (.+)$")
    public void checkFieldPresent(String fieldName) {
        var body = response.getBody();
        var user = ((List<Map<String, Object>>)body.get("results")).get(0);
        assertThat(user).containsKey(fieldName)
                .extractingByKey(fieldName).isEqualTo("female");
    }

    private ResponseEntity<Map<String, Object>> secondResponse;
    private String id;

    @When("выполнен запрос на создание шаблона расписания")
    public void scheduleTemplateRequest() {
        response = serviceFeignClient.createScheduleTemplate();
    }

    @When("получен успешный ответ")
    public void successResponse() {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

    @Then("если выполнен запрос на получение по id из ответа")
    public void scheduleRequestById() {
        id = response.getBody().get("id").toString();
        response = serviceFeignClient.readScheduleTemplate(id);
    }

    @Then("дата создания недавняя")
    public void creationDateRecent() {
        var scheduleTemplate = scheduleTemplateRepository.findById(id)
                .orElseThrow();
        assertThat(scheduleTemplate.getCreationDate())
                .isBetween(Instant.now().minus(1, ChronoUnit.MINUTES),Instant.now());
    }

    @Then("если выполнен повторный запрос")
    public void repeatRequest() {
        secondResponse = serviceFeignClient.readScheduleTemplate(id);
    }

    @Then("ответ совпадает")
    public void responseMatch() {
        assertThat(response.getBody()).isEqualTo(response.getBody());
    }

    @Then("в базе есть шаблон с id из ответа")
    public void existsTemplate() {
        assertThat(
                scheduleTemplateRepository.existsById(id)
        ).isTrue();
    }
}
