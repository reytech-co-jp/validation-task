package com.validationtask.task2;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource({
            "2, Electronics, 150000, 2",
            "10, Electronics, 150000, 10",
            "20, Electronics, 150000, 20",
            "20, ELECTRONICS, 150000, 20",
            "20, electronics, 150000, 20",
            "20, CLOTHING, 150000, 20",
            "20, Clothing, 150000, 20",
            "20, clothing, 150000, 20",
            "20, BOOKS, 150000, 20",
            "20, Books, 150000, 20",
            "20, books, 150000, 20",
            "20, Electronics, 1, 20",
            "20, Electronics, 1000000, 20"
    })
    public void 有効なproductNameとcategoryとprice場合はバリデーションエラーとならないこと(String productNameCount, String category, String price, String sellerCount) {
        ProductRequest productRequest = new ProductRequest("p".repeat(Integer.valueOf(productNameCount)), category, Integer.valueOf(price), "Y".repeat(Integer.valueOf(sellerCount)));
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void productNameがnullのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest(null, "Electronics", 150000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"));
    }

    @Test
    public void productNameが空文字のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("", "Electronics", 150000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"),
                        tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが半角スペースのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest(" ", "Electronics", 150000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "入力してください"),
                        tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが2文字未満のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("p", "Electronics", 150000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void productNameが20文字超えのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("p".repeat(21), "Electronics", 150000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("productName", "2文字以上20文字以下である必要があります"));
    }

    @Test
    public void categoryがnullのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", null, 150000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "入力してください"));
    }

    @Test
    public void categoryが空白のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "", 150000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "無効なカテゴリです"),
                        tuple("category", "入力してください"));
    }

    @Test
    public void categoryが半角スペースのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", " ", 150000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "無効なカテゴリです"),
                        tuple("category", "入力してください"));
    }

    @Test
    public void categoryがElectronicsとClothingとBooks以外のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("Table", "Furniture", 150000, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("category", "無効なカテゴリです"));
    }

    @Test
    public void priceがnullのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", null, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("price", "入力してください"));
    }

    @Test
    public void priceが0以下のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 0, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("price", "0より大きい値である必要があります"));
    }

    @Test
    public void priceが1000000超えのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 1000001, "Yamada");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("price", "1000000以下である必要があります"));
    }

    @Test
    public void sellerがnullのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 150000, null);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("seller", "無効な販売者です"));
    }

    @Test
    public void sellerが空文字のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 150000, "");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("seller", "無効な販売者です"));
    }

    @Test
    public void sellerが半角スペースのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 150000, " ");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("seller", "無効な販売者です"));
    }

    @Test
    public void sellerが2文字未満のときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 150000, "Y".repeat(1));
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("seller", "無効な販売者です"));
    }

    @Test
    public void sellerが20文字超えのときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 150000, "Y".repeat(21));
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("seller", "無効な販売者です"));
    }

    @ParameterizedTest
    @CsvSource({
            "20, Electronics, 1000000, \uD868\uDC82",
            "20, Electronics, 1000000, 📷"
    })
    public void sellerに1文字のサロゲートペアの漢字と絵文字を渡したときにバリデーションエラーとならないこと(String productNameCount, String category, String price, String sellerCount) {
        ProductRequest productRequest = new ProductRequest("p".repeat(Integer.valueOf(productNameCount)), category, Integer.valueOf(price), sellerCount);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    public void sellerが19文字の通常文字と1文字のサロゲートペアの漢字を渡したときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 150000, "Y".repeat(19) + "\uD868\uDC82");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("seller", "無効な販売者です"));
    }

    @Test
    public void sellerが19文字の通常文字と絵文字を渡したときにバリデーションエラーとなること() {
        ProductRequest productRequest = new ProductRequest("iPhone15", "Electronics", 150000, "Y".repeat(19) + "📷");
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(tuple("seller", "無効な販売者です"));
    }
}
