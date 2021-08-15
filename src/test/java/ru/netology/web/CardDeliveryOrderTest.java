package ru.netology.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTest {
    String formatDeliveryDate(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL, "a", Keys.DELETE);
    }

    @Test
    public void shouldMeetingSuccessfullyBooked() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(3));
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(exactText("Успешно! Встреча успешно забронирована на " + formatDeliveryDate(3)));
    }

    @Test
    public void shouldRequestIfNoDeliveryToTheCity() {
        $("[data-test-id=city] input").setValue("Джанкой");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(3));
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldRequestValidDateIfDays2() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(2));
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldMeetingSuccessfullyBookedIfDays4() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(4));
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(exactText("Успешно! Встреча успешно забронирована на " + formatDeliveryDate(4)));
    }

    @Test
    public void shouldRequestNoRussianLetterInName() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(4));
        $("[data-test-id=name] input").setValue("Gortchisina Varvara");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldRequestAgreementCheckbox() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(3));
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    public void shouldRequestPhoneNumberDigits12() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(3));
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=phone] input").setValue("+790123456789");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldRequestPhoneNumberDigits10() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(3));
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=phone] input").setValue("+7901234567");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldRequestIfNotChosenCity() {
        $("[data-test-id=date] input").setValue(formatDeliveryDate(3));
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldRequestIfNotChosenDate() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    public void shouldRequestIfEmptyName() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(3));
        $("[data-test-id=phone] input").setValue("+79012345678");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldRequestIfEmptyPhoneNumber() {
        $("[data-test-id=city] input").setValue("Калининград");
        $("[data-test-id=date] input").setValue(formatDeliveryDate(3));
        $("[data-test-id=name] input").setValue("Горчицына Варвара");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
}
