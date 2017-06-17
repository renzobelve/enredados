package com.belvedere.service.dto;

import javax.validation.constraints.NotNull;

/**
 *
 * A DTO representing a Player.
 */
public class PlayerDTO {

    @NotNull
    private Long id;

    /**
     * Constructor.
     */
    public PlayerDTO() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

}
