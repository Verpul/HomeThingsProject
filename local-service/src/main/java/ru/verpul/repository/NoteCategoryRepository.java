package ru.verpul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.verpul.model.NoteCategory;

public interface NoteCategoryRepository extends JpaRepository<NoteCategory, Long> {

    @Query("SELECT nc.id FROM NoteCategory nc WHERE nc.title = :title")
    Long findByTitle(String title);
}
