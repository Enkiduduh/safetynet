package com.project.safetynet;

import com.project.safetynet.controller.FirestationController;
import com.project.safetynet.model.*;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private FirestationController firestationController;

    @Mock
    private FirestationService firestationService;

    @Mock
    private PersonService personService;

    @Mock
    private Firestation firestation;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetFirestations() throws Exception {
        mockMvc.perform(get("/api/firestations")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetPhoneFromPersonByFirestation() {
        // Création d'une persone
        Person person = new Person();
        person.setFirstName("Ilyes");
        person.setLastName("Soumar");
        person.setAddress("947 E. Rose Dr");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("841-874-0000");
        person.setEmail("ilyes.soumar@test.fr");


        // Création d'une persone
        Person person2 = new Person();
        person2.setFirstName("Jack");
        person2.setLastName("Jones");
        person2.setAddress("947 E. Rose Dr");
        person2.setCity("Culver");
        person2.setZip("97451");
        person2.setPhone("841-874-7784");
        person2.setEmail("jack.jones@test.fr");

        // Création de l'objet DTO attendu
        PersonPhoneDTO testPersonPhoneDTO = new PersonPhoneDTO(
                person.getPhone()
        );

        PersonPhoneDTO testPersonPhoneDTO2 = new PersonPhoneDTO(
                person2.getPhone()
        );

        // Mock du service
        when(firestationService.getPhoneFromPersonByFirestation(1)).thenReturn(List.of(testPersonPhoneDTO,testPersonPhoneDTO2));

        // Exécution
        List<PersonPhoneDTO> retrievedPhoneList = firestationService.getPhoneFromPersonByFirestation(1);

        // Vérifications
        verify(firestationService, times(1)).getPhoneFromPersonByFirestation(1);

        // Vérifications des résultats
        assertNotNull(testPersonPhoneDTO);
        assertEquals("841-874-0000", testPersonPhoneDTO.getPhone());

        assertNotNull(retrievedPhoneList);
        assertTrue(retrievedPhoneList.size() > 1);
        assertEquals("841-874-7784", retrievedPhoneList.get(1).getPhone());
    }

    @Test
    public void testAddFirestation() {
        String address = "1502 Fire St";
        long stationNumber = 5;

        // Création d'une persone
        Person person = new Person();
        person.setFirstName("Ilyes");
        person.setLastName("Soumar");
        person.setAddress("1502 Fire St");
        person.setCity("Paris");
        person.setZip("94000");
        person.setPhone("0658895313");
        person.setEmail("ilyes.soumar@test.fr");

        // Création de l'objet DTO attendu
        PersonDTO personDTO = new PersonDTO(
                person.getFirstName(),
                person.getLastName(),
                person.getAddress(),
                person.getPhone()
        );
        // Creation d'une firestation
        Firestation firestation = new Firestation();
        firestation.setAddress(address);
        firestation.setId(stationNumber);

        // Mock du service
        when(firestationService.addFirestation(firestation)).thenReturn(firestation);
        when(personService.savePerson(person)).thenReturn(person);
        when(firestationService.getPersonsByFirestation(5)).thenReturn(List.of(personDTO));
        // Execution
        firestationService.addFirestation(firestation);
        personService.savePerson(person);
        List<PersonDTO> retrievePersonDto = firestationService.getPersonsByFirestation(5);
        // Vérifications
        verify(personService, times(1)).savePerson(person);
        verify(firestationService, times(1)).getPersonsByFirestation(5);

        // Vérifications des résultats
        assertEquals(1, retrievePersonDto.size());
        assertEquals("1502 Fire St", retrievePersonDto.get(0).getAddress());
        assertEquals("Soumar", retrievePersonDto.get(0).getLastName());

    }
}
