package roomescape.unit.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.domain.Role;

public class MemberTest {

    private Member member1;
    private Member member2;
    private Member member3;

    @BeforeEach
    void setUp() {
        member1 = new Member(1L, "John Doe", "john@example.com", "password123", Role.USER.getRole());
        member2 = new Member(1L, "Jane Doe", "jane@example.com", "password456", Role.USER.getRole());
        member3 = new Member(2L, "Jim Doe", "jim@example.com", "password789", Role.USER.getRole());
    }

    @Test
    void testEquals_SameObject() {
        Assertions.assertThat(member1).isEqualTo(member1);
    }

    @Test
    void testEquals_NullObject() {
        Assertions.assertThat(member1).isNotEqualTo(null);
    }

    @Test
    void testEquals_DifferentClass() {
        Assertions.assertThat(member1).isNotEqualTo(new Object());
    }

    @Test
    void testEquals_SameId() {
        Assertions.assertThat(member1).isEqualTo(member2);
    }

    @Test
    void testEquals_DifferentId() {
        Assertions.assertThat(member1).isNotEqualTo(member3);
    }

    @Test
    void testHashCode_SameId() {
        Assertions.assertThat(member1.hashCode()).isEqualTo(member2.hashCode());
    }

    @Test
    void testHashCode_DifferentId() {
        Assertions.assertThat(member1.hashCode()).isNotEqualTo(member3.hashCode());
    }
}
