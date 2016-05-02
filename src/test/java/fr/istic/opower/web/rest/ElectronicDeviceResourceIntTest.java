package fr.istic.opower.web.rest;

import fr.istic.opower.Application;
import fr.istic.opower.domain.ElectronicDevice;
import fr.istic.opower.repository.ElectronicDeviceRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ElectronicDeviceResource REST controller.
 *
 * @see ElectronicDeviceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ElectronicDeviceResourceIntTest {


    private static final Double DEFAULT_AVG_CONSUMPTION = 1D;
    private static final Double UPDATED_AVG_CONSUMPTION = 2D;

    @Inject
    private ElectronicDeviceRepository electronicDeviceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restElectronicDeviceMockMvc;

    private ElectronicDevice electronicDevice;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ElectronicDeviceResource electronicDeviceResource = new ElectronicDeviceResource();
        ReflectionTestUtils.setField(electronicDeviceResource, "electronicDeviceRepository", electronicDeviceRepository);
        this.restElectronicDeviceMockMvc = MockMvcBuilders.standaloneSetup(electronicDeviceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        electronicDevice = new ElectronicDevice();
        electronicDevice.setAvgConsumption(DEFAULT_AVG_CONSUMPTION);
    }

    @Test
    @Transactional
    public void createElectronicDevice() throws Exception {
        int databaseSizeBeforeCreate = electronicDeviceRepository.findAll().size();

        // Create the ElectronicDevice

        restElectronicDeviceMockMvc.perform(post("/api/electronicDevices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(electronicDevice)))
                .andExpect(status().isCreated());

        // Validate the ElectronicDevice in the database
        List<ElectronicDevice> electronicDevices = electronicDeviceRepository.findAll();
        assertThat(electronicDevices).hasSize(databaseSizeBeforeCreate + 1);
        ElectronicDevice testElectronicDevice = electronicDevices.get(electronicDevices.size() - 1);
        assertThat(testElectronicDevice.getAvgConsumption()).isEqualTo(DEFAULT_AVG_CONSUMPTION);
    }

    @Test
    @Transactional
    public void getAllElectronicDevices() throws Exception {
        // Initialize the database
        electronicDeviceRepository.saveAndFlush(electronicDevice);

        // Get all the electronicDevices
        restElectronicDeviceMockMvc.perform(get("/api/electronicDevices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(electronicDevice.getId().intValue())))
                .andExpect(jsonPath("$.[*].avgConsumption").value(hasItem(DEFAULT_AVG_CONSUMPTION.doubleValue())));
    }

    @Test
    @Transactional
    public void getElectronicDevice() throws Exception {
        // Initialize the database
        electronicDeviceRepository.saveAndFlush(electronicDevice);

        // Get the electronicDevice
        restElectronicDeviceMockMvc.perform(get("/api/electronicDevices/{id}", electronicDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(electronicDevice.getId().intValue()))
            .andExpect(jsonPath("$.avgConsumption").value(DEFAULT_AVG_CONSUMPTION.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingElectronicDevice() throws Exception {
        // Get the electronicDevice
        restElectronicDeviceMockMvc.perform(get("/api/electronicDevices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElectronicDevice() throws Exception {
        // Initialize the database
        electronicDeviceRepository.saveAndFlush(electronicDevice);

		int databaseSizeBeforeUpdate = electronicDeviceRepository.findAll().size();

        // Update the electronicDevice
        electronicDevice.setAvgConsumption(UPDATED_AVG_CONSUMPTION);

        restElectronicDeviceMockMvc.perform(put("/api/electronicDevices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(electronicDevice)))
                .andExpect(status().isOk());

        // Validate the ElectronicDevice in the database
        List<ElectronicDevice> electronicDevices = electronicDeviceRepository.findAll();
        assertThat(electronicDevices).hasSize(databaseSizeBeforeUpdate);
        ElectronicDevice testElectronicDevice = electronicDevices.get(electronicDevices.size() - 1);
        assertThat(testElectronicDevice.getAvgConsumption()).isEqualTo(UPDATED_AVG_CONSUMPTION);
    }

    @Test
    @Transactional
    public void deleteElectronicDevice() throws Exception {
        // Initialize the database
        electronicDeviceRepository.saveAndFlush(electronicDevice);

		int databaseSizeBeforeDelete = electronicDeviceRepository.findAll().size();

        // Get the electronicDevice
        restElectronicDeviceMockMvc.perform(delete("/api/electronicDevices/{id}", electronicDevice.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ElectronicDevice> electronicDevices = electronicDeviceRepository.findAll();
        assertThat(electronicDevices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
