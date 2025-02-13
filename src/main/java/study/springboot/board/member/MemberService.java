package study.springboot.board.member;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.springboot.board.DataNotFoundException;

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

    public Member findMember(String name) {
        Optional<Member> optionalMember = memberRepository.findByName(name);

        if (optionalMember.isPresent()) {
            return optionalMember.get();
        }

        throw new DataNotFoundException("Site User Not Found");
    }

}