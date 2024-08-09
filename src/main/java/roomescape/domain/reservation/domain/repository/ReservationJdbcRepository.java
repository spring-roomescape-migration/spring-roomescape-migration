package roomescape.domain.reservation.domain.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.domain.Member;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private static final String FIND_BY_ID_SQL = """
            SELECT
                r.id AS reservation_id,
                r.name AS reservation_name,
                r.date AS reservation_date,
                rt.id AS reservation_time_id,
                rt.start_at AS reservation_time_start_at,
                t.id AS theme_id,
                t.name AS theme_name,
                t.description AS theme_description,
                t.thumbnail AS theme_thumbnail,
                m.id AS member_id,
                m.name AS member_name,
                m.email AS member_email,
                m.password AS member_password,
                m.role AS member_role
            FROM reservation r
            INNER JOIN reservation_time rt
                ON r.time_id = rt.id
            INNER JOIN theme t
                ON r.theme_id = t.id
            INNER JOIN member m
                ON r.member_id = m.id
            WHERE r.id = ?;
            """;
    private static final String SAVE_SQL = "INSERT INTO reservation (name, date, time_id, theme_id, member_id) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_ALL_SQL = """
            SELECT
                r.id AS reservation_id,
                r.name AS reservation_name,
                r.date AS reservation_date,
                rt.id AS reservation_time_id,
                rt.start_at AS reservation_time_start_at,
                t.id AS theme_id,
                t.name AS theme_name,
                t.description AS theme_description,
                t.thumbnail AS theme_thumbnail,
                m.id AS member_id,
                m.name AS member_name,
                m.email AS member_email,
                m.password AS member_password,
                m.role AS member_role
            FROM reservation r
            INNER JOIN reservation_time rt
                ON r.time_id = rt.id
            INNER JOIN theme t
                ON r.theme_id = t.id
            INNER JOIN member m
                ON r.member_id = m.id;
            """;

    private static final String FIND_BY_MEMBER_ID_SQL = """
            SELECT
                r.id AS reservation_id,
                r.name AS reservation_name,
                r.date AS reservation_date,
                rt.id AS reservation_time_id,
                rt.start_at AS reservation_time_start_at,
                t.id AS theme_id,
                t.name AS theme_name,
                t.description AS theme_description,
                t.thumbnail AS theme_thumbnail,
                m.id AS member_id,
                m.name AS member_name,
                m.email AS member_email,
                m.password AS member_password,
                m.role AS member_role
            FROM reservation r
            INNER JOIN reservation_time rt
                ON r.time_id = rt.id
            INNER JOIN theme t
                ON r.theme_id = t.id
            INNER JOIN member m
                ON r.member_id = m.id
            WHERE m.id = ?;
            """;
    private static final String DELETE_SQL = "DELETE FROM reservation WHERE id = ?;";
    private static final String ID = "id";
    private static final String RESERVATION_ID = "reservation_id";
    private static final String RESERVATION_NAME = "reservation_name";
    private static final String RESERVATION_DATE = "reservation_date";
    private static final String RESERVATION_STATUS = "reservation_status";
    private static final String TIME_ID = "reservation_time_id";
    private static final String TIME_START_AT = "reservation_time_start_at";
    private static final String THEME_ID = "theme_id";
    private static final String THEME_NAME = "theme_name";
    private static final String THEME_DESCRIPTION = "theme_description";
    private static final String THEME_THUMBNAIL = "theme_thumbnail";
    private static final String MEMBER_ID = "member_id";
    private static final String MEMBER_NAME = "member_name";
    private static final String MEMBER_EMAIL = "member_email";
    private static final String MEMBER_PASSWORD = "member_password";
    private static final String MEMBER_ROLE = "member_role";

    private final JdbcTemplate jdbcTemplate;

    public ReservationJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Reservation> findById(long reservationId) {
        return Optional.of(jdbcTemplate.queryForObject(FIND_BY_ID_SQL, reservationRowMapper(), reservationId));
    }

    @Override
    public Long save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    SAVE_SQL,
                    new String[]{ID}
            );
            preparedStatement.setString(1, reservation.getName());
            preparedStatement.setString(2, reservation.getDate());
            preparedStatement.setLong(3, reservation.getTime().getId());
            preparedStatement.setLong(4, reservation.getTheme().getId());
            preparedStatement.setLong(5, reservation.getMember().getId());
            return preparedStatement;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, reservationRowMapper());
    }

    @Override
    public List<Reservation> findAllByMemberId(Long id) {
        return jdbcTemplate.query(FIND_BY_MEMBER_ID_SQL, reservationRowMapper(), id);
    }

    @Override
    public void delete(Reservation reservation) {
        jdbcTemplate.update(DELETE_SQL, reservation.getId());
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) ->
                new Reservation(
                        rs.getLong(RESERVATION_ID),
                        rs.getString(RESERVATION_NAME),
                        rs.getString(RESERVATION_DATE),
                        rs.getString(RESERVATION_STATUS),
                        new Theme(
                                rs.getLong(THEME_ID),
                                rs.getString(THEME_NAME),
                                rs.getString(THEME_DESCRIPTION),
                                rs.getString(THEME_THUMBNAIL)
                        ),
                        new Time(
                                rs.getLong(TIME_ID),
                                rs.getString(TIME_START_AT)),
                        new Member(
                                rs.getLong(MEMBER_ID),
                                rs.getString(MEMBER_NAME),
                                rs.getString(MEMBER_EMAIL),
                                rs.getString(MEMBER_PASSWORD),
                                rs.getString(MEMBER_ROLE)
                        ));
    }
}
