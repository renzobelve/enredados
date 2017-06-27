package com.belvedere.service;

import com.belvedere.domain.Game;
import com.belvedere.domain.GamePlayer;
import com.belvedere.domain.Level;
import com.belvedere.domain.Player;
import com.belvedere.domain.Question;
import com.belvedere.domain.enumeration.LevelName;
import com.belvedere.domain.enumeration.QuestionType;
import com.belvedere.repository.GamePlayerRepository;
import com.belvedere.repository.LevelRepository;
import com.belvedere.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Player.
 */
@Service
@Transactional
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    
    private final GamePlayerRepository gamePlayerRepository;
    
    private final LevelRepository LevelRepository;
    

    public PlayerService(PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository, LevelRepository levelRepository) {
        this.playerRepository = playerRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.LevelRepository = levelRepository;
    }

    /**
     * Save a player.
     *
     * @param player the entity to save
     * @return the persisted entity
     */
    public Player save(Player player) {
        log.debug("Request to save Player : {}", player);
        return playerRepository.save(player);
    }

    /**
     *  Get all the players.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Player> findAll(Pageable pageable) {
        log.debug("Request to get all Players");
        return playerRepository.findAll(pageable);
    }

    /**
     *  Get one player by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Player findOne(Long id) {
        log.debug("Request to get Player : {}", id);
        return playerRepository.findOne(id);
    }

    /**
     *  Delete the  player by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Player : {}", id);
        playerRepository.delete(id);
    }
    
    /**
     *  Answer a Question and add points if is correct.
     *
     * @param gamePlayer
     * @param question
     * @param answer
     * @return 
     */
    public boolean answerQuestion(GamePlayer gamePlayer, Question question, String answer){
        Player player = gamePlayer.getPlayer();
        if(player.answerQuestion(question, answer)){
            if(question.getType().equals(QuestionType.IMAGE)){
                player.addPoints(Game.QUESTION_IMAGE_POINTS);
                gamePlayer.addPoints(Game.QUESTION_IMAGE_POINTS);
            }
            if(question.getType().equals(QuestionType.TEXT)){
                player.addPoints(Game.QUESTION_TEXT_POINTS);
                gamePlayer.addPoints(Game.QUESTION_TEXT_POINTS);
            }
            gamePlayer.correctQuestion();
            this.checkPlayerLevel(player);
            gamePlayerRepository.save(gamePlayer);
            playerRepository.save(player);
            return true;
        }else{
            playerRepository.save(player);
            return false;
        }
    }
    
    /**
     * Checks de current level of player and upgrade if possible.
     * @param player
     */
    private void checkPlayerLevel(Player player) {
        int points = player.getPoints();
        Level level = null;
        
        if(points >= 0 && points <= 100){
            level = LevelRepository.findOneByName(LevelName.BRONZE);
        }
        if(points >= 101 && points <= 500){
            level = LevelRepository.findOneByName(LevelName.SILVER);
        }
        if(points >= 501 && points <= 1000){
            level = LevelRepository.findOneByName(LevelName.GOLD);
        }
        
        player.setLevel(level);
    }
}
