package study.springboot.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String handleRootRedirect() {
        return "redirect:/question/list";
    }

}