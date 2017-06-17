package com.belvedere.repository;

import com.belvedere.domain.Level;
import com.belvedere.domain.enumeration.LevelName;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Level entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LevelRepository extends JpaRepository<Level,Long> {

    Level findOneByName(LevelName name);
}
