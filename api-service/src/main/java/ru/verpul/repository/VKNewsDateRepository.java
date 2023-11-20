package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.VKNewsDate;

import java.util.Optional;

public interface VKNewsDateRepository extends JpaRepository<VKNewsDate, Long> {

    @Query(value = "SELECT * FROM vk_news_date LIMIT 1", nativeQuery = true)
    Optional<VKNewsDate> getNewsDate();

}
