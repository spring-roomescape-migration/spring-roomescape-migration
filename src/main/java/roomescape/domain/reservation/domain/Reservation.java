package roomescape.domain.reservation.domain;

import jakarta.persistence.*;
import roomescape.domain.member.domain.Member;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

import java.util.Objects;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String date;
    private String status;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "time_id")
    private Time time;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Reservation(Long id, String name, String date, String status, Theme theme, Time time, Member member) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
        this.member = member;
        this.status = status;
    }

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public Theme getTheme() {
        return theme;
    }

    public Time getTime() {
        return time;
    }

    public Member getMember() {
        return member;
    }

    public void addMember(Member member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
