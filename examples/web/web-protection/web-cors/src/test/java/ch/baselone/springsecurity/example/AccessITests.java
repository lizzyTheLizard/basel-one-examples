package ch.baselone.springsecurity.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
    void corsNotAllowed() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .options("https://url.com/hello")
                .header("Origin", "https://test.com")
                .header("Access-Control-Request-Method", "GET");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void corsAllowed() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .options("https://url.com/hello")
                .header("Origin", "https://example.com")
                .header("Access-Control-Request-Method", "GET");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Origin", "https://example.com"));
    }
}