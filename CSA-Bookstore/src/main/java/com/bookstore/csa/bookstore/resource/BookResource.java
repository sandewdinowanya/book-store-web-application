/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.csa.bookstore.resource;

import com.bookstore.csa.bookstore.db.InMemoryDatabase;
import com.bookstore.csa.bookstore.exception.AuthorNotFoundException;
import com.bookstore.csa.bookstore.exception.BookNotFoundException;
import com.bookstore.csa.bookstore.exception.InvalidInputException;
import com.bookstore.csa.bookstore.model.Author;
import com.bookstore.csa.bookstore.model.Book;

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

/**
 * managing book entities through RESTful APIs
 * provide endpoints to perform CRUD operations
 * @author Hp
 */

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    
    private static final Logger LOGGER = Logger.getLogger(BookResource.class.getName());
    private InMemoryDatabase database = InMemoryDatabase.getInstance();
    
    /**
     * retrieves all books from the database
     * @return list of all books
     */
    @GET
    public List<Book> getAllBooks(){
        LOGGER.info("Fetching all books");
        return database.getAllBooks();
    }
    
    /**
     * retrieves a book by its ID
     * @param id of the book to retrieve
     * @return book object
     */
    @GET
    @Path("/{id}")
    public Book getBook(@PathParam("id")Long id){
       LOGGER.log(Level.INFO, "Fetching book with ID: {0}", id);
       Book book = database.getBookId(id);
       if(book == null){
           throw new BookNotFoundException("Book with ID " + id + " does not exist");
       }
       return book;
    }
    
    /**
     * creates a new book with the author 
     * @param book object to create
     * @return HTTP Response with created book
     */
    @POST
    public Response createBook(Book book){
    //        Book createdBook = database.addBook(book);
    //        return Response.status(Response.Status.CREATED)
    //                .entity(createdBook)
    //                .build();

    LOGGER.log(Level.INFO, "Creating a new book : {0}", book.getTitle());
    // check if author exists
    if(book.getAuthorId() != null && !database.authorExists(book.getAuthorId())){
        LOGGER.log(Level.WARNING, "Author with ID {0} not found during book creation", book.getAuthorId());
        throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist");
    }
    
    if(book.getPublicationYear() <= 0 || book.getPrice() <= 0 || book.getStock() <= 0){
        LOGGER.warning("Invalid input detected while creating book");
        throw new InvalidInputException("Numeric values must be positive");
    }
    
    if(book.getAuthorId() <= 0 ){
        LOGGER.warning("Invalid author ID during book creation");
        throw new InvalidInputException("Author ID can't be negative");
    }
    
    Book createdBook = database.addBook(book);
    
    // Add book ID to author's book List
    if(book.getAuthorId() != null){
        Author author = database.getAuthorById(book.getAuthorId());
        if(author != null){
            author.addBookId(createdBook.getId());
            database.updateAuthor(author);
            LOGGER.log(Level.INFO, "Associated new book with author ID: {0}", book.getAuthorId());
        }
    }
    
    return Response.status(Response.Status.CREATED)
            .entity(createdBook)
            .build();
    }
    
    /**
     * updates an existing book with new details
     * @param id ID of the book to update
     * @param book the updated Book object
     * @return updated Book object
     */
    @PUT
    @Path("/{id}")
    public Book updateBook(@PathParam("id") Long id, Book book){
        LOGGER.log(Level.WARNING, "Book with ID : {0} not found for update", id);
        
        // check if book exists
        Book existingBook = database.getBookId(id);
        if(existingBook == null){
            LOGGER.log(Level.WARNING, "Updating book with ID: {0}not found for update", id);
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }

        // check if author exists
        if(book.getAuthorId() != null && !database.authorExists(book.getAuthorId())){
            LOGGER.log(Level.WARNING, "Author with ID {0} not found during book update", book.getAuthorId());
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist");
        }

        // update author references if author changed
        if(existingBook.getAuthorId() != null && !existingBook.getAuthorId().equals(book.getAuthorId())){
            // remove book from old author
            Author oldAuthor = database.getAuthorById(existingBook.getAuthorId());
            if(oldAuthor != null){
                oldAuthor.removeBookId(id);
                database.updateAuthor(oldAuthor);
                LOGGER.log(Level.INFO, "Removed book ID {0} from old author ID {1}", new Object[]{id, existingBook.getAuthorId()});

            }

            // add book to new author
            if(book.getAuthorId() != null){
                Author newAuthor = database.getAuthorById(book.getAuthorId());
                if(newAuthor != null){
                    newAuthor.addBookId(id);
                    database.updateAuthor(newAuthor);
                    LOGGER.log(Level.INFO, "Added book ID {0} to new author ID {1}", new Object[]{id, book.getAuthorId()});
                }
            } 
        }
        book.setId(id);
            return database.updateBook(book);
    }

    /**
     * deletes a book by its ID
     * @param id of the book to delete
     * @return HTTP Response with no content
     */
    @DELETE 
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id){
        LOGGER.log(Level.INFO, "Deleting book with ID: {0}", id);
        boolean deleted = database.deleteBook(id);
        if(!deleted){
            LOGGER.log(Level.WARNING, "Book with ID {0} not found dor deletion", id);
            throw new BookNotFoundException("Book with ID " + id + " does not exist");
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}


