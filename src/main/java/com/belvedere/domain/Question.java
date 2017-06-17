package com.belvedere.domain;

import com.belvedere.domain.enumeration.QuestionDificulty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.belvedere.domain.enumeration.QuestionType;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private QuestionType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dificulty", nullable = false)
    private QuestionDificulty dificulty;

    @NotNull
    @Column(name = "answer_time", nullable = false)
    private Integer answerTime;

    @NotNull
    @Column(name = "answer", nullable = false)
    private String answer;

    @ManyToMany(mappedBy = "questions")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Game> games = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public Question question(String question) {
        this.question = question;
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionType getType() {
        return type;
    }

    public Question type(QuestionType type) {
        this.type = type;
        return this;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public Integer getAnswerTime() {
        return answerTime;
    }

    public Question answerTime(Integer answerTime) {
        this.answerTime = answerTime;
        return this;
    }

    public void setAnswerTime(Integer answerTime) {
        this.answerTime = answerTime;
    }

    public String getAnswer() {
        return answer;
    }

    public Question answer(String answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Set<Game> getGames() {
        return games;
    }

    public Question games(Set<Game> games) {
        this.games = games;
        return this;
    }

    public Question addGames(Game game) {
        this.games.add(game);
        game.getQuestions().add(this);
        return this;
    }

    public Question removeGames(Game game) {
        this.games.remove(game);
        game.getQuestions().remove(this);
        return this;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    /**
     * @return the dificulty
     */
    public QuestionDificulty getDificulty() {
        return dificulty;
    }

    /**
     * @param dificulty the dificulty to set
     */
    public void setDificulty(QuestionDificulty dificulty) {
        this.dificulty = dificulty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        if (question.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Question{"
                + "id=" + getId()
                + ", question='" + getQuestion() + "'"
                + ", type='" + getType() + "'"
                + ", answerTime='" + getAnswerTime() + "'"
                + ", answer='" + getAnswer() + "'"
                + "}";
    }

    /**
     * Metodo que verifica si la respuesta de la pregunta es correcta.
     *
     * @param answer
     * @return boolean
     */
    public boolean checkIsCorrect(String answer) {
        return answer.equals(this.getAnswer());
    }
}
