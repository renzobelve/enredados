package com.belvedere.repository;

import com.belvedere.domain.Game;
import com.belvedere.domain.enumeration.GameState;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Game entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameRepository extends JpaRepository<Game,Long> {

    @Query("select distinct game from Game game left join fetch game.questions")
    List<Game> findAllWithEagerRelationships();

    @Query("select game from Game game left join fetch game.questions where game.id =:id")
    Game findOneWithEagerRelationships(@Param("id") Long id);
    
    Page<Game> findByState(Pageable pageable, GameState state);

}
