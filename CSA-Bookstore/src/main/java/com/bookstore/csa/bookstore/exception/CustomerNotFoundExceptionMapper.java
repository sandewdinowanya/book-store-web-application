/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.csa.bookstore.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Hp
 */
@Provider
public class CustomerNotFoundExceptionMapper implements ExceptionMapper<CustomerNotFoundException> {
    
    private static final Logger LOGGER = Logger.getLogger(CustomerNotFoundExceptionMapper.class.getName());
    
    /**
     * converts a CustomerNotFoundException into an HTTP Response
     * @param exception the exception that was thrown
     * @return response object with status 404 and JSON error details
     */
    @Override
    public Response toResponse(CustomerNotFoundException exception){
        
        LOGGER.log(Level.WARNING,"Customer not found: {0}", exception.getMessage());
        
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Customer Not Found");
        errorResponse.put("message", exception.getMessage());
        
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
