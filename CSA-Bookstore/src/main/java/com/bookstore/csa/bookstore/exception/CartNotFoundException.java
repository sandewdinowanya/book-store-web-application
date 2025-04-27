/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.csa.bookstore.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hp
 */
public class CartNotFoundException extends RuntimeException {
    
    private static final Logger LOGGER = Logger.getLogger(CartNotFoundException.class.getName());
    
    /**
     * constructs a new CartNotFoundException with the specified detail message
     * @param message 
     */
    public CartNotFoundException(String message){
        super(message);
        LOGGER.log(Level.WARNING, "CartNotFoundException thrown: {0}", message);
    }
}
