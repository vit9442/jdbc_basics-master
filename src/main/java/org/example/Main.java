package org.example;

import org.example.dao.AuthorDao;
import org.example.dao.AuthorDaoImpl;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Genre;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Не удалось загрузить драйвер");
//        }

        String url = "jdbc:postgresql://localhost:5432/lesson_2";
        String user = "postgres";
        String password = "123";

        AuthorDao dao = new AuthorDaoImpl(url, user, password);
        //Author author = dao.getById(2);
        //System.out.println("Author: " + author.getName());
        //System.out.println("Books: ");
        //author.getBooks().forEach(book -> System.out.println(book.getTitle()));
       Book book1 = new Book("Книга1", new Genre("Роман"), 450.00, 10);
       Book book2 = new Book("Книга2", new Genre("Приключения"), 600.50, 4);
        List<Book> books =  Arrays.asList(book1, book2);
        Author author = new Author();
        author.setName("Новый автор");
        author.setBooks(books);

        //dao.save(author); //Сохранение автора
        Scanner s = new Scanner(System.in);
        System.out.println("Введите номер автора, которога нужно удалить: ");
        int num = s.nextInt();
        dao.delete(num); // удаление автора
        List<Author> authors = dao.getAll(); // получить всех авторов



        for(Author a: authors){
            System.out.println(a.getName());
            System.out.println("Книги:");
        for (Book b: a.getBooks())
            System.out.printf("%s, Количество: %d, Цена: %f, Жанр: %s\n",b.getTitle(),b.getAmount(),b.getPrice(),b.getGenre().getName());
            System.out.println();
        }
    }
}
