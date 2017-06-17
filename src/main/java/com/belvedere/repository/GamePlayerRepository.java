package com.belvedere.repository;

import com.belvedere.domain.GamePlayer;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the GamePlayer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer,Long> {

}
