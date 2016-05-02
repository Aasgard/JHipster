package fr.istic.opower.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.istic.opower.domain.ElectronicDevice;
import fr.istic.opower.repository.ElectronicDeviceRepository;
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
 * REST controller for managing ElectronicDevice.
 */
@RestController
@RequestMapping("/api")
public class ElectronicDeviceResource {

    private final Logger log = LoggerFactory.getLogger(ElectronicDeviceResource.class);
        
    @Inject
    private ElectronicDeviceRepository electronicDeviceRepository;
    
    /**
     * POST  /electronicDevices -> Create a new electronicDevice.
     */
    @RequestMapping(value = "/electronicDevices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ElectronicDevice> createElectronicDevice(@RequestBody ElectronicDevice electronicDevice) throws URISyntaxException {
        log.debug("REST request to save ElectronicDevice : {}", electronicDevice);
        if (electronicDevice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("electronicDevice", "idexists", "A new electronicDevice cannot already have an ID")).body(null);
        }
        ElectronicDevice result = electronicDeviceRepository.save(electronicDevice);
        return ResponseEntity.created(new URI("/api/electronicDevices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("electronicDevice", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /electronicDevices -> Updates an existing electronicDevice.
     */
    @RequestMapping(value = "/electronicDevices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ElectronicDevice> updateElectronicDevice(@RequestBody ElectronicDevice electronicDevice) throws URISyntaxException {
        log.debug("REST request to update ElectronicDevice : {}", electronicDevice);
        if (electronicDevice.getId() == null) {
            return createElectronicDevice(electronicDevice);
        }
        ElectronicDevice result = electronicDeviceRepository.save(electronicDevice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("electronicDevice", electronicDevice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /electronicDevices -> get all the electronicDevices.
     */
    @RequestMapping(value = "/electronicDevices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ElectronicDevice> getAllElectronicDevices() {
        log.debug("REST request to get all ElectronicDevices");
        return electronicDeviceRepository.findAll();
            }

    /**
     * GET  /electronicDevices/:id -> get the "id" electronicDevice.
     */
    @RequestMapping(value = "/electronicDevices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ElectronicDevice> getElectronicDevice(@PathVariable Long id) {
        log.debug("REST request to get ElectronicDevice : {}", id);
        ElectronicDevice electronicDevice = electronicDeviceRepository.findOne(id);
        return Optional.ofNullable(electronicDevice)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /electronicDevices/:id -> delete the "id" electronicDevice.
     */
    @RequestMapping(value = "/electronicDevices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteElectronicDevice(@PathVariable Long id) {
        log.debug("REST request to delete ElectronicDevice : {}", id);
        electronicDeviceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("electronicDevice", id.toString())).build();
    }
}
