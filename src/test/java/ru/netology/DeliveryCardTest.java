package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {
    private WebDriver driver;

    String date(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldBeAcceptedForm() {
        String deliveryDay = date(3);
        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (deliveryDay));
        $("[data-test-id=name] input").setValue("Селиванов-Иванов Петр");
        $("[data-test-id=phone] input").setValue("+79998881122");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Успешно! Встреча успешно забронирована на " + deliveryDay));
    }

    @Test
    void shouldRuinCityIncorrect() {
        String deliveryDay = date(3);
        $("[data-test-id=city] input").setValue("Kirov");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (deliveryDay));
        $("[data-test-id=name] input").setValue("Селиванов-Иванов Петр");
        $("[data-test-id=phone] input").setValue("+79998881122");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id='city'].input_invalid").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldRuinDateDeliveryIncorrect() {
        String deliveryDay = date(2);
        $("[data-test-id=city] input").setValue("Смоленск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (deliveryDay));
        $("[data-test-id=name] input").setValue("Селиванов-Иванов Петр");
        $("[data-test-id=phone] input").setValue("+79998881122");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=date] .input_invalid").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSendFormWithoutDash() {
        String deliveryDay = date(3);
        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (deliveryDay));
        $("[data-test-id=name] input").setValue("Селиванов Петр");
        $("[data-test-id=phone] input").setValue("+79998881122");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + deliveryDay));
    }

    @Test
    void shouldRuinSpecialSymbol() {
        String deliveryDay = date(3);
        $("[data-test-id=city] input").setValue("Петрозаводск");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (deliveryDay));
        $("[data-test-id=name] input").setValue("Cеливанв-Иванов Петр");
        $("[data-test-id=phone] input").setValue("+79998881122");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=name].input_invalid").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldRuinPhoneNumberIncorrect() {
        String deliveryDay = date(3);
        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (deliveryDay));
        $("[data-test-id=name] input").setValue("Селиванов-Иванов Петр");
        $("[data-test-id=phone] input").setValue("79998881122");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=phone].input_invalid").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldRuinPhoneNumberIncorrectLong() {
        String deliveryDay = date(3);
        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (deliveryDay));
        $("[data-test-id=name] input").setValue("Селиванов-Иванов Петр");
        $("[data-test-id=phone] input").setValue("+799988811222");
        $("[data-test-id=agreement]").click();
        $(By.className("button__text")).click();
        $("[data-test-id=phone].input_invalid").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldCheckBoxFalse() {
        String deliveryDay = date(3);
        $("[data-test-id=city] input").setValue("Киров");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE, (deliveryDay));
        $("[data-test-id=name] input").setValue("Селиванов-Иванов Петр");
        $("[data-test-id=phone] input").setValue("+79998881122");
        $(By.className("button__text")).click();
        $("[data-test-id=agreement].input_invalid").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Я соглашаюсь с условиями"));
    }
}