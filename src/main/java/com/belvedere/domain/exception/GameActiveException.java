package com.belvedere.domain.exception;

/**
 *
 * @author renzobelvedere
 */
public class GameActiveException extends Exception{
    
    public GameActiveException(){
        super("El juego ya se encuentra activo");
    }
}
