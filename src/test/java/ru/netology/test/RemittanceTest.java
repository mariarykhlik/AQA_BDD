package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TopUpPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.*;

class RemittanceTest {
    static DataHelper.AuthInfo authInfo = DataHelper.getAuthInfo();
    static DataHelper.VerificationCode verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    static String cardId = DataHelper.getCardFor(authInfo).getId();
    static String otherCardId = DataHelper.getOtherCardFor(authInfo).getId();

    @BeforeEach
    void equalizeBalances() {
        LoginPage loginPage = open("http://localhost:9999", LoginPage.class);
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int cardBalance = dashboardPage.getBalance(cardId);
        int otherCardBalance = dashboardPage.getBalance(otherCardId);
        if (cardBalance < otherCardBalance) {
            dashboardPage.cardTopUp(cardId).cardTopUpSuccess((otherCardBalance - cardBalance) / 2);
        } else if (cardBalance > otherCardBalance) {
            dashboardPage.cardTopUp(otherCardId).cardTopUpSuccess((cardBalance - otherCardBalance) / 2);
        }
    }

    @Test
    void shouldRemitMoneyForOwnCards() {
        LoginPage loginPage = open("http://localhost:9999", LoginPage.class);
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int cardBalance = dashboardPage.getBalance(cardId);
        TopUpPage topUpPage = dashboardPage.cardTopUp(cardId);
        DashboardPage afterRemittancePage = topUpPage.cardTopUpSuccess(cardBalance / 2)
                .cardTopUp(otherCardId).cardTopUpSuccess(cardBalance / 2)
                .cardTopUp(cardId).cardTopUpSuccess(cardBalance / 2);
        int expected = cardBalance;
        int actual = afterRemittancePage.getBalance(cardId) - afterRemittancePage.getBalance(otherCardId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotRemitAmountExceedBalance() {
        LoginPage loginPage = open("http://localhost:9999", LoginPage.class);
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int cardBalance = dashboardPage.getBalance(cardId);
        TopUpPage topUpPage = dashboardPage.cardTopUp(otherCardId);
        DashboardPage afterRemittancePage = topUpPage.cardTopUpSuccess(cardBalance * 2);
        Assertions.assertFalse(afterRemittancePage.getBalance(cardId) < 0);
    }

    @Test
    void shouldNotRemitNegativeAmount() {
        LoginPage loginPage = open("http://localhost:9999", LoginPage.class);
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int cardBalance = dashboardPage.getBalance(cardId);
        TopUpPage topUpPage = dashboardPage.cardTopUp(otherCardId);
        DashboardPage afterRemittancePage = topUpPage.cardTopUpSuccess(-cardBalance);
        Assertions.assertFalse(afterRemittancePage.getBalance(cardId) > cardBalance);
    }

    @Test
    void shouldBeErrorNotificationForWrongCardFrom() {
        LoginPage loginPage = open("http://localhost:9999", LoginPage.class);
        VerificationPage verificationPage = loginPage.validLogin(authInfo);
        DashboardPage dashboardPage = verificationPage.validVerify(verificationCode);
        int cardBalance = dashboardPage.getBalance(cardId);
        TopUpPage topUpPage = dashboardPage.cardTopUp(otherCardId);
        topUpPage.cardTopUpFail(cardBalance);
    }
}