package ch.baselone.springsecurity.example.web.protection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

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
    void contextLoads() {
    }

    @Test
    void corsNotAllowed() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .options("https://url.com/csrf/none")
                .header("Origin", "https://test.com")
                .header("Access-Control-Request-Method", "GET");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void corsAllowed() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .options("https://url.com/csrf/none")
                .header("Origin", "https://example.com")
                .header("Access-Control-Request-Method", "GET");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Origin", "https://example.com"));
    }

    @Test
    void contentSecurity() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .options("/csrf/none");
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("X-Frame-Options", "DENY"));
    }

    @Test
    void csrf()  throws Exception{
        MockHttpServletRequestBuilder request1 = MockMvcRequestBuilders
                .get("/csrf/token");
        MvcResult result = mvc.perform(request1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        String token = getToken(result.getResponse().getContentAsString());

        MockHttpServletRequestBuilder request2 = MockMvcRequestBuilders
                .post("/csrf")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("name=Name&_csrf="+token)
                .session(Objects.requireNonNull(session));
        mvc.perform(request2)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello Name"));
    }

    private String getToken(String form) {
        int startPosition = form.indexOf("name=\"_csrf\" value=\"") + 20;
        String formEnd = form.substring(startPosition);
        int end = formEnd.indexOf("\"");
        return formEnd.substring(0, end);
    }
}