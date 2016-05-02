package fr.istic.opower.web.rest;

import fr.istic.opower.Application;
import fr.istic.opower.domain.Heater;
import fr.istic.opower.repository.HeaterRepository;

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
 * Test class for the HeaterResource REST controller.
 *
 * @see HeaterResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HeaterResourceIntTest {


    private static final Double DEFAULT_AVG_CONSUMPTION = 1D;
    private static final Double UPDATED_AVG_CONSUMPTION = 2D;

    @Inject
    private HeaterRepository heaterRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHeaterMockMvc;

    private Heater heater;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HeaterResource heaterResource = new HeaterResource();
        ReflectionTestUtils.setField(heaterResource, "heaterRepository", heaterRepository);
        this.restHeaterMockMvc = MockMvcBuilders.standaloneSetup(heaterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        heater = new Heater();
        heater.setAvgConsumption(DEFAULT_AVG_CONSUMPTION);
    }

    @Test
    @Transactional
    public void createHeater() throws Exception {
        int databaseSizeBeforeCreate = heaterRepository.findAll().size();

        // Create the Heater

        restHeaterMockMvc.perform(post("/api/heaters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(heater)))
                .andExpect(status().isCreated());

        // Validate the Heater in the database
        List<Heater> heaters = heaterRepository.findAll();
        assertThat(heaters).hasSize(databaseSizeBeforeCreate + 1);
        Heater testHeater = heaters.get(heaters.size() - 1);
        assertThat(testHeater.getAvgConsumption()).isEqualTo(DEFAULT_AVG_CONSUMPTION);
    }

    @Test
    @Transactional
    public void getAllHeaters() throws Exception {
        // Initialize the database
        heaterRepository.saveAndFlush(heater);

        // Get all the heaters
        restHeaterMockMvc.perform(get("/api/heaters?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(heater.getId().intValue())))
                .andExpect(jsonPath("$.[*].avgConsumption").value(hasItem(DEFAULT_AVG_CONSUMPTION.doubleValue())));
    }

    @Test
    @Transactional
    public void getHeater() throws Exception {
        // Initialize the database
        heaterRepository.saveAndFlush(heater);

        // Get the heater
        restHeaterMockMvc.perform(get("/api/heaters/{id}", heater.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(heater.getId().intValue()))
            .andExpect(jsonPath("$.avgConsumption").value(DEFAULT_AVG_CONSUMPTION.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHeater() throws Exception {
        // Get the heater
        restHeaterMockMvc.perform(get("/api/heaters/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHeater() throws Exception {
        // Initialize the database
        heaterRepository.saveAndFlush(heater);

		int databaseSizeBeforeUpdate = heaterRepository.findAll().size();

        // Update the heater
        heater.setAvgConsumption(UPDATED_AVG_CONSUMPTION);

        restHeaterMockMvc.perform(put("/api/heaters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(heater)))
                .andExpect(status().isOk());

        // Validate the Heater in the database
        List<Heater> heaters = heaterRepository.findAll();
        assertThat(heaters).hasSize(databaseSizeBeforeUpdate);
        Heater testHeater = heaters.get(heaters.size() - 1);
        assertThat(testHeater.getAvgConsumption()).isEqualTo(UPDATED_AVG_CONSUMPTION);
    }

    @Test
    @Transactional
    public void deleteHeater() throws Exception {
        // Initialize the database
        heaterRepository.saveAndFlush(heater);

		int databaseSizeBeforeDelete = heaterRepository.findAll().size();

        // Get the heater
        restHeaterMockMvc.perform(delete("/api/heaters/{id}", heater.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Heater> heaters = heaterRepository.findAll();
        assertThat(heaters).hasSize(databaseSizeBeforeDelete - 1);
    }
}
