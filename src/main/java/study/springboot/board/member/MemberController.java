package study.springboot.board.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join(MemberCreateForm memberCreateForm) {
        return "join_form";
    }

    @PostMapping("/join")
    public String join(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "join_form";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "Incorrect Password",
                    "2개의 비밀번호가 일치하지 않습니다.");

            return "join_form";
        }

        try {
            memberService.createMember(memberCreateForm.getName(), memberCreateForm.getPassword1(),
                    memberCreateForm.getEmail());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("Join Failed", "이미 등록된 회원입니다.");

            return "join_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("Join Failed", e.getMessage());

            return "join_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

}