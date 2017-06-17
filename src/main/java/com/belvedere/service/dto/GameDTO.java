package com.belvedere.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * A DTO representing a Game.
 */
public class GameDTO {

    private Long id;
    
    @NotNull
    private Long creatorID;

    @NotNull
    @Min(2)
    @Max(4)
    private int maxPlayers;

    /**
     * Constructor.
     */
    public GameDTO() {
    }

    /**
     * @return the maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @param maxPlayers the maxPlayers to set
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * @return the creatorID
     */
    public Long getCreatorID() {
        return creatorID;
    }

    /**
     * @param creatorID the creatorID to set
     */
    public void setCreatorID(Long creatorID) {
        this.creatorID = creatorID;
    }

    @Override
    public String toString() {
        return "GameDTO{"
                + "creatorID='" + creatorID + '\''
                + ", maxPlayers='" + maxPlayers
                + "} ";
    }
}
