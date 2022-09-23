package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byValue;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class TopUpPage {
    private static SelenideElement heading = $("[data-test-id=dashboard]");
    private static SelenideElement topUpHeading = $(withText("Пополнение карты"));
    private static SelenideElement amountField = $("[data-test-id=amount] input");
    private static SelenideElement cardFromField = $("[data-test-id=from] input");
    private static SelenideElement cardToField = $("[data-test-id=to] input");
    private static SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private static SelenideElement errorNotification = $("[data-test-id=error-notification");

    public TopUpPage() {
        heading.shouldBe(visible);
        topUpHeading.shouldBe(visible);
        cardToField.shouldBe(disabled);
    }

    public DashboardPage cardTopUpSuccess(int amount) {
        amountField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        cardFromField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        amountField.setValue(String.valueOf(amount));
        if ($(byValue("**** **** **** "
                + DataHelper.getCardFor(DataHelper.getAuthInfo()).getNumber().split(" ")[3])).isDisplayed()) {
            cardFromField.setValue(DataHelper.getOtherCardFor(DataHelper.getAuthInfo()).getNumber());
        } else {
            cardFromField.setValue(DataHelper.getCardFor(DataHelper.getAuthInfo()).getNumber());
        }
        transferButton.click();
        return new DashboardPage();
    }

    public void cardTopUpFail(int amount) {
        amountField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        cardFromField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        amountField.setValue(String.valueOf(amount));
        if ($(byValue("**** **** **** "
                + DataHelper.getCardFor(DataHelper.getAuthInfo()).getNumber().split(" ")[3])).isDisplayed()) {
            cardFromField.setValue(DataHelper.getOtherCardFor(DataHelper.getAuthInfo()).getNumber()
                    .replace("0","8"));
        } else {
            cardFromField.setValue(DataHelper.getCardFor(DataHelper.getAuthInfo()).getNumber()
                    .replace("0","8"));
        }
        transferButton.click();
        errorNotification.shouldBe(visible, Duration.ofSeconds(15));
    }
}