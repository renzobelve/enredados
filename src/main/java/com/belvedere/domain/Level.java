package com.belvedere.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.belvedere.domain.enumeration.LevelName;

/**
 * A Level.
 */
@Entity
@Table(name = "level")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private LevelName name;

    @NotNull
    @Column(name = "min_points", nullable = false)
    private Integer minPoints;

    @NotNull
    @Column(name = "max_points", nullable = false)
    private Integer maxPoints;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LevelName getName() {
        return name;
    }

    public Level name(LevelName name) {
        this.name = name;
        return this;
    }

    public void setName(LevelName name) {
        this.name = name;
    }

    public Integer getMinPoints() {
        return minPoints;
    }

    public Level minPoints(Integer minPoints) {
        this.minPoints = minPoints;
        return this;
    }

    public void setMinPoints(Integer minPoints) {
        this.minPoints = minPoints;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public Level maxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
        return this;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Level level = (Level) o;
        if (level.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), level.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Level{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", minPoints='" + getMinPoints() + "'" +
            ", maxPoints='" + getMaxPoints() + "'" +
            "}";
    }
}
