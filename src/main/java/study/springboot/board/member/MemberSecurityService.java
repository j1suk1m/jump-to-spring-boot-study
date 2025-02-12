package study.springboot.board.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 유저 이름으로 유저 객체를 조회해 리턴
     * @param name 유저 이름
     * @return 스프링 시큐리티 유저 객체
     * @throws UsernameNotFoundException 유저 이름에 해당하는 데이터가 없는 경우 발생하는 예외
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByName(name);

        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("회원을 찾을 수 없습니다.");
        }

        Member member = optionalMember.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 권한 부여
        if ("admin".equals(name)) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.MEMBER.getValue()));
        }

        // 스프링 시큐리티에서 사용하는 유저 객체
        // 유저 이름, 비밀번호, 권한 리스트 전달
        return new User(member.getName(), member.getPassword(), authorities);
    }

}