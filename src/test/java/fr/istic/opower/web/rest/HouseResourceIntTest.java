package fr.istic.opower.web.rest;

import fr.istic.opower.Application;
import fr.istic.opower.domain.House;
import fr.istic.opower.repository.HouseRepository;

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
 * Test class for the HouseResource REST controller.
 *
 * @see HouseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HouseResourceIntTest {


    private static final Double DEFAULT_SQUARE_MATERS = 1D;
    private static final Double UPDATED_SQUARE_MATERS = 2D;

    private static final Integer DEFAULT_NOMBER_OF_ROOMS = 1;
    private static final Integer UPDATED_NOMBER_OF_ROOMS = 2;

    @Inject
    private HouseRepository houseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHouseMockMvc;

    private House house;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HouseResource houseResource = new HouseResource();
        ReflectionTestUtils.setField(houseResource, "houseRepository", houseRepository);
        this.restHouseMockMvc = MockMvcBuilders.standaloneSetup(houseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        house = new House();
        house.setSquareMaters(DEFAULT_SQUARE_MATERS);
        house.setNomberOfRooms(DEFAULT_NOMBER_OF_ROOMS);
    }

    @Test
    @Transactional
    public void createHouse() throws Exception {
        int databaseSizeBeforeCreate = houseRepository.findAll().size();

        // Create the House

        restHouseMockMvc.perform(post("/api/houses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(house)))
                .andExpect(status().isCreated());

        // Validate the House in the database
        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeCreate + 1);
        House testHouse = houses.get(houses.size() - 1);
        assertThat(testHouse.getSquareMaters()).isEqualTo(DEFAULT_SQUARE_MATERS);
        assertThat(testHouse.getNomberOfRooms()).isEqualTo(DEFAULT_NOMBER_OF_ROOMS);
    }

    @Test
    @Transactional
    public void getAllHouses() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houses
        restHouseMockMvc.perform(get("/api/houses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(house.getId().intValue())))
                .andExpect(jsonPath("$.[*].squareMaters").value(hasItem(DEFAULT_SQUARE_MATERS.doubleValue())))
                .andExpect(jsonPath("$.[*].nomberOfRooms").value(hasItem(DEFAULT_NOMBER_OF_ROOMS)));
    }

    @Test
    @Transactional
    public void getHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get the house
        restHouseMockMvc.perform(get("/api/houses/{id}", house.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(house.getId().intValue()))
            .andExpect(jsonPath("$.squareMaters").value(DEFAULT_SQUARE_MATERS.doubleValue()))
            .andExpect(jsonPath("$.nomberOfRooms").value(DEFAULT_NOMBER_OF_ROOMS));
    }

    @Test
    @Transactional
    public void getNonExistingHouse() throws Exception {
        // Get the house
        restHouseMockMvc.perform(get("/api/houses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

		int databaseSizeBeforeUpdate = houseRepository.findAll().size();

        // Update the house
        house.setSquareMaters(UPDATED_SQUARE_MATERS);
        house.setNomberOfRooms(UPDATED_NOMBER_OF_ROOMS);

        restHouseMockMvc.perform(put("/api/houses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(house)))
                .andExpect(status().isOk());

        // Validate the House in the database
        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeUpdate);
        House testHouse = houses.get(houses.size() - 1);
        assertThat(testHouse.getSquareMaters()).isEqualTo(UPDATED_SQUARE_MATERS);
        assertThat(testHouse.getNomberOfRooms()).isEqualTo(UPDATED_NOMBER_OF_ROOMS);
    }

    @Test
    @Transactional
    public void deleteHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

		int databaseSizeBeforeDelete = houseRepository.findAll().size();

        // Get the house
        restHouseMockMvc.perform(delete("/api/houses/{id}", house.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
