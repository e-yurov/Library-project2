package ru.vsu.cs.yurov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.yurov.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
