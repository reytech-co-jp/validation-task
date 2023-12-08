package com.validationtask.task1;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class UserPostRequestTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void usernameがnullのときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest(null, "password", "password");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("username", "ユーザー名を入力してください"));
    }

    @Test
    public void usernameが空白のときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest("", "password", "password");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("username", "ユーザー名を入力してください"),
                        tuple("username", "ユーザー名は3文字以上20文字以下である必要があります"));
    }

    @Test
    public void usernameがスペースのときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest(" ", "password", "password");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("username", "ユーザー名を入力してください"),
                        tuple("username", "ユーザー名は3文字以上20文字以下である必要があります"));

    }

    @Test
    public void usernameは3文字未満のときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest("Ya", "password", "password");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("username", "ユーザー名は3文字以上20文字以下である必要があります"));

    }

    @Test
    public void usernameは3文字のときにバリデーションエラーとならないこと() {
        UserPostRequest userPostRequest = new UserPostRequest("Yam", "password", "password");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void usernameは20文字超のときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest("Y".repeat(21), "password", "password");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("username", "ユーザー名は3文字以上20文字以下である必要があります"));

    }

    @Test
    public void usernameは20文字のときにバリデーションエラーとならないこと() {
        UserPostRequest userPostRequest = new UserPostRequest("Y".repeat(20), "password", "password");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void passwordがnullのときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest("Yamada", null, null);
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("password", "パスワードを入力してください"),
                        tuple("passwordMatching", "パスワードと確認用パスワードが一致していません"));
    }

    @Test
    public void passwordが空白のときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest("Yamada", "", "");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("password", "パスワードを入力してください"),
                        tuple("password", "パスワードは8文字以上30文字以下である必要があります"));
    }

    @Test
    public void passwordは3文字未満のときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest("Yamada", "pa", "pa");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("password", "パスワードは8文字以上30文字以下である必要があります"));

    }

    @Test
    public void passwordは8文字のときにバリデーションエラーとならないこと() {
        UserPostRequest userPostRequest = new UserPostRequest("Yamada", "p".repeat(8), "p".repeat(8));
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void passwordは30文字超のときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest("Yamada", "p".repeat(31), "p".repeat(31));
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("password", "パスワードは8文字以上30文字以下である必要があります"));

    }

    @Test
    public void passwordは30文字のときにバリデーションエラーとならないこと() {
        UserPostRequest userPostRequest = new UserPostRequest("Yamada", "p".repeat(30), "p".repeat(30));
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void passwordがスペースのときにバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest("Yamada", " ", " ");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("password", "パスワードを入力してください"),
                        tuple("password", "パスワードは8文字以上30文字以下である必要があります"));
    }

    @Test
    public void passwordとconfirmPasswordが一致しないときバリデーションエラーとなること() {
        UserPostRequest userPostRequest = new UserPostRequest("Yamada", "password", "passwords");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("passwordMatching", "パスワードと確認用パスワードが一致していません"));
    }

    @Test
    public void usernameが有効でpasswordとconfirmPasswordが一致するときバリデーションエラーとならないこと() {
        UserPostRequest userPostRequest = new UserPostRequest("Yamada", "password", "password");
        Set<ConstraintViolation<UserPostRequest>> violations = validator.validate(userPostRequest);
        assertThat(violations).isEmpty();
    }

}
