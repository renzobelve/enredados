package com.belvedere.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Entities
 */
@ApiModel(description = "Entities")
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "player")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @ManyToOne
    private Level level;

    /**
     * Constructor.
     * @param user
     * @param level
     */
    public Player(User user, Level level) {
        this.user = user;
        this.level = level;
        this.points = 0;
    }
    
    public Player() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public Player points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public User getUser() {
        return user;
    }

    public Player user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Player gamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
        return this;
    }

    public Player addGamePlayers(GamePlayer gamePlayer) {
        this.gamePlayers.add(gamePlayer);
        gamePlayer.setPlayer(this);
        return this;
    }

    public Player removeGamePlayers(GamePlayer gamePlayer) {
        this.gamePlayers.remove(gamePlayer);
        gamePlayer.setPlayer(null);
        return this;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Level getLevel() {
        return level;
    }

    public Player level(Level level) {
        this.level = level;
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        if (player.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Player{"
                + "id=" + getId()
                + ", points='" + getPoints() + "'"
                + "}";
    }
    
    /**
     * Metodo que responde una pregunta realizada al jugador.
     * @param question
     * @param answer
     * @return boolean
     */
    public boolean answerQuestion(Question question, String answer){
        return question.checkIsCorrect(answer);
    }

    /**
     * Metodo que suma puntos a un jugador.
     * @param points
     */
    public void addPoints(int points) {
        this.setPoints(this.getPoints() + points);
    }
}
