package ch.baselone.springsecurity.example;


import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
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
    void notAuthenticated() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/hello");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void authenticated() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/hello");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(StringStartsWith.startsWith("Hello ")));
    }
}