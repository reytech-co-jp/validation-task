package com.validationtask.task2;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ProductRequestTest {
    public static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void productNameがnullのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest(null, "Electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"));
    }

    @Test
    public void productNameが空文字のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("", "Electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"),
                        tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが半角スペースのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest(" ", "Electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"),
                        tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが2文字未満のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("p", "Electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが20文字超えのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("p".repeat(21), "Electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが2文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("Ph", "Electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void productNameが20文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("P".repeat(20), "Electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void categoryがnullのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", null, 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "無効なカテゴリです"),
                        tuple("category", "入力してください"));
    }

    @Test
    public void categoryが空白のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "無効なカテゴリです"),
                        tuple("category", "入力してください"));
    }

    @Test
    public void categoryが半角スペースのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", " ", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "無効なカテゴリです"),
                        tuple("category", "入力してください"));
    }

    @Test
    public void categoryがElectronicsとClothingとBooks以外のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("Table", "Furniture", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "無効なカテゴリです"));
    }

    @Test
    public void categoryがELECTRONICSで大文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "ELECTRONICS", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void categoryがelectronicsで小文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void categoryがElectronicsで文字の先頭が大文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void categoryがCLOTHINGで大文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "CLOTHING", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void categoryがclothingで小文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "clothing", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void categoryがClothingで文字の先頭が大文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Clothing", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void categoryがBOOKSで大文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "BOOKS", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void categoryがbooksで小文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "books", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void categoryがBooksで文字の先頭が大文字のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Books", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void priceがnullのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", null);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("price", "入力してください"));
    }

    @Test
    public void priceが0以下のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 0);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("price", "0より大きい値である必要があります"));
    }

    @Test
    public void priceが1000000超えのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 1000001);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("price", "1000000以下である必要があります"));
    }

    @Test
    public void priceが0より大きいときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 1);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void priceが1000000のときにバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 1000000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void productNameとcategoryとpriceが有効な場合はバリデーションエラーとならないこと() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 150000);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }
}
