package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.VKToken;

public interface VKTokenRepository extends JpaRepository<VKToken, Long> {

    @Query(value = "SELECT * FROM vk_token LIMIT 1", nativeQuery = true)
    VKToken getVKToken();

}
