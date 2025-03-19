package com.project.safetynet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.safetynet.controller.FirestationController;
import com.project.safetynet.model.*;
import com.project.safetynet.service.DataLoaderService;
import com.project.safetynet.service.FirestationService;
import com.project.safetynet.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FirestationController firestationController;

    @InjectMocks
    private FirestationService firestationService;

    @Mock
    private DataLoaderService dataLoaderService;

    @Mock
    private PersonService personService;

    @Mock
    private Firestation firestation;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllFirestations() throws Exception {
        mockMvc.perform(get("/api/firestations")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetPersonsByFirestation() throws Exception {
        mockMvc.perform(get("/api/firestation").param("station", "1")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetPhoneFromPersonByFirestation() throws Exception {
        mockMvc.perform(get("/api/phoneAlert").param("firestation", "1")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetAllInfoByStation() throws Exception {
        mockMvc.perform(get("/api/flood/stations").param("stationIds", "1,2"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetPhoneFromPersonByFirestationWithData() throws Exception {
        mockMvc.perform(get("/api/phoneAlert").param("firestation", "1")) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].phone").value("841-874-6512"))
                .andExpect(jsonPath("$[1].phone").value("841-874-8547"))
                .andExpect(jsonPath("$[2].phone").value("841-874-7462"));
    }

    @Test
    public void testAddFirestation() throws Exception {
        String firestationJson = """
                {
                   "address": "1 rue Springboot",
                   "station": "1"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firestationJson))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteFirestation() throws Exception {
        String address = "1 rue Camembert";
        int station = 1;

        mockMvc.perform(delete("/api/firestation")
                        .param("address", address)
                        .param("station", String.valueOf(station)))
                .andExpect(status().isOk()) //
                .andDo(print());
    }





}


