/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.csa.bookstore.resource;

import com.bookstore.csa.bookstore.db.InMemoryDatabase;
import com.bookstore.csa.bookstore.exception.CustomerNotFoundException;
import com.bookstore.csa.bookstore.exception.InvalidInputException;
import com.bookstore.csa.bookstore.model.Customer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


/**
 * 
 * @author Hp
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    
    private static final Logger LOGGER = Logger.getLogger(CustomerResource.class.getName());
    private InMemoryDatabase database = InMemoryDatabase.getInstance();
    
    //Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    /**
     * retrieves all customers from the database
     * @return List of customers
     */
    @GET
    public List<Customer> getAllCustomers(){
        LOGGER.info("Fetching all customers");
        return database.getAllCustomers();
    }
    
    /**
     * retrieves a specific customer by ID
     * @param id Customer ID
     * @return Customer object
     */
    @GET
    @Path("/{id}")
    public Customer getCustomer(@PathParam("id") Long id){
        LOGGER.log(Level.INFO, "Fetching customer with ID: {0}", id);
        Customer customer = database.getCustomerById(id);
        if(customer == null){
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found", id);
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist");
        }
        return customer;
    }
    
    /**
     * creates a new customer
     * @param customer Customer object
     * @return HTTP Response with created customer
     */
    @POST
    public Response createCustomer(Customer customer){
        LOGGER.log(Level.INFO, "Creating new customer: {0}", customer.getEmail());
        
        // validate input
        validateCustomer(customer);
        
        Customer createdCustomer = database.addCustomer(customer);
        if(createdCustomer == null){
            LOGGER.log(Level.INFO, "Email already in use: {0}", customer.getEmail());
            throw new InvalidInputException("Email already in use");
        }
        
        LOGGER.log(Level.WARNING, "Customer created successfully with ID: {0}", createdCustomer.getId());
        return Response.status(Response.Status.CREATED)
                .entity(createdCustomer)
                .build();
    }
    
    /**
     * updates an existing customer by ID
     * @param id customer ID
     * @param customer updated customer information
     * @return Updated customer object
     */
    @PUT
    @Path("/{id}")
    public Customer updateCustomer(@PathParam("id")Long id, Customer customer){
        LOGGER.log(Level.INFO, "Updating customer with ID: {0}", id);
        
        // check if customer exists
        if(!database.customerExists(id)){
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for update", id);
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        
        // validate input
        validateCustomer(customer);
        
        // set ID from path
        customer.setId(id);
        
        Customer updatedCustomer = database.updateCustomer(customer);
        if(updatedCustomer == null){
            LOGGER.log(Level.WARNING, "Email already in use by another customer: {0}", customer.getEmail());
            throw new InvalidInputException("Email already in use by another customer");
        }
        
        LOGGER.log(Level.INFO, "Customer updates succefully with ID: {0}", id);
        return updatedCustomer;
    }
    
    /**
     * deletes a customer by ID
     * @param id customer ID
     * @return HTTP Response with status NO_CONTENT
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id")Long id){
        LOGGER.log(Level.INFO, "Deleting customer with ID: {0}", id);
        
        boolean deleted = database.deleteCustomer(id);
        if(!deleted){
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for deletion", id);
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist");
        }
        
        LOGGER.log(Level.INFO, "Customer deleted successfully with ID: {0}", id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    /**
     * Helper method to validate customer input
     * throws InvalidInputException if validation fails
     * @param customer Customer to validate
     */
    private void validateCustomer(Customer customer){
        if(customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()){
            LOGGER.warning("Invalid input: first name is empty");
            throw new InvalidInputException("Customer first name is required");
        }
        
        if(customer.getLastName() == null || customer.getLastName().trim().isEmpty()){
            LOGGER.warning("Invalid input: last name is empty");
            throw new InvalidInputException("Customer last name is required");
        }
        
        if(customer.getEmail() == null || customer.getEmail().trim().isEmpty()){
            LOGGER.warning("Invalid input: email is empty");
            throw new InvalidInputException("Customer email is required");
        }
        
        if(!EMAIL_PATTERN.matcher(customer.getEmail()).matches()){
            LOGGER.warning("Invalid input: email format is invalid");
            throw new InvalidInputException("Invalid email format");
        }
        
        if(customer.getPassword() == null || customer.getPassword().length()<6){
            LOGGER.warning("Invalid input: password too short");
            throw new InvalidInputException("Password must be at least 6 characters long");
        }
    }
}


