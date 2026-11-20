# Bookstore application
This application is of form of REST service for storing books information. Created with use of Java 25 and gradle as building tool.

### API base URL
`http://localhost:8080/bookstore/api/books`

### Running instructions:
- Build the war file using gradle `./gradlew build or ./gradlew war`, as a result bookstore.war file will be created and located in the /build/libs folder
- Deploy the war file to the e.g. Tomcat server
- The API will be available on its base URL
- This project contains bruno collection in bookstore-ee-bruno folder that can be used for testing purspose.
- Example of data that can be imported via http://localhost:8080/bookstore/api/books/import is available in project main/resources folder
### Book types
There are three book types:

- STANDARD
- ANTIQUE
- SCIENCE_JOURNAL
  
### Available methods
- `POST /api/books` - add new book
- `GET /api/books/{barcode}` - get a book by barcode
- `POST /api/books/search` - get books by search criteria, search criteria are passed in form of JSON payload
- `PATCH /api/books/{barcode}` - update books identified via barcode with data passed in JSON payload
- `GET /api/books/total-price?barcodes=XXX` - calculate total price of all books with barcodes passed as query parameters
- `GET /api/books/barcodes-by-quantity` - returns barcodes grouped by books quantity
- `GET /api/books/export - returns JSON` representing an array of all books stored in the database
- `POST /api/books/import` - imports into database all books passed in JSON payload
