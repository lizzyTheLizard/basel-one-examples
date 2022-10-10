package ch.baselone.springsecurity.example;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccessITests {
    private final MockMvc mvc;

    AccessITests(WebApplicationContext context) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @SuppressWarnings("EmptyMethod")
    void contextLoads() {
    }

    @Test
    @WithMockUser(username = "user")
    void readUser() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/entities");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json("[{\"name\": \"zero\"}]"));
    }

    @Test
    @WithMockUser(username = "admin")
    void readAdmin() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/entities");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json("[" +
                        "{\"name\": \"zero\",\"permission\":{\"user\":[\"READ\"],\"admin\": [\"READ\",\"WRITE\"]}}, " +
                        "{\"name\": \"one\",\"permission\":{\"admin\": [\"READ\",\"WRITE\"]}}" +
                        "]"));
    }

    @Test
    @WithMockUser(username = "user")
    void readSingleFailed() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/entities/1");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin")
    void readSingleAllowed() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/entities/1");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json("{\"name\": \"one\",\"permission\":{\"admin\": [\"READ\",\"WRITE\"]}}"));
    }

    @Test
    @WithMockUser(username = "user")
    void updateFailed() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/entities/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"zero2\",\"permission\":{\"user\":[\"READ\"],\"admin\": [\"READ\",\"WRITE\"]}}")
                .with(csrf());
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin")
    void updateAllowed() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/entities/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"zero2\",\"permission\":{\"user\":[\"READ\"],\"admin\": [\"READ\",\"WRITE\"]}}")
                .with(csrf());
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        MockHttpServletRequestBuilder request2 = MockMvcRequestBuilders
                .get("/entities/0");
        mvc.perform(request2)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json("{\"name\": \"zero2\"}"));
    }
}