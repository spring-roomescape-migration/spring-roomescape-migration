package roomescape.domain.theme.domain.repository;

import roomescape.domain.theme.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Long save(Theme theme);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    void delete(Theme theme);
}
