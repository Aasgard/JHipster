package fr.istic.opower.repository;

import fr.istic.opower.domain.Heater;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Heater entity.
 */
public interface HeaterRepository extends JpaRepository<Heater,Long> {

}
