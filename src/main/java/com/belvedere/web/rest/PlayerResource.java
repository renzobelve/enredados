package com.belvedere.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.belvedere.domain.Player;
import com.belvedere.service.PlayerService;

import com.belvedere.web.rest.util.HeaderUtil;
import com.belvedere.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * REST controller for managing Player.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    private static final String ENTITY_NAME = "player";

    private final PlayerService playerService;

    public PlayerResource(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * GET /players : get all the players.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of games in
     * body
     */
    @GetMapping("/players")
    @Timed
    public ResponseEntity<List<Player>> getAllPlayers(@ApiParam Pageable pageable) {
        Page<Player> page = playerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/players");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /players/:id : get the "id" player.
     *
     * @param id the id of the player to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the player,
     * or with status 404 (Not Found)
     */
    @GetMapping("/players/{id}")
    @Timed
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        Player player = playerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(player));
    }

}
