# CSA Bookstore RESTful API

A comprehensive RESTful bookstore management system built with JAX-RS and JSON, providing full functionality for managing books, authors, customers, shopping carts, and order processing.

## Overview

This project implements a complete bookstore management system using Java Enterprise Edition (JEE) technologies. The API provides endpoints for managing all aspects of a bookstore including inventory management, customer registration, shopping cart functionality, and order processing with proper error handling and data validation.

## Project Structure

```
CSA-Bookstore/
├── src/main/
│   ├── java/com/bookstore/csa/bookstore/
│   │   ├── db/
│   │   │   └── InMemoryDatabase.java          # In-memory data storage
│   │   ├── exception/                         # Custom exceptions and mappers
│   │   │   ├── AuthorNotFoundException.java
│   │   │   ├── AuthorNotFoundExceptionMapper.java
│   │   │   ├── BookNotFoundException.java
│   │   │   ├── BookNotFoundExceptionMapper.java
│   │   │   ├── CartNotFoundException.java
│   │   │   ├── CartNotFoundExceptionMapper.java
│   │   │   ├── CustomerNotFoundException.java
│   │   │   ├── CustomerNotFoundExceptionMapper.java
│   │   │   ├── InvalidInputException.java
│   │   │   ├── InvalidInputExceptionMapper.java
│   │   │   ├── OrderNotFoundException.java
│   │   │   ├── OrderNotFoundExceptionMapper.java
│   │   │   ├── OutOfStockException.java
│   │   │   └── OutOfStockExceptionMapper.java
│   │   ├── model/                             # Data models
│   │   │   ├── Author.java                    # Author entity
│   │   │   ├── Book.java                      # Book entity
│   │   │   ├── Cart.java                      # Shopping cart entity
│   │   │   ├── CartItem.java                  # Cart item entity
│   │   │   ├── Customer.java                  # Customer entity
│   │   │   ├── Order.java                     # Order entity
│   │   │   └── OrderItem.java                 # Order item entity
│   │   ├── resource/                          # REST endpoints
│   │   │   ├── AuthorResource.java            # Author management endpoints
│   │   │   ├── BookResource.java              # Book management endpoints
│   │   │   ├── CartResource.java              # Shopping cart endpoints
│   │   │   ├── CustomerResource.java          # Customer management endpoints
│   │   │   └── OrderResource.java             # Order processing endpoints
│   │   └── resources/
│   │       ├── JavaEE8Resource.java
│   │       └── JAXRSConfiguration.java        # JAX-RS configuration
│   └── resources/META-INF/
│       └── webapp/
├── target/                                    # Build output directory
├── nb-configuration.xml                       # NetBeans configuration
├── pom.xml                                    # Maven dependencies
└── README.md
```

## Dependencies

- Java Development Kit (JDK) 8 or higher
- Apache Maven 3.6+
- Java EE 8 Application Server (WildFly, GlassFish, or similar)
- JAX-RS 2.1 implementation
- JSON-B for JSON processing

## Installation and Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/csa-bookstore.git
   cd csa-bookstore
   ```

2. **Verify prerequisites:**
   ```bash
   java -version
   mvn -version
   ```

3. **Build the project:**
   ```bash
   mvn clean compile
   ```

4. **Package the application:**
   ```bash
   mvn package
   ```

## How to Run

### Using Maven with Embedded Server
```bash
# Run with Maven (if configured)
mvn exec:java
```

### Deployment to Application Server
1. **Build WAR file:**
   ```bash
   mvn clean package
   ```

2. **Deploy to server:**
   - Copy the generated WAR file from `target/` directory
   - Deploy to your Java EE application server
   - Access the API at `http://localhost:8080/csa-bookstore/api/`

### Using IDE
1. Import project into NetBeans/IntelliJ/Eclipse
2. Configure Java EE application server
3. Run the project from IDE

## API Endpoints

### Book Management
- `POST /books` - Create a new book
- `GET /books` - Get all books
- `GET /books/{id}` - Get book by ID
- `PUT /books/{id}` - Update book details
- `DELETE /books/{id}` - Delete a book

### Author Management
- `POST /authors` - Create a new author
- `GET /authors` - Get all authors
- `GET /authors/{id}` - Get author by ID
- `PUT /authors/{id}` - Update author details
- `DELETE /authors/{id}` - Delete an author
- `GET /authors/{id}/books` - Get books by specific author

