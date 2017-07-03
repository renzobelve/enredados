package com.belvedere.repository;

import com.belvedere.domain.Player;
import com.belvedere.domain.User;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Player entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {

    Player findOneByUser(User user);
}
