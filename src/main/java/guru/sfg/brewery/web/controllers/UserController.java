package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/register2fa")
    public String register2fa(Model model){
        model.addAttribute("googleUrl", "to do");
        return "user/register2fa";
    }

    @PostMapping
    public String confirm2fa(@PathVariable Integer verifyCode){
        // to do impl
        return "index";
    }

}
