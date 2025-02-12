package study.springboot.board.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member createMember(String name, String password, String email) {
        Member member = new Member(name, passwordEncoder.encode(password), email);
        memberRepository.save(member);
        return member;
    }

}