package com.belvedere.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GamePlayer.
 */
@Entity
@Table(name = "game_player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GamePlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "correct_questions", nullable = false)
    private Integer correctQuestions;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @ManyToOne
    private Player player;

    @ManyToOne
    @JsonIgnore
    private Game game;

    /**
     * Constructor.
     *
     * @param game
     * @param player
     */
    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.correctQuestions = 0;
        this.points = 0;
    }
    
    public GamePlayer() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCorrectQuestions() {
        return correctQuestions;
    }

    public GamePlayer correctQuestions(Integer correctQuestions) {
        this.correctQuestions = correctQuestions;
        return this;
    }

    public void setCorrectQuestions(Integer correctQuestions) {
        this.correctQuestions = correctQuestions;
    }

    public Integer getPoints() {
        return points;
    }

    public GamePlayer points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Player getPlayer() {
        return player;
    }

    public GamePlayer player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public GamePlayer game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GamePlayer gamePlayer = (GamePlayer) o;
        if (gamePlayer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gamePlayer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GamePlayer{"
                + "id=" + getId()
                + ", correctQuestions='" + getCorrectQuestions() + "'"
                + ", points='" + getPoints() + "'"
                + "}";
    }

    /**
     * Metodo que suma puntos en la partida de un jugador.
     * @param points
     */
    public void addPoints(int points) {
        this.setPoints(this.getPoints() + points);
    }
    
    /**
     * Metodo que suma respuestas correctas en la partida de un jugador.
     */
    public void correctQuestion(){
        this.setCorrectQuestions(this.getCorrectQuestions() + 1);
    }
}
