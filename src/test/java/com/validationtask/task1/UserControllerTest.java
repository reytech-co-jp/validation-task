package com.validationtask.task1;

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
class UserControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;

    @Test
    public void usernameとpasswordとconfirmPasswordがnullの場合は400エラーとなること() throws Exception {
        UserPostRequest userPostRequest = new UserPostRequest(null, null, null);
        ResultActions actual = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(userPostRequest)));
        actual.andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "status":"BAD_REQUEST",
                        "message":"validation error",
                        "errors":[
                          {
                            "field":"username",
                            "message":"ユーザー名を入力してください"
                          },
                          {
                          "field":"password",
                          "message":"パスワードを入力してください"
                          },
                          {
                          "field":"passwordMatching",
                          "message":"パスワードと確認用パスワードが一致していません"
                          }
                         ]
                        }
                        """));
    }

    @Test
    public void usernameとpasswordとconfirmPasswordが空白の場合は400エラーとなること() throws Exception {
        UserPostRequest userPostRequest = new UserPostRequest("", "", "");
        ResultActions actual = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(userPostRequest)));
        actual.andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                        "status":"BAD_REQUEST",
                        "message":"validation error",
                        "errors":[
                          {
                            "field":"username",
                            "message":"ユーザー名を入力してください"
                          },
                          {
                            "field":"username",
                            "message":"ユーザー名は3文字以上20文字以下である必要があります"
                          },
                          {
                          "field":"password",
                          "message":"パスワードを入力してください"
                          },
                          {
                          "field":"password",
                          "message":"パスワードは8文字以上30文字以下である必要があります"
                          }
                         ]
                        }
                        """));
    }

    @Test
    public void usernameとpasswordとconfirmPasswordがデータが有効な場合はエラーとならないこと() throws Exception {
        UserPostRequest user = new UserPostRequest("Yamada", "password", "password");
        ResultActions actualResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(user)));
        actualResult.andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "message": "Successfully created"
                        }
                        """));
    }
}
