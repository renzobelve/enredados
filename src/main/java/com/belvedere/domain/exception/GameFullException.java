package com.belvedere.domain.exception;

/**
 *
 * @author renzobelvedere
 */
public class GameFullException extends Exception{
    
    public GameFullException(){
        super("La partida del juego ya tiene a todos sus jugadores");
    }
}
