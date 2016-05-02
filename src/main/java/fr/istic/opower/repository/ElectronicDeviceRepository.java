package fr.istic.opower.repository;

import fr.istic.opower.domain.ElectronicDevice;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ElectronicDevice entity.
 */
public interface ElectronicDeviceRepository extends JpaRepository<ElectronicDevice,Long> {

}
