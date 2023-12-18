package com.validationtask.task2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ProductControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;

    @Test
    public void productNameとcategoryが空白でpriceが0の場合は400エラーとなること() throws Exception {
        ProductRequest productRequest = new ProductRequest("", "", 0);
        ResultActions actual = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(productRequest)));
        actual.andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                           "status": "BAD_REQUEST",
                           "message": "validation error",
                           "errors": [
                              {
                                 "field": "productName",
                                 "message": "入力してください"
                              },
                              {
                                 "field": "productName",
                                 "message": "2文字以上20文字以下である必要があります"
                              },
                              {
                                 "field": "category",
                                 "message": "入力してください"
                              },
                              {
                                 "field": "category",
                                 "message": "無効なカテゴリです"
                              },
                              {
                                 "field": "price",
                                 "message": "0より大きい値である必要があります"
                              }
                           ]
                        }
                        """));
    }

    @Test
    public void productNameとcategoryとpriceがnullの場合は400エラーとなること() throws Exception {
        ProductRequest productRequest = new ProductRequest(null, null, null);
        ResultActions actual = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(productRequest)));
        actual.andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                           "status": "BAD_REQUEST",
                           "message": "validation error",
                           "errors": [
                              {
                                 "field": "productName",
                                 "message": "入力してください"
                              },
                              {
                                 "field": "category",
                                 "message": "入力してください"
                              },
                              {
                                 "field": "category",
                                 "message": "無効なカテゴリです"
                              },
                              {
                                 "field": "price",
                                 "message": "入力してください"
                              }
                           ]
                        }
                        """));
    }

    @Test
    public void productNameとcategoryとpriceが有効な場合は400エラーとならないこと() throws Exception {
        ProductRequest productRequest = new ProductRequest("iphone15", "Electronics", 150000);
        ResultActions actual = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(productRequest)));
        actual.andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                           "message": "Successfully created"
                        }
                        """));
    }
}
