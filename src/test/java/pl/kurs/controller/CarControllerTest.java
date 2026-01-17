package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.kurs.dto.CarDto;
import pl.kurs.entity.Car;
import pl.kurs.repository.CarRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void before() {
        carRepository.deleteAll();
    }

    @Test
    void shouldReturnCarForGetById() throws Exception {
        //given
        Car savedCar = carRepository.save(createTestCar());

        //when
        MvcResult mvcResult = mockMvc.perform(get("/cars/" + savedCar.getId())
                        .with(httpBasic("adam", "adam"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        CarDto carDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CarDto.class);
        assertCarDto(carDto, savedCar);
    }

    @Test
    void shouldCreateCarSuccessfully() throws Exception {
        //given
        CarDto carDto = new CarDto(null, "BMW", "X5", "WBANA31060B162560", 2020, "W0BMW");

        //when then
        MvcResult mvcResult = mockMvc.perform(post("/cars")
                        .with(httpBasic("adam", "adam"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.producer").value(carDto.getProducer()))
                .andExpect(jsonPath("$.model").value(carDto.getModel()))
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        CarDto createdCar = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CarDto.class);
        assertThat(createdCar.getId()).isNotNull();
        assertThat(carRepository.findById(createdCar.getId())).isPresent();
    }

    @Test
    void shouldUpdateCarSuccessfully() throws Exception {
        //given
        Car savedCar = carRepository.save(createTestCar());
        CarDto updateDto = new CarDto(savedCar.getId(), "Volkswagen", "Passat", "WVWZZZ1K36W612345", 2016, "WX12345");

        //when then
        mockMvc.perform(put("/cars")
                        .with(httpBasic("adam", "adam"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.id").value(savedCar.getId()))
                .andExpect(jsonPath("$.model").value(updateDto.getModel()))
                .andExpect(jsonPath("$.registrationNumber").value(updateDto.getRegistrationNumber()))
                .andReturn();
    }

    @Test
    void shouldDeleteCarSuccessfully() throws Exception {
        //given
        Car savedCar = carRepository.save(createTestCar());

        //when then
        mockMvc.perform(delete("/cars/" + savedCar.getId())
                        .with(httpBasic("adam", "adam")))
                .andExpect(status().isOk());

        //then
        assertThat(carRepository.findById(savedCar.getId())).isEmpty();
    }

    @Test
    void shouldReturn404WhenCarNotFound() throws Exception {
        //when then
        mockMvc.perform(get("/cars/" + 999L)
                        .with(httpBasic("adam", "adam"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400ForInvalidCarId() throws Exception {
        //when then
        mockMvc.perform(get("/cars/invalid-id")
                        .with(httpBasic("adam", "adam"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForNegativeCarId() throws Exception {
        //when then
        mockMvc.perform(get("/cars/-1")
                        .with(httpBasic("adam", "adam"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForZeroCarId() throws Exception {
        //when then
        mockMvc.perform(get("/cars/0")
                        .with(httpBasic("adam", "adam"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn401WhenNoAuthentication() throws Exception {
        //given
        Car savedCar = carRepository.save(createTestCar());

        //when then
        mockMvc.perform(get("/cars/" + savedCar.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn401WhenInvalidCredentials() throws Exception {
        //given
        Car savedCar = carRepository.save(createTestCar());

        //when then
        mockMvc.perform(get("/cars/" + savedCar.getId())
                        .with(httpBasic("adam", "wrongpassword"))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private Car createTestCar() {
        return new Car("Volkswagen", "Golf", "WVWZZZ1K36W612345", 2016, "KR1ABC");
    }

    private void assertCarDto(CarDto carDto, Car expectedCar) {
        assertThat(carDto).isNotNull();
        assertThat(carDto.getId()).isEqualTo(expectedCar.getId());
        assertThat(carDto.getProducer()).isEqualTo(expectedCar.getProducer());
        assertThat(carDto.getModel()).isEqualTo(expectedCar.getModel());
        assertThat(carDto.getYearOfProduction()).isEqualTo(expectedCar.getYearOfProduction());
        assertThat(carDto.getRegistrationNumber()).isEqualTo(expectedCar.getRegistrationNumber());
        assertThat(carDto.getVinNumber()).isEqualTo(expectedCar.getVinNumber());
    }

}