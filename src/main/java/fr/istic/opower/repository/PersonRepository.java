package fr.istic.opower.repository;

import fr.istic.opower.domain.Person;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Person entity.
 */
public interface PersonRepository extends JpaRepository<Person,Long> {

    @Query("select distinct person from Person person left join fetch person.friendss")
    List<Person> findAllWithEagerRelationships();

    @Query("select person from Person person left join fetch person.friendss where person.id =:id")
    Person findOneWithEagerRelationships(@Param("id") Long id);

}
