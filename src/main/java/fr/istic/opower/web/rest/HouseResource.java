package fr.istic.opower.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.istic.opower.domain.House;
import fr.istic.opower.repository.HouseRepository;
import fr.istic.opower.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing House.
 */
@RestController
@RequestMapping("/api")
public class HouseResource {

    private final Logger log = LoggerFactory.getLogger(HouseResource.class);
        
    @Inject
    private HouseRepository houseRepository;
    
    /**
     * POST  /houses -> Create a new house.
     */
    @RequestMapping(value = "/houses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<House> createHouse(@RequestBody House house) throws URISyntaxException {
        log.debug("REST request to save House : {}", house);
        if (house.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("house", "idexists", "A new house cannot already have an ID")).body(null);
        }
        House result = houseRepository.save(house);
        return ResponseEntity.created(new URI("/api/houses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("house", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /houses -> Updates an existing house.
     */
    @RequestMapping(value = "/houses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<House> updateHouse(@RequestBody House house) throws URISyntaxException {
        log.debug("REST request to update House : {}", house);
        if (house.getId() == null) {
            return createHouse(house);
        }
        House result = houseRepository.save(house);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("house", house.getId().toString()))
            .body(result);
    }

    /**
     * GET  /houses -> get all the houses.
     */
    @RequestMapping(value = "/houses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<House> getAllHouses() {
        log.debug("REST request to get all Houses");
        return houseRepository.findAll();
            }

    /**
     * GET  /houses/:id -> get the "id" house.
     */
    @RequestMapping(value = "/houses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<House> getHouse(@PathVariable Long id) {
        log.debug("REST request to get House : {}", id);
        House house = houseRepository.findOne(id);
        return Optional.ofNullable(house)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /houses/:id -> delete the "id" house.
     */
    @RequestMapping(value = "/houses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHouse(@PathVariable Long id) {
        log.debug("REST request to delete House : {}", id);
        houseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("house", id.toString())).build();
    }
}
