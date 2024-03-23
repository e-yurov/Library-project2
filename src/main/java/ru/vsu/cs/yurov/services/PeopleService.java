package ru.vsu.cs.yurov.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.yurov.models.Book;
import ru.vsu.cs.yurov.models.Person;
import ru.vsu.cs.yurov.repositories.PeopleRepository;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> index() {
        return peopleRepository.findAll();
    }

    public Person show(int id) {
        Person person = peopleRepository.findById(id).orElse(null);
        for (Book book : person.getBooks()) {
            Calendar bookCalendar = new GregorianCalendar();
            bookCalendar.setTime(book.getTimeOfTaking());
            bookCalendar.add(Calendar.DAY_OF_MONTH, 10);
            book.setOverdue(bookCalendar.before(new GregorianCalendar()));
        }
        return person;
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
