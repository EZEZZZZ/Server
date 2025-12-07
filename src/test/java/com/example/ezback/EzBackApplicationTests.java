package com.example.ezback;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class EzBackApplicationTests {

    @Autowired
    private WebApplicationContext context;

    @Test
    void contextLoads() {
    }

    @Test
    void swaggerDocsEndpointShouldReturnOk() {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        try {
            var mvcResult = mockMvc.perform(get("/v3/api-docs"))
                    .andDo(print())
                    .andReturn();

            if (mvcResult.getResponse().getStatus() != 200) {
                var resolved = mvcResult.getResolvedException();
                String message = "Swagger docs 실패 - status=" + mvcResult.getResponse().getStatus()
                        + ", exception=" + (resolved != null ? resolved.getClass().getName() + ": " + resolved.getMessage() : "none")
                        + ", body=" + mvcResult.getResponse().getContentAsString();
                throw new AssertionError(message);
            }
        } catch (Exception e) {
            throw new AssertionError("Swagger docs 요청이 실패했습니다", e);
        }
    }

}