### Customer Management
- `POST /customers` - Register new customer
- `GET /customers` - Get all customers
- `GET /customers/{id}` - Get customer by ID
- `PUT /customers/{id}` - Update customer details
- `DELETE /customers/{id}` - Delete customer account

### Shopping Cart
- `POST /customers/{customerId}/cart/items` - Add item to cart
- `GET /customers/{customerId}/cart` - Get customer's cart
- `PUT /customers/{customerId}/cart/items/{bookId}` - Update item quantity
- `DELETE /customers/{customerId}/cart/items/{bookId}` - Remove item from cart

### Order Processing
- `POST /customers/{customerId}/orders` - Create order from cart
- `GET /customers/{customerId}/orders` - Get customer's order history
- `GET /customers/{customerId}/orders/{orderId}` - Get specific order details

## Request/Response Examples

### Create a Book
**Request:**
```json
POST /books
{
  "title": "Harry Potter and the Sorcerer's Stone",
  "authorId": 1,
  "isbn": "978-0-590-35340-3",
  "publicationYear": 1997,
  "price": 15.99,
  "stock": 200
}
```

**Response:**
```json
{
  "id": 1,
  "title": "Harry Potter and the Sorcerer's Stone",
  "authorId": 1,
  "isbn": "978-0-590-35340-3",
  "publicationYear": 1997,
  "price": 15.99,
  "stock": 200
}
```

### Add Item to Cart
**Request:**
```json
POST /customers/1/cart/items
{
  "bookId": 1,
  "quantity": 2
}
```

**Response:**
```json
{
  "customerId": 1,
  "items": {
    "1": {
      "bookId": 1,
      "quantity": 2,
      "unitPrice": 15.99,
      "totalPrice": 31.98
    }
  },
  "totalPrice": 31.98,
  "totalQuantity": 2
}
```

## Error Handling

The API provides comprehensive error handling with appropriate HTTP status codes:

- **400 Bad Request** - Invalid input data or validation errors
- **404 Not Found** - Resource not found (book, author, customer, etc.)
- **409 Conflict** - Resource conflicts (duplicate email, etc.)
- **500 Internal Server Error** - Server-side errors

**Error Response Format:**
```json
{
  "error": "Book Not Found",
  "message": "Book with ID 999 does not exist"
}
```

## Data Validation

The API includes validation for:
- **Books**: Valid author ID, positive price and stock values, proper ISBN format
- **Authors**: Required name fields, biography length limits
- **Customers**: Valid email format, password strength requirements
- **Orders**: Stock availability, positive quantities
- **Cart Operations**: Item availability, quantity limits

## Testing

The API has been thoroughly tested using Postman with test cases covering:
- All CRUD operations for each resource
- Error scenarios and edge cases
- Input validation and data integrity
- Cart and order workflow
- Exception handling

### Running Tests
```bash
# Unit tests (if implemented)
mvn test

# Integration tests
mvn verify
```

## Implementation Details

### Architecture
- **RESTful Design**: Follows REST principles with proper HTTP methods and status codes
- **Layered Architecture**: Separation of concerns with models, resources, and data access
- **Exception Handling**: Custom exception classes with JAX-RS exception mappers
- **In-Memory Storage**: Simple data persistence using the InMemoryDatabase class

### Key Features
- Complete CRUD operations for all entities
- Shopping cart functionality with quantity management
- Order processing with inventory tracking
- Comprehensive error handling and validation
- JSON request/response format
- Proper HTTP status code usage

### Technology Stack
- **JAX-RS 2.1**: RESTful web services framework
- **JSON-B**: JSON binding for Java objects
- **Java EE 8**: Enterprise application platform
- **Maven**: Build and dependency management
- **In-Memory Database**: Simple data storage solution

## Performance Considerations

- **In-Memory Storage**: Fast read/write operations but data is not persistent
- **Stateless Design**: Each request is independent for better scalability
- **Efficient Data Models**: Lightweight POJOs with minimal overhead
- **Exception Mapping**: Centralized error handling for consistent responses

## API Usage Guidelines

1. **Content-Type**: Always use `application/json` for request/response
2. **HTTP Methods**: Use appropriate methods (GET, POST, PUT, DELETE)
3. **Error Handling**: Check response status codes and error messages
4. **Data Validation**: Ensure input data meets requirements before sending
5. **Resource IDs**: Use valid IDs when accessing specific resources

## Deployment Notes

- Ensure a Java EE 8-compatible application server
- Configure server ports and context paths as needed
- Set up proper logging for production environments
- Consider database integration for persistent storage
- Implement security measures for production deployment
