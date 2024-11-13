package roomescape.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.domain.Role;
import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;
import roomescape.domain.member.service.MemberService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InterceptorTest {

    private static final String TOKEN = "token";
    private static final String VALID_TOKEN = "valid-token";
    private static final String NAME = "박민욱";
    private static final String EMAIL = "uo5234@naver.com";
    private static final String PASSWORD = "Password12!#";

    private RoleCheckInterceptor roleCheckInterceptor;
    private MemberService memberService;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        memberService = mock(MemberService.class);
        roleCheckInterceptor = new RoleCheckInterceptor(memberService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    /**
     * given : 서비스, 요청 값 모킹 => 모킹된 쿠키를 전달하면 모킹된 관리자 권한의 사용자 세팅
     * when, then : 어드민 페이지에 필요한 쿠키 관련 인터셉터의 통과를 검증할 수 있다.
     */
    @Test
    void 쿠키와_관리자_권한의_사용자를_모킹해서_인증과_인가가_필요한_인터셉터를_통과한다() throws Exception {

        //given
        Cookie cookie = new Cookie(TOKEN, VALID_TOKEN);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        Member adminMember = new Member(1L, NAME, EMAIL, PASSWORD, Role.USER.getRole());
        adminMember.grantAdminRole();
        when(memberService.findByToken(VALID_TOKEN)).thenReturn(adminMember);

        //when then
        Assertions.assertThat(roleCheckInterceptor.preHandle(request, response, new Object())).isTrue();


    }

    /**
     * given : 서비스, 요청 값 모킹 => 모킹된 쿠키를 전달하면 모킹된 기본 권한의 사용자 세팅
     * when, then : 어드민 페이지에 필요한 쿠키 관련 인터셉터를 통과하지 못하고 권한 필요에 관한 예외가 발생한다.
     */
    @Test
    void 쿠키와_기본_권한의_사용자를_모킹해서_인증과_인가가_필요한_인터셉터를_통과히지_못하고_권한_예외가_발생한다() {

        // given
        Cookie cookie = new Cookie(TOKEN, VALID_TOKEN);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        Member regularMember = new Member(1L, NAME, EMAIL, PASSWORD, Role.USER.getRole());
        when(memberService.findByToken(VALID_TOKEN)).thenReturn(regularMember);

        // then
        Assertions.assertThatThrownBy(() -> roleCheckInterceptor.preHandle(request, response, new Object())).isInstanceOf(MemberException.class).hasMessage(MemberErrorCode.UNAUTHORIZED_ERROR.getErrorMessage());
    }

    /**
     * given : 요청 값 모킹 => 빈 쿠키 모킹 세팅
     * when, then : 어드민 페이지에 필요한 쿠키 관련 인터셉터를 통과하지 못하고 쿠키 관련 예외가 발생한다.
     */
    @Test
    void 빈_쿠키를_모킹해서_인증과_인가가_필요한_인터셉터를_통과시킬_때_쿠키_관련_예외가_발생한다() {

        // given
        when(request.getCookies()).thenReturn(new Cookie[]{});

        // when, then
        Assertions.assertThatThrownBy(() -> roleCheckInterceptor.preHandle(request, response, new Object())).isInstanceOf(MemberException.class).hasMessage(MemberErrorCode.NOT_FOUND_COOKIE_ERROR.getErrorMessage());
    }
}
