package com.belvedere.domain.exception;

/**
 *
 * @author renzobelvedere
 */
public class PlayerNotInGameException extends Exception{
    
    public PlayerNotInGameException(){
        super("El jugador no se encuentra dentro de la partida del juego");
    }
}
