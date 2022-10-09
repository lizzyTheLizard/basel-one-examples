package ch.baselone.springsecurity.example;

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
    @SuppressWarnings("EmptyMethod")
    void contextLoads() {
    }

    @Test
    void csrf() throws Exception {
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
                .content("name=Name&_csrf=" + token)
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

    @Test
    void nocsrf() throws Exception {
        MockHttpServletRequestBuilder request2 = MockMvcRequestBuilders
                .post("/csrf")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
        mvc.perform(request2)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}