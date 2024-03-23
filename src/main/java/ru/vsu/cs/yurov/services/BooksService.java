package ru.vsu.cs.yurov.services;

import org.hibernate.boot.model.source.spi.Sortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.yurov.models.Book;
import ru.vsu.cs.yurov.models.Person;
import ru.vsu.cs.yurov.repositories.BooksRepository;
import ru.vsu.cs.yurov.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> index(Integer page, Integer booksPerPage, String sort) {
        Sort sortRules = sort.equalsIgnoreCase("true") ?
                Sort.by("year") : Sort.unsorted();

        if (page == null || booksPerPage == null) {
            return booksRepository.findAll(sortRules);
        }
        return booksRepository.findAll(PageRequest.of(page, booksPerPage, sortRules)).getContent();
    }

    public Book show(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBooks) {
        updatedBooks.setId(id);
        booksRepository.save(updatedBooks);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void setHolder(int bookId, Person newHolder) {
        Book book = this.show(bookId);
        book.setHolder(newHolder);
        book.setTimeOfTaking(new Date());
    }

    @Transactional
    public void deleteHolder(int bookId) {
        Book book = this.show(bookId);
        book.setHolder(null);
        book.setTimeOfTaking(null);
    }

    public List<Book> findByQuery(String query) {
        return booksRepository.findByTitleStartingWithIgnoreCase(query);
    }
}
