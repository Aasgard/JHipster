package fr.istic.opower.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.istic.opower.domain.Heater;
import fr.istic.opower.repository.HeaterRepository;
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
 * REST controller for managing Heater.
 */
@RestController
@RequestMapping("/api")
public class HeaterResource {

    private final Logger log = LoggerFactory.getLogger(HeaterResource.class);
        
    @Inject
    private HeaterRepository heaterRepository;
    
    /**
     * POST  /heaters -> Create a new heater.
     */
    @RequestMapping(value = "/heaters",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Heater> createHeater(@RequestBody Heater heater) throws URISyntaxException {
        log.debug("REST request to save Heater : {}", heater);
        if (heater.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("heater", "idexists", "A new heater cannot already have an ID")).body(null);
        }
        Heater result = heaterRepository.save(heater);
        return ResponseEntity.created(new URI("/api/heaters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("heater", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /heaters -> Updates an existing heater.
     */
    @RequestMapping(value = "/heaters",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Heater> updateHeater(@RequestBody Heater heater) throws URISyntaxException {
        log.debug("REST request to update Heater : {}", heater);
        if (heater.getId() == null) {
            return createHeater(heater);
        }
        Heater result = heaterRepository.save(heater);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("heater", heater.getId().toString()))
            .body(result);
    }

    /**
     * GET  /heaters -> get all the heaters.
     */
    @RequestMapping(value = "/heaters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Heater> getAllHeaters() {
        log.debug("REST request to get all Heaters");
        return heaterRepository.findAll();
            }

    /**
     * GET  /heaters/:id -> get the "id" heater.
     */
    @RequestMapping(value = "/heaters/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Heater> getHeater(@PathVariable Long id) {
        log.debug("REST request to get Heater : {}", id);
        Heater heater = heaterRepository.findOne(id);
        return Optional.ofNullable(heater)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /heaters/:id -> delete the "id" heater.
     */
    @RequestMapping(value = "/heaters/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHeater(@PathVariable Long id) {
        log.debug("REST request to delete Heater : {}", id);
        heaterRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("heater", id.toString())).build();
    }
}
