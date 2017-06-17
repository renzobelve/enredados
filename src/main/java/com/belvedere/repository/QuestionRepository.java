package com.belvedere.repository;

import com.belvedere.domain.Question;
import com.belvedere.domain.enumeration.QuestionDificulty;
import com.belvedere.domain.enumeration.QuestionType;
import java.util.List;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    
    List<Question> findByTypeAndDificulty(QuestionType type, QuestionDificulty dificulty);

}
