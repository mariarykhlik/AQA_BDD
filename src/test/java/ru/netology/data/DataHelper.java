package ru.netology.data;

import lombok.Value;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class Card {
        private String id;
        private String number;
    }

    public static Card getCardFor(AuthInfo authInfo) {
        return new Card(
                "92df3f1c-a033-48e6-8390-206f6b1f56c0",
                "5559 0000 0000 0001"
        );
    }

    public static Card getOtherCardFor(AuthInfo authInfo) {
        return new Card(
                "0f3f5c2a-249e-4c3d-8287-09f7a039391d",
                "5559 0000 0000 0002"
        );
    }
}