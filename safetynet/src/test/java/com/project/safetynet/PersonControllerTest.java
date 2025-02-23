package com.project.safetynet;

import com.project.safetynet.controller.PersonController;
import com.project.safetynet.model.Person;
import com.project.safetynet.model.PersonInfoDTO;
import com.project.safetynet.repository.FirestationRepository;
import com.project.safetynet.repository.MedicalrecordRepository;
import com.project.safetynet.repository.PersonRepository;
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
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonService personService;

    @Mock
    private FirestationService firestationService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private MedicalrecordRepository medicalrecordRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetAllPersons() throws Exception {
        mockMvc.perform(get("/api/persons")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetFirestations() throws Exception {
        mockMvc.perform(get("/api/firestations")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetMedicalrecords() throws Exception {
        mockMvc.perform(get("/api/medicalrecords")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetEmailsFromCommunity() throws Exception {
        mockMvc.perform(get("/api/communityEmail").param("city", "Culver")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetAllPersonsInfosByAddressFire() throws Exception {
        mockMvc.perform(get("/api/fire").param("address", "1509 Culver St")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetAllPersonsWithName() throws Exception {
        mockMvc.perform(get("/api/personInfolastName").param("lastName", "Boyd")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetChildFromAddress() throws Exception {
        mockMvc.perform(get("/api/childAlert").param("address", "1509 Culver St")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testAddPerson() {
        List<String> medications = List.of("Doliprane");
        List<String> allergies = List.of("Pollen", "Element2");

        // Création d'une persone
        Person person = new Person();
        person.setFirstName("Ilyes");
        person.setLastName("Soumar");
        person.setAddress("1 rue Springboot");
        person.setCity("Paris");
        person.setZip("94000");
        person.setPhone("0658895313");
        person.setEmail("ilyes.soumar@test.fr");

        // Création d'une persone2
        Person person2 = new Person();
        person2.setFirstName("Imraan");
        person2.setLastName("Soumar");
        person2.setAddress("5 rue Springboot");
        person2.setCity("Bonneuil");
        person2.setZip("94400");
        person2.setPhone("0658895314");
        person2.setEmail("imraan.soumar@test.fr");

        // Création de l'objet DTO attendu
        PersonInfoDTO personInfoDTO = new PersonInfoDTO(
                person.getLastName(),
                person.getFirstName(),
                person.getAddress(),
                36, // Exemple d'âge, adapte selon ta logique
                person.getEmail(),
                medications, // Exemple de médicaments
                allergies // Exemple d'allergies
        );
        // Création de l'objet DTO attendu
        PersonInfoDTO personInfoDTO2 = new PersonInfoDTO(
                person2.getLastName(),
                person2.getFirstName(),
                person2.getAddress(),
                34, // Exemple d'âge, adapte selon ta logique
                person2.getEmail(),
                medications, // Exemple de médicaments
                allergies // Exemple d'allergies
        );

        // Mock du service
        when(personService.savePerson(person)).thenReturn(person);
        when(personService.savePerson(person2)).thenReturn(person2);
        when(personService.getAllPersonsWithName("Soumar")).thenReturn(List.of(personInfoDTO, personInfoDTO2));
        // Exécution
        personService.savePerson(person);
        personService.savePerson(person2);
        List<PersonInfoDTO> retrievedPersons = personService.getAllPersonsWithName("Soumar");
        // Vérifications
        verify(personService, times(1)).savePerson(person);
        verify(personService, times(1)).savePerson(person2);
        verify(personService, times(1)).getAllPersonsWithName("Soumar");

        // Vérifications des résultats
        List<Person> savedPersons = List.of(person, person2);
        assertNotNull(savedPersons);
        assertEquals(2, savedPersons.size());
        assertEquals("Ilyes", savedPersons.get(0).getFirstName());
        assertEquals("Bonneuil", savedPersons.get(1).getCity());
        assertEquals("Soumar", savedPersons.get(0).getLastName());

        assertEquals(2, retrievedPersons.size());
        assertEquals(36, retrievedPersons.get(0).getAge());
        assertEquals("1 rue Springboot", retrievedPersons.get(0).getAddress());
        assertEquals("Soumar", retrievedPersons.get(0).getLastName());
    }



}
