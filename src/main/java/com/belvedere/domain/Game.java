package com.belvedere.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.belvedere.domain.enumeration.GameState;
import com.belvedere.domain.exception.GameActiveException;
import com.belvedere.domain.exception.GameFullException;
import com.belvedere.domain.exception.PlayerInGameException;
import com.belvedere.domain.exception.PlayerNotInGameException;
import com.belvedere.domain.exception.PlayerNotInLevelException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int QUESTION_IMAGE_POINTS = 10;
    public static final int QUESTION_TEXT_POINTS = 15;
    public static final int GAME_WIN_POINTS = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private GameState state;

    @NotNull
    @Column(name = "max_players", nullable = false)
    private Integer maxPlayers;

    @NotNull
    @Column(name = "size", nullable = false)
    private Integer size;

    @ManyToOne
    private Player creator;
    
    @Column(name = "first_player")
    private Integer firstPlayer;

    @Column(name = "active_player")
    private Integer activePlayer;

    @Column(name = "active_question")
    private Integer activeQuestion;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<GamePlayer> gamePlayers = new ArrayList<>();
    
    @ManyToOne
    private Player winner;

    @ManyToMany(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "game_questions",
            joinColumns = @JoinColumn(name = "games_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "questions_id", referencedColumnName = "id"))
    private List<Question> questions = new ArrayList<>();

    public Game() {
    }

    /**
     * Constructor.
     *
     * @param creator
     * @param maxPlayers
     * @throws com.belvedere.domain.exception.GameFullException
     * @throws com.belvedere.domain.exception.PlayerInGameException
     * @throws com.belvedere.domain.exception.PlayerNotInLevelException
     */
    public Game(Player creator, int maxPlayers) throws GameFullException, PlayerInGameException, PlayerNotInLevelException {
        this.maxPlayers = maxPlayers;
        this.state = GameState.OPEN;
        this.winner = null;
        this.firstPlayer = null;
        this.activePlayer = null;
        this.activeQuestion = null;
        this.creator = creator;
        this.addPlayer(creator);

        switch (maxPlayers) {
            case 2:
                this.size = 8;
                break;
            case 3:
                this.size = 12;
                break;
            case 4:
                this.size = 16;
                break;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameState getState() {
        return state;
    }

    public Game state(GameState state) {
        this.state = state;
        return this;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public Game maxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getActivePlayer() {
        return activePlayer;
    }

    public Game activePlayer(Integer activePlayer) {
        this.activePlayer = activePlayer;
        return this;
    }

    public void setActivePlayer(Integer activePlayer) {
        this.activePlayer = activePlayer;
    }

    public Integer getActiveQuestion() {
        return activeQuestion;
    }

    public Game activeQuestion(Integer activeQuestion) {
        this.activeQuestion = activeQuestion;
        return this;
    }

    public void setActiveQuestion(Integer activeQuestion) {
        this.activeQuestion = activeQuestion;
    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Game addGamePlayers(GamePlayer gamePlayer) {
        this.gamePlayers.add(gamePlayer);
        gamePlayer.setGame(this);
        return this;
    }

    public Game removeGamePlayers(GamePlayer gamePlayer) {
        this.gamePlayers.remove(gamePlayer);
        gamePlayer.setGame(null);
        return this;
    }

    public void setGamePlayers(List<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Game addQuestions(Question question) {
        this.questions.add(question);
        question.getGames().add(this);
        return this;
    }

    public Game removeQuestions(Question question) {
        this.questions.remove(question);
        question.getGames().remove(this);
        return this;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Integer getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Integer firstPlayer) {
        this.firstPlayer = firstPlayer;
    }
    
    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
    
    public Player getCreator() {
        return creator;
    }

    public void setCreator(Player creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        if (game.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), game.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Game{"
                + "id=" + getId()
                + ", state='" + getState() + "'"
                + ", maxPlayers='" + getMaxPlayers() + "'"
                + ", activePlayer='" + getActivePlayer() + "'"
                + ", activeQuestion='" + getActiveQuestion() + "'"
                + "}";
    }

    /**
     * Metodo que agrega un jugador al juego.
     *
     * @param player
     * @throws com.belvedere.domain.exception.GameFullException
     * @throws com.belvedere.domain.exception.PlayerInGameException
     * @throws com.belvedere.domain.exception.PlayerNotInLevelException
     */
    public void addPlayer(Player player) throws GameFullException, PlayerInGameException, PlayerNotInLevelException {
        // Valida que el juego este abierto
        if (this.getState() != GameState.OPEN) {
            throw new GameFullException();
        }

        // Valida que el jugador pertenezca al mismo nivel que el creador
        if (!this.getGamePlayers().isEmpty()) {
            for (GamePlayer gamePlayer : this.getGamePlayers()) {
                if (!gamePlayer.getPlayer().getLevel().getName().equals(player.getLevel().getName())) {
                    throw new PlayerNotInLevelException();
                }
            }
        }

        // Valida que el jugador no este dentro del juego
        if (this.searchPlayer(player)) {
            throw new PlayerInGameException();
        }

        GamePlayer gamePlayer = new GamePlayer(this, player);
        this.addGamePlayers(gamePlayer);

        // Inicia el Juego si ya esta lleno
        if (this.getMaxPlayers() == this.getGamePlayers().size()) {
            this.startGame();
        }

    }

    public void removePlayer(Player player) throws GameActiveException, PlayerNotInGameException {
        if (this.getState() == GameState.OPEN) {
            this.removeGamePlayer(player);
        } else {
            throw new GameActiveException();
        }
    }

    /**
     * Metodo que busca un jugador dentro del juego.
     *
     * @param player
     */
    private boolean searchPlayer(Player player) {
        boolean result = false;
        for (GamePlayer gamePlayer : this.getGamePlayers()) {
            if (gamePlayer.getPlayer().getUser().getEmail().equals(player.getUser().getEmail())) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Metodo que elimina un jugador dentro del juego.
     *
     * @param player
     */
    private void removeGamePlayer(Player player) throws PlayerNotInGameException {
        boolean result = false;
        Iterator<GamePlayer> it = this.getGamePlayers().iterator();
        while (it.hasNext()) {
            GamePlayer gamePlayer = it.next();
            if (gamePlayer.getPlayer().getUser().getEmail().equals(player.getUser().getEmail())) {
                this.removeGamePlayers(gamePlayer);
                result = true;
                break;
            }
        }

        if (!result) {
            throw new PlayerNotInGameException();
        }
    }

    /**
     * Metodo que inicia una partida de juego.
     */
    public void startGame() {
        // Activa el juego para que no se permita el ingreso de mas jugadores
        this.setState(GameState.ACTIVE);
        // Determina de forma aleatoria el primer Jugador
        Random randomNumber = new Random();
        int randomPlayerNumber = randomNumber.nextInt(this.getGamePlayers().size());
        this.setFirstPlayer(randomPlayerNumber);
        this.setActivePlayer(randomPlayerNumber);
        this.setActiveQuestion(0);
    }

    /**
     * Metodo que finaliza una partida de juego.
     *
     */
    public void finishGame() {
        // Cierra el juego para darlo por finalizado
        this.setState(GameState.CLOSE);
        // Anula los valores activos
        this.setActivePlayer(null);
        this.setActiveQuestion(null);
        // Determina el Jugador ganador
        this.winnerPlayer();
    }

    /**
     * Metodo que determina el ganador del juego.
     *
     */
    private void winnerPlayer() {
        int maxQuestions = 0;
        List<Player> winnerPlayers = new ArrayList<>();
        
        for(GamePlayer gamePlayer : this.getGamePlayers()){
            if(gamePlayer.getCorrectQuestions() >= maxQuestions){
                maxQuestions = gamePlayer.getCorrectQuestions();
                winnerPlayers.add(gamePlayer.getPlayer());
            }
        }
        for(Player player : winnerPlayers){
            this.setWinner(player);
            player.addPoints(Game.GAME_WIN_POINTS);
        }
        
    }

    /**
     * Metodo que inicia el turno del jugador siguiente en la ronda.
     */
    public void nextPlayerTurn() {
        // Asigna proximo jugador
        this.setActivePlayer((this.getActivePlayer() + 1) % this.getGamePlayers().size());
        // Asigna proxima pregunta si corresponde
        if (this.getFirstPlayer().equals(this.getActivePlayer())) {
            this.setActiveQuestion(this.getActiveQuestion() + 1);
        }
    }

    /**
     * Metodo que retorna la pregunta actual del juego.
     *
     * @return
     */
    public Question activeQuestion() throws IndexOutOfBoundsException {
        Question question = this.getQuestions().get(this.getActiveQuestion());
        return question;
    }

    /**
     * Metodo que retorna el jugador actual del juego.
     *
     * @return
     */
    public Player activePlayer() {
        Player player = this.getGamePlayers().get(this.getActivePlayer()).getPlayer();
        return player;
    }

    
}
