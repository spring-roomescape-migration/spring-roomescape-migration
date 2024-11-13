package roomescape.domain.member.api.signup;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class ApiSignupController {

    @GetMapping
    public String signup() {
        return "signup";
    }
}
