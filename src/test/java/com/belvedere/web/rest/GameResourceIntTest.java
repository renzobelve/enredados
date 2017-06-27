package com.belvedere.web.rest;

import com.belvedere.EnredadosApp;

import com.belvedere.domain.Game;
import com.belvedere.domain.Level;
import com.belvedere.domain.Player;
import com.belvedere.domain.Question;
import com.belvedere.domain.User;
import com.belvedere.repository.GameRepository;
import com.belvedere.service.GameService;
import com.belvedere.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.belvedere.domain.enumeration.GameState;
import com.belvedere.domain.enumeration.LevelName;
import com.belvedere.domain.exception.GameFullException;
import com.belvedere.domain.exception.PlayerInGameException;
import com.belvedere.domain.exception.PlayerNotInLevelException;
import com.belvedere.repository.GamePlayerRepository;
import com.belvedere.repository.LevelRepository;
import com.belvedere.repository.PlayerRepository;
import com.belvedere.repository.UserRepository;
import com.belvedere.service.PlayerService;
import com.belvedere.service.QuestionService;
import com.belvedere.service.dto.GameDTO;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hamcrest.core.IsNull;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for the GameResource REST controller.
 *
 * @see GameResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnredadosApp.class)
public class GameResourceIntTest {

    private static final GameState DEFAULT_STATE = GameState.OPEN;
    private static final GameState UPDATED_STATE = GameState.ACTIVE;

    private static final Integer DEFAULT_MAX_PLAYERS = 2;
    private static final Integer UPDATED_MAX_PLAYERS = 4;

    private static final Integer DEFAULT_SIZE = 8;

    private static final Integer DEFAULT_ACTIVE_PLAYER = 0;
    private static final Integer UPDATED_ACTIVE_PLAYER = 1;

    private static final Integer DEFAULT_ACTIVE_QUESTION = 0;
    private static final Integer UPDATED_ACTIVE_QUESTION = 1;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LevelRepository levelRepository;
    
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;
    
    @Autowired
    private QuestionService questionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGameMockMvc;
    
    private MockMvc restPlayerMockMvc;

    private Game game;

    private static Player creator;

    private static Player player;

    private static User user;

    private static User user2;

    private static Level level;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameResource gameResource = new GameResource(gameService, playerService);
        this.restGameMockMvc = MockMvcBuilders.standaloneSetup(gameResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
        
        PlayerResource playerResource = new PlayerResource(playerService, questionService, gameService, gamePlayerRepository);
        this.restPlayerMockMvc = MockMvcBuilders.standaloneSetup(playerResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createEntity(EntityManager em) {
        Game game = null;
        try {
            game = new Game(creator, DEFAULT_MAX_PLAYERS);
        } catch (GameFullException | PlayerInGameException | PlayerNotInLevelException ex) {
            System.out.println(ex.getMessage());
        }
        return game;
    }

    public static Player createPlayer(EntityManager em, User user) {
        Player player = new Player(user, level);
        return player;
    }

    public static Level createLevel(EntityManager em) {
        Level level = new Level();
        level.setName(LevelName.BRONZE);
        level.setMinPoints(0);
        level.setMaxPoints(100);
        return level;
    }

    @Before
    public void initTest() {
        level = createLevel(em);
        user = UserResourceIntTest.createEntity(em);
        user2 = UserResourceIntTest.createEntity(em);
        user2.setLogin("user");
        user2.setEmail("user@user.com");
        creator = createPlayer(em, user);
        player = createPlayer(em, user2);
        game = createEntity(em);
    }

    @Test
    @Transactional
    public void createGame() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        playerRepository.saveAndFlush(creator);

        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        GameDTO gameDTO = new GameDTO();
        gameDTO.setCreatorID(creator.getId());
        gameDTO.setMaxPlayers(DEFAULT_MAX_PLAYERS);

        // Create the Game
        restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
                .andExpect(status().isCreated());
        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testGame.getMaxPlayers()).isEqualTo(DEFAULT_MAX_PLAYERS);
        assertThat(testGame.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testGame.getActivePlayer()).isEqualTo(null);
        assertThat(testGame.getActiveQuestion()).isEqualTo(null);
        assertThat(testGame.getFirstPlayer()).isEqualTo(null);
        assertThat(testGame.getQuestions().size()).isEqualTo(DEFAULT_SIZE);

    }

