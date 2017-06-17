package com.belvedere.domain.exception;

/**
 *
 * @author renzobelvedere
 */
public class PlayerNotInLevelException extends Exception{
    
    public PlayerNotInLevelException(){
        super("El jugador no se encuentra dentro del nivel de la partida");
    }
}
