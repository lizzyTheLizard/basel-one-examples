package ch.baselone.springsecurity.example.web.protection;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class Controller {

    @GetMapping("csrf/none")
    public String noToken() {
        return """
            <html>
            <body>
            <form action="/csrf" method="POST">
              <label for="name">Name:</label><br>
              <input type="text" id="name" name="name" value="John"><br>
              <input type="submit" value="Submit">
            </form>
            </body>
            </html>
        """;
    }

    @GetMapping("csrf/token")
    public String token(CsrfToken t) {
        return """
            <html>
            <body>
            <form action="/csrf" method="POST">
              <label for="name">Name:</label><br>
              <input type="text" id="name" name="name" value="John"><br>
              <input type="hidden" name="%s" value="%s"/>
              <input type="submit" value="Submit">
            </form>
            </body>
            </html>
        """.formatted(t.getParameterName(), t.getToken());
    }

    @RequestMapping("csrf")
    public String form(@RequestBody MultiValueMap<String, String> formData) {
        return  "Hello " + formData.getFirst("name");
    }
}