    @Test
    @Transactional
    public void getAllGames() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        playerRepository.saveAndFlush(creator);
        gameRepository.saveAndFlush(game);

        // Get all the gameList
        restGameMockMvc.perform(get("/api/games?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].maxPlayers").value(hasItem(DEFAULT_MAX_PLAYERS)))
                .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
                .andExpect(jsonPath("$.[*].activePlayer").value(hasItem(IsNull.nullValue())))
                .andExpect(jsonPath("$.[*].activeQuestion").value(hasItem(IsNull.nullValue())))
                .andExpect(jsonPath("$.[*].firstPlayer").value(hasItem(IsNull.nullValue())));
    }

    @Test
    @Transactional
    public void getGame() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        playerRepository.saveAndFlush(creator);
        gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", game.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(game.getId().intValue()))
                .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
                .andExpect(jsonPath("$.maxPlayers").value(DEFAULT_MAX_PLAYERS))
                .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
                .andExpect(jsonPath("$.activePlayer").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activeQuestion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.firstPlayer").value(IsNull.nullValue()));
    }

    @Test
    @Transactional
    public void deleteGame() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        playerRepository.saveAndFlush(creator);
        gameService.save(game, creator);

        int databaseSizeBeforeDelete = gameRepository.findAll().size();

        // Get the game
        restGameMockMvc.perform(delete("/api/games/{id}", game.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void addPlayer() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(user2);
        playerRepository.saveAndFlush(creator);
        playerRepository.saveAndFlush(player);
        gameRepository.saveAndFlush(game);

        // Add Player
        restGameMockMvc.perform(post("/api/games/add-player")
                .param("gameID", Long.toString(game.getId()))
                .param("playerID", Long.toString(player.getId())))
                .andExpect(status().isOk());

        assertThat(game.getGamePlayers().size()).isEqualTo(2);
        assertThat(game.getState()).isEqualTo(GameState.ACTIVE);
    }

    @Test
    @Transactional
    public void removePlayer() throws Exception {
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(user2);
        playerRepository.saveAndFlush(creator);
        playerRepository.saveAndFlush(player);
        game.setMaxPlayers(4);
        gameRepository.saveAndFlush(game);

        assertThat(game.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(game.getGamePlayers().size()).isEqualTo(1);

        // Add Player
        restGameMockMvc.perform(post("/api/games/add-player")
                .param("gameID", Long.toString(game.getId()))
                .param("playerID", Long.toString(player.getId())))
                .andExpect(status().isOk());

        assertThat(game.getGamePlayers().size()).isEqualTo(2);

        // Remove Player
        restGameMockMvc.perform(post("/api/games/remove-player")
                .param("gameID", Long.toString(game.getId()))
                .param("playerID", Long.toString(player.getId())))
                .andExpect(status().isOk());

        assertThat(game.getGamePlayers().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void activeQuestion() throws Exception {
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(user2);
        playerRepository.saveAndFlush(creator);
        playerRepository.saveAndFlush(player);

        GameDTO gameDTO = new GameDTO();
        gameDTO.setCreatorID(creator.getId());
        gameDTO.setMaxPlayers(DEFAULT_MAX_PLAYERS);

        // Create the Game
        MvcResult gameResult = restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = gameResult.getResponse().getHeader("Location");
        Pattern pattern = Pattern.compile("(\\d+)$");
        Matcher matcher = pattern.matcher(location);
        matcher.find();
        Long id = Long.parseLong(matcher.group(), 10);

        // Add Player
        restGameMockMvc.perform(post("/api/games/add-player")
                .param("gameID", Long.toString(id))
                .param("playerID", Long.toString(player.getId())))
                .andExpect(status().isOk());

        // Get Question
        restGameMockMvc.perform(get("/api/games/question/{id}", id))
                .andExpect(status().isOk());
    }
    
    @Test
    @Transactional
    public void activePlayer() throws Exception {
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(user2);
        playerRepository.saveAndFlush(creator);
        playerRepository.saveAndFlush(player);

        GameDTO gameDTO = new GameDTO();
        gameDTO.setCreatorID(creator.getId());
        gameDTO.setMaxPlayers(DEFAULT_MAX_PLAYERS);

        // Create the Game
        MvcResult gameResult = restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = gameResult.getResponse().getHeader("Location");
        Pattern pattern = Pattern.compile("(\\d+)$");
        Matcher matcher = pattern.matcher(location);
        matcher.find();
        Long id = Long.parseLong(matcher.group(), 10);

        // Add Player
        restGameMockMvc.perform(post("/api/games/add-player")
                .param("gameID", Long.toString(id))
                .param("playerID", Long.toString(player.getId())))
                .andExpect(status().isOk());

        // Get Player
        restGameMockMvc.perform(get("/api/games/player/{id}", id))
                .andExpect(status().isOk());
    }

    
    @Test
    @Transactional
    public void nextPlayerTurnAndFinish() throws Exception {
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(user2);
        playerRepository.saveAndFlush(creator);
        playerRepository.saveAndFlush(player);
        
        GameDTO gameDTO = new GameDTO();
        gameDTO.setCreatorID(creator.getId());
        gameDTO.setMaxPlayers(DEFAULT_MAX_PLAYERS);

        // Create the Game
        MvcResult gameResult = restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = gameResult.getResponse().getHeader("Location");
        Pattern pattern = Pattern.compile("(\\d+)$");
        Matcher matcher = pattern.matcher(location);
        matcher.find();
        Long id = Long.parseLong(matcher.group(), 10);

        // Add Player
        restGameMockMvc.perform(post("/api/games/add-player")
                .param("gameID", Long.toString(id))
                .param("playerID", Long.toString(player.getId())))
                .andExpect(status().isOk());
        
        Game testGame = gameService.findOne(id);
        int firstPlayer = testGame.getFirstPlayer();
        int activePlayer = testGame.getActivePlayer();
        int activeQuestion = testGame.getActiveQuestion();    
        assertThat(testGame.getActiveQuestion()).isEqualTo(0);
        
        // Next Turn
        testGame = gameService.nextPlayerTurn(testGame);
        
        assertThat(testGame.getActivePlayer()).isNotEqualTo(activePlayer);
        assertThat(testGame.getActiveQuestion()).isEqualTo(activeQuestion);
        assertThat(testGame.getActiveQuestion()).isEqualTo(0);        
        testGame = gameService.nextPlayerTurn(testGame);
        
        assertThat(testGame.getActivePlayer()).isEqualTo(firstPlayer);
        assertThat(testGame.getActiveQuestion()).isNotEqualTo(activeQuestion);
        assertThat(testGame.getActiveQuestion()).isEqualTo(1);      
        testGame = gameService.nextPlayerTurn(testGame);
        
        assertThat(testGame.getActiveQuestion()).isEqualTo(1);     
        testGame = gameService.nextPlayerTurn(testGame);
        testGame = gameService.nextPlayerTurn(testGame);
        
        assertThat(testGame.getActiveQuestion()).isEqualTo(2);    
        testGame = gameService.nextPlayerTurn(testGame);
        testGame = gameService.nextPlayerTurn(testGame);
        
        assertThat(testGame.getActiveQuestion()).isEqualTo(3);
        testGame = gameService.nextPlayerTurn(testGame);
        testGame = gameService.nextPlayerTurn(testGame);
        
        assertThat(testGame.getActiveQuestion()).isEqualTo(4);
        testGame = gameService.nextPlayerTurn(testGame);
        testGame = gameService.nextPlayerTurn(testGame);
        
        assertThat(testGame.getActiveQuestion()).isEqualTo(5);
        testGame = gameService.nextPlayerTurn(testGame);
        testGame = gameService.nextPlayerTurn(testGame);
        
        assertThat(testGame.getActiveQuestion()).isEqualTo(6);
        testGame = gameService.nextPlayerTurn(testGame);
        testGame = gameService.nextPlayerTurn(testGame);
        
        testGame.getGamePlayers().get(0).setCorrectQuestions(1);
        testGame.getGamePlayers().get(1).setCorrectQuestions(1);
        assertThat(testGame.getActiveQuestion()).isEqualTo(7);
        testGame = gameService.nextPlayerTurn(testGame);
        testGame = gameService.nextPlayerTurn(testGame);
        
        assertThat(testGame.getState()).isEqualTo(GameState.CLOSE);
        assertThat(testGame.getActiveQuestion()).isEqualTo(null);
        assertThat(testGame.getActivePlayer()).isEqualTo(null);
        assertThat(testGame.getWinner()).isNotEqualTo(null);
        assertThat(testGame.getWinner().getPoints()).isEqualTo(Game.GAME_WIN_POINTS);
        assertThat(testGame.getGamePlayers().get(0).getPlayer().getPoints()).isEqualTo(Game.GAME_WIN_POINTS);
        assertThat(testGame.getGamePlayers().get(1).getPlayer().getPoints()).isEqualTo(Game.GAME_WIN_POINTS);
    }
    
    @Test
    @Transactional
    public void answerQuestion() throws Exception {
        levelRepository.saveAndFlush(level);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(user2);
        playerRepository.saveAndFlush(creator);
        playerRepository.saveAndFlush(player);
        
        GameDTO gameDTO = new GameDTO();
        gameDTO.setCreatorID(creator.getId());
        gameDTO.setMaxPlayers(DEFAULT_MAX_PLAYERS);

        // Create the Game
        MvcResult gameResult = restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = gameResult.getResponse().getHeader("Location");
        Pattern pattern = Pattern.compile("(\\d+)$");
        Matcher matcher = pattern.matcher(location);
        matcher.find();
        Long id = Long.parseLong(matcher.group(), 10);
        
        Game testGame = gameService.findOne(id);

        // Add Player
        restGameMockMvc.perform(post("/api/games/add-player")
                .param("gameID", Long.toString(id))
                .param("playerID", Long.toString(player.getId())))
                .andExpect(status().isOk());
        
        Player testPlayer = playerService.findOne(testGame.getGamePlayers().get(0).getPlayer().getId());
        testPlayer.setPoints(95);
        testGame.getGamePlayers().get(0).setPoints(95);
        // Answer Question
        MvcResult playerResult = restPlayerMockMvc.perform(post("/api/players/answer-question")
                .param("gameID", Long.toString(id))
                .param("gamePlayerID", Long.toString(testGame.getGamePlayers().get(0).getId()))
                .param("questionID", Long.toString(testGame.getQuestions().get(0).getId()))
                .param("answer", testGame.getQuestions().get(0).getAnswer()))
                .andExpect(status().isOk())
                .andReturn();
        
        testPlayer = playerService.findOne(testGame.getGamePlayers().get(0).getPlayer().getId());
        
        assertThat(playerResult.getResponse().getContentAsString()).isEqualTo("true");
        assertThat(testPlayer.getPoints()).isEqualTo(95 + Game.QUESTION_IMAGE_POINTS);
        assertThat(testGame.getGamePlayers().get(0).getPoints()).isEqualTo(95 + Game.QUESTION_IMAGE_POINTS);
        assertThat(testGame.getGamePlayers().get(0).getCorrectQuestions()).isEqualTo(1);
        assertThat(testPlayer.getLevel().getName()).isEqualTo(LevelName.SILVER);
    }
}
