package com.project.safetynet;

import com.project.safetynet.controller.PersonController;
import com.project.safetynet.service.FirestationService;
import com.project.safetynet.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private PersonController personController; //  Injecte le mock dans le contrôleur

    @Mock
    private PersonService personService; // Mock du service pour intercepter les appels

    @Mock
    private FirestationService firestationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPersons() throws Exception {
        mockMvc.perform(get("/api/persons")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetEmailsFromCommunity() throws Exception {
        mockMvc.perform(get("/api/communityEmail").param("city", "Culver"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetAllPersonsInfosByAddressFire() throws Exception {
        mockMvc.perform(get("/api/fire").param("address", "1509 Culver St"))
                .andExpect(status().isOk()).andDo(print());
    }


    @Test
    public void testGetMedicalrecords() throws Exception {
        mockMvc.perform(get("/api/medicalrecords")).andExpect(status().isOk()).andDo(print());
    }


    @Test
    public void testGetAllPersonsWithName() throws Exception {
        mockMvc.perform(get("/api/personInfoLastName").param("lastName", "Boyd"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetChildFromAddress() throws Exception {
        mockMvc.perform(get("/api/childAlert").param("address", "1509 Culver St"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testAddPerson() throws Exception {
        String personJson = """
                {
                    "firstName": "Ilyes",
                    "lastName": "Soumar",
                    "address": "1 rue Springboot",
                    "city": "Paris",
                    "zip": "94000",
                    "phone": "0658895313",
                    "email": "ilyes.soumar@test.fr"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeletePerson() throws Exception {
        String firstName = "Ilyes";
        String lastName = "Soumar";

        mockMvc.perform(delete("/api/person")
                        .param("firstName", firstName)
                        .param("lastName", lastName))
                .andExpect(status().isOk()) //
                .andDo(print());
    }


}
