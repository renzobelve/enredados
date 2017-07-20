package com.belvedere.service;

import com.belvedere.domain.Game;
import com.belvedere.domain.Player;
import com.belvedere.domain.Question;
import com.belvedere.domain.enumeration.GameState;
import com.belvedere.domain.enumeration.LevelName;
import com.belvedere.domain.enumeration.QuestionDificulty;
import com.belvedere.domain.enumeration.QuestionType;
import com.belvedere.domain.exception.GameActiveException;
import com.belvedere.domain.exception.GameFullException;
import com.belvedere.domain.exception.PlayerInGameException;
import com.belvedere.domain.exception.PlayerNotInGameException;
import com.belvedere.domain.exception.PlayerNotInLevelException;
import com.belvedere.repository.GameRepository;
import com.belvedere.repository.QuestionRepository;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Game.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    private final QuestionRepository questionRepository;

    public GameService(GameRepository gameRepository, QuestionRepository questionRepository) {
        this.gameRepository = gameRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Save a game.
     *
     * @param game the entity to save
     * @param creator
     * @return the persisted entity
     */
    public Game save(Game game, Player creator) {
        // Se obtienen preguntas segun el nivel del creador
        LevelName level = creator.getLevel().getName();
        List<Question> imageQuestions = null;
        List<Question> textQuestions = null;
        switch (level) {
            case BRONZE:
                imageQuestions = questionRepository.findByTypeAndDificulty(QuestionType.IMAGE, QuestionDificulty.EASY);
                textQuestions = questionRepository.findByTypeAndDificulty(QuestionType.TEXT, QuestionDificulty.EASY);
                break;
            case SILVER:
                imageQuestions = questionRepository.findByTypeAndDificulty(QuestionType.IMAGE, QuestionDificulty.MEDIUM);
                textQuestions = questionRepository.findByTypeAndDificulty(QuestionType.TEXT, QuestionDificulty.MEDIUM);
                break;
            case GOLD:
                imageQuestions = questionRepository.findByTypeAndDificulty(QuestionType.IMAGE, QuestionDificulty.HARD);
                textQuestions = questionRepository.findByTypeAndDificulty(QuestionType.TEXT, QuestionDificulty.HARD);
                break;

        }
        // Se organizan las preguntas
        for (int i = 0; i < game.getSize() / 2; i++) {
            Random randomNumber = new Random();
            int randomImageQuestion = randomNumber.nextInt(imageQuestions.size());
            int randomTextQuestion = randomNumber.nextInt(textQuestions.size());
            Question imageQuestion = imageQuestions.get(randomImageQuestion);
            Question textQuestion = textQuestions.get(randomTextQuestion);
            game.addQuestions(imageQuestion);
            game.addQuestions(textQuestion);
            imageQuestions.remove(imageQuestion);
            textQuestions.remove(textQuestion);
        }
        return gameRepository.save(game);
    }

    /**
     * Get all the games.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Game> findAll(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }
    
    /**
     * Get all OPEN games.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Game> findOpen(Pageable pageable) {
        return gameRepository.findByState(pageable, GameState.OPEN);
    }

    /**
     * Get one game by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Game findOne(Long id) {
        return gameRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the game by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        gameRepository.delete(id);
    }

    /**
     * Add a player to a game.
     *
     * @param game
     * @param player
     * @return
     * @throws com.belvedere.domain.exception.GameFullException
     * @throws com.belvedere.domain.exception.PlayerInGameException
     * @throws com.belvedere.domain.exception.PlayerNotInLevelException
     */
    public Game addPlayer(Game game, Player player) throws GameFullException, PlayerInGameException, PlayerNotInLevelException {
        game.addPlayer(player);
        return gameRepository.save(game);
    }

    /**
     * Remove a player from a game.
     *
     * @param game
     * @param player
     * @return
     * @throws com.belvedere.domain.exception.GameActiveException
     * @throws com.belvedere.domain.exception.PlayerNotInGameException
     */
    public Game removePlayer(Game game, Player player) throws GameActiveException, PlayerNotInGameException {
        game.removePlayer(player);
        return gameRepository.save(game);
    }

    /**
     * Next Player turn of game.
     *
     * @param game
     * @return
     */
    public Game nextPlayerTurn(Game game) {
        // Arranca el proximo turno si quedan preguntas en el juego y sino lo finaliza
        if (!game.getActiveQuestion().equals(game.getQuestions().size())) {
            game.nextPlayerTurn();
        } else {
            game.finishGame();
        }
        return gameRepository.save(game);
    }
}
