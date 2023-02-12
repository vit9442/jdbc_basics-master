package org.example.dao;

import org.example.model.Author;
import org.example.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDaoImpl implements AuthorDao {
    private String url;
    private String user;
    private String password;

    private GenreDao genreDao;

    public AuthorDaoImpl(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.genreDao = new GenreDaoImpl(url, user, password);
    }

    @Override
    public Author getById(int id) throws SQLException {
        try (Connection cnn = DriverManager.getConnection(url, user, password)) {
            String query =
                    "SELECT * FROM author a\n" +
                            "INNER JOIN book b on a.author_id = b.author_id\n" +
                            "WHERE a.author_id = ?";
            PreparedStatement statement = cnn.prepareStatement(query);
            statement.setInt(1, id);
            statement.execute();
            ResultSet data = statement.getResultSet();
            Author author = new Author();
            data.next();

            author.setName(data.getString("name_author"));
            author.setId(data.getInt("author_id"));

            List<Book> books = new ArrayList<>();

            do {
                Book book = new Book();
                book.setAuthor(author);
                book.setId(data.getInt("book_id"));
                book.setAmount(data.getInt("amount"));
                book.setPrice(data.getDouble("price"));
                book.setTitle(data.getString("title"));
                book.setGenre(genreDao.getById(data.getInt("genre_id")));
                books.add(book);
            } while (data.next());

            author.setBooks(books);
            return author;
        }
    }

    @Override
    public List<Author> getAll() throws SQLException {
        List<Author> authors = new ArrayList<Author>();
        try (Connection cnn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM book " +
                    "INNER JOIN author ON book.author_id = author.author_id " +
                    "INNER JOIN genre ON genre.genre_id = book.genre_id ORDER BY author.author_id;";
            PreparedStatement statement = cnn.prepareStatement(query);
            statement.execute();
            ResultSet data = statement.getResultSet();

            data.next();
            while (!data.isAfterLast()) {
                Author author = new Author();
                String name = data.getString("name_author");
                int id = data.getInt("author_id");
                author.setId(data.getInt("author_id"));
                author.setName(name);
                List<Book> books = new ArrayList<Book>();
                while (data.getInt("author_id") == id) {
                    Book book = new Book();
                    book.setId(data.getInt("book_id"));
                    book.setTitle(data.getString("title"));
                    book.setAuthor(author);
                    book.setAmount(data.getInt("amount"));
                    book.setPrice(data.getDouble("price"));
                    book.setGenre(genreDao.getById(data.getInt("genre_id")));
                    books.add(book);
                    if (!data.next()) {
                        break;
                    }
                }
                author.setBooks(books);
                authors.add(author);
            }
        }


        return authors;
    }

    @Override
    public void save(Author author) throws SQLException {
    try(Connection cnn = DriverManager.getConnection(url, user, password)){
        String queryA = "INSERT INTO author (name_author) VALUES" +
                "(?);";

        PreparedStatement statementA = cnn.prepareStatement(queryA);
        statementA.setString(1, author.getName());
        statementA.executeUpdate();

        if (author.getBooks().size() > 0){
            List<Book> books = author.getBooks();
            String queryB = "INSERT INTO book(title, author_id, genre_id, price, amount) VALUES" +
                    "(?, (SELECT author_id FROM author WHERE name_author = ?), (SELECT genre_id FROM genre WHERE name_genre = ?), ?, ?);";

            PreparedStatement statementB = cnn.prepareStatement(queryB);
            for (Book b : books) {
                statementB.setString(1, b.getTitle());
                statementB.setString(2, author.getName());
                statementB.setString(3, b.getGenre().getName());
                statementB.setDouble(4, b.getPrice());
                statementB.setInt(5, b.getAmount());

                statementB.execute();
            }
        }
        System.out.println("Автор добавлен!");
    }



    }

    @Override
    public void delete(int id) throws SQLException{
        try (Connection cnn = DriverManager.getConnection(url, user, password)){
        String query = "DELETE FROM author WHERE author_id = ?";
        PreparedStatement statement = cnn.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();
            System.out.println("Автор удалён!");
        }

    }
}
