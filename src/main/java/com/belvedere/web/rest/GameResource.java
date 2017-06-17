package com.belvedere.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.belvedere.domain.Game;
import com.belvedere.domain.Player;
import com.belvedere.domain.Question;
import com.belvedere.domain.exception.GameFullException;
import com.belvedere.domain.exception.PlayerInGameException;
import com.belvedere.domain.exception.PlayerNotInLevelException;
import com.belvedere.service.GameService;
import com.belvedere.service.PlayerService;
import com.belvedere.service.dto.GameDTO;
import com.belvedere.service.dto.PlayerDTO;
import com.belvedere.web.rest.util.HeaderUtil;
import com.belvedere.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * REST controller for managing Game.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

    private static final String ENTITY_NAME = "game";

    private final GameService gameService;

    private final PlayerService playerService;

    public GameResource(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    /**
     * POST /games : Create a new game.
     *
     * @param gameDTO
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new game, or with status 400 (Bad Request) if the game has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/games")
    @Timed
    public ResponseEntity<Game> createGame(@Valid @RequestBody GameDTO gameDTO) throws URISyntaxException {
        try {
            // Se busca al creador
            Player creator = playerService.findOne(gameDTO.getCreatorID());
            // Se crea el juego
            Game game = new Game(creator, gameDTO.getMaxPlayers());
            // Se utiliza el servicio para persistir el juego
            Game result = gameService.save(game, creator);

            return ResponseEntity.created(new URI("/api/games/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                    .body(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "error", ex.getMessage())).body(null);
        }
    }

    /**
     * GET /games : get all the games.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of games in
     * body
     */
    @GetMapping("/games")
    @Timed
    public ResponseEntity<List<Game>> getAllGames(@ApiParam Pageable pageable) {
        Page<Game> page = gameService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/games");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /games/:id : get the "id" game.
     *
     * @param id the id of the game to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the game,
     * or with status 404 (Not Found)
     */
    @GetMapping("/games/{id}")
    @Timed
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        Game game = gameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(game));
    }

    /**
     * DELETE /games/:id : delete the "id" game.
     *
     * @param id the id of the game to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/games/{id}")
    @Timed
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST /games/add-player : Add a player to a game.
     *
     * @param gameID
     * @param playerID
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new game, or with status 400 (Bad Request) if the game has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/games/add-player")
    @Timed
    public ResponseEntity<Game> addPlayer(@RequestParam(value = "gameID") String gameID, @RequestParam(value = "playerID") String playerID) throws URISyntaxException {
        try {
            // Se busca al juego
            Game game = gameService.findOne(Long.parseLong(gameID));
            // Se busca al jugador
            Player player = playerService.findOne(Long.parseLong(playerID));
            // Se utiliza el servicio para agregar un jugador
            Game result = gameService.addPlayer(game, player);

            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);

        } catch (Exception ex) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "error", ex.getMessage())).body(null);
        }

    }

    /**
     * POST /games/remove-player : Remove a player from a game.
     *
     * @param gameID
     * @param playerID
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new game, or with status 400 (Bad Request) if the game has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/games/remove-player")
    @Timed
    public ResponseEntity<Game> removePlayer(@RequestParam(value = "gameID") String gameID, @RequestParam(value = "playerID") String playerID) throws URISyntaxException {
        try {
            // Se busca al juego
            Game game = gameService.findOne(Long.parseLong(gameID));
            // Se busca al jugador
            Player player = playerService.findOne(Long.parseLong(playerID));
            // Se utiliza el servicio para agregar un jugador
            Game result = gameService.removePlayer(game, player);

            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString())).body(result);

        } catch (Exception ex) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "error", ex.getMessage())).body(null);
        }
    }

    /**
     * GET /games/question/:id : get the actual question of game.
     *
     * @param id the id of the game to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the game,
     * or with status 404 (Not Found)
     */
    @GetMapping("/games/question/{id}")
    @Timed
    public ResponseEntity<Question> activeQuestion(@PathVariable Long id) {
        try {
            // Se busca al juego
            Game game = gameService.findOne(id);
            // Se busca la pregunta actual
            Question question = game.activeQuestion();

            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(question));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "error", ex.getMessage())).body(null);
        }

    }
    
    /**
     * GET /games/player/:id : get the actual player of game.
     *
     * @param id the id of the game to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the game,
     * or with status 404 (Not Found)
     */
    @GetMapping("/games/player/{id}")
    @Timed
    public ResponseEntity<Player> activePlayer(@PathVariable Long id) {
        try {
            // Se busca al juego
            Game game = gameService.findOne(id);
            // Se busca al jugador actual
            Player player = game.activePlayer();

            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(player));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "error", ex.getMessage())).body(null);
        }

    }
}
