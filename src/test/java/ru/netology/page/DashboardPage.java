package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement cardsListHeading = $(withText("Ваши карты"));

    public DashboardPage() {
        heading.shouldBe(visible);
        cardsListHeading.shouldBe(visible);
    }

    public TopUpPage cardTopUp(String id) {
        $(byAttribute("data-test-id", id)).shouldHave(text("Пополнить"))
                .find(withText("Пополнить")).click();
        return new TopUpPage();
    }

    public int getBalance(String id) {
        String balanceInfo = $(byAttribute("data-test-id", id)).shouldHave(text("Пополнить"))
                .text().split(":")[1];
        String extractedBalance = balanceInfo.substring(0, balanceInfo.indexOf("р.")).strip();
        int balance = Integer.parseInt(extractedBalance);
        return balance;
    }
}