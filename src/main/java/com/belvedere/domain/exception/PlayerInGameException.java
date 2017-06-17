package com.belvedere.domain.exception;

/**
 *
 * @author renzobelvedere
 */
public class PlayerInGameException extends Exception{
    
    public PlayerInGameException(){
        super("El jugador ya se encuentra dentro de la partida del juego");
    }
}
