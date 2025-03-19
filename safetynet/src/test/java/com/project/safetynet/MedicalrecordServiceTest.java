package com.project.safetynet;

import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import com.project.safetynet.service.DataLoaderService;
import com.project.safetynet.service.MedicalrecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalrecordServiceTest {

    @Mock
    private DataLoaderService dataLoaderService;

    @InjectMocks
    private MedicalrecordService medicalrecordService;

    private Person person;
    private Medicalrecord medicalrecord;

    @BeforeEach
    public void setup() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Création d'un objet Person pour les tests
        person = new Person();
        person.setFirstName("Brian");
        person.setLastName("Stelzer");
        person.setAddress("947 E. Rose Dr");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("841-874-7784");
        person.setEmail("bstel@email.com");


        // Création d'un dossier médical correspondant
        medicalrecord = new Medicalrecord();
        medicalrecord.setFirstName("Brian");
        medicalrecord.setLastName("Stelzer");
        medicalrecord.setBirthdate(LocalDate.of(1975, 6, 12));
        medicalrecord.setMedications(Arrays.asList("ibupurin:200mg", "hydrapermazol:400mg"));
        medicalrecord.setAllergies(Arrays.asList("nillacilan"));
    }

//    @Test
//    public void testCalculAge_RecordFound() {
//        // Simuler qu'un dossier médical correspondant est trouvé
//        doReturn(List.of(medicalrecord)).when(dataLoaderService).getMedicalrecords();
//
//        int age = medicalrecordService.calculAge(person);
//        // Calcule l'âge attendu
//        int expectedAge = Period.between(LocalDate.of(1975, 6, 12), LocalDate.now()).getYears();
//        assertEquals(expectedAge, age, "L'âge calculé devrait correspondre à l'âge attendu");
//    }

    @Test
    public void testCalculAge_NoRecordFound() {
        // Simuler qu'aucun dossier médical n'est trouvé
        lenient().when(dataLoaderService.getMedicalrecords()).thenReturn(new ArrayList<>());

        int age = medicalrecordService.calculAge(person);
        assertEquals(0, age, "Si aucun dossier n'est trouvé, l'âge doit être 0");
    }

//    @Test
//    public void testRecoverMedications_RecordFound() {
//        // Simuler qu'un dossier médical correspondant est trouvé
//        lenient().when(dataLoaderService.getMedicalrecords()).thenReturn(List.of(medicalrecord));
//
//        List<String> medications = medicalrecordService.recoverMedications(person);
//        assertNotNull(medications, "La liste des médicaments ne doit pas être null");
//        assertEquals(2, medications.size(), "La liste devrait contenir 2 médicaments");
//        assertTrue(medications.contains("Med1"));
//        assertTrue(medications.contains("Med2"));
//    }

    @Test
    public void testRecoverMedications_NoRecordFound() {
        // Simuler qu'aucun dossier médical n'est trouvé
        lenient().when(dataLoaderService.getMedicalrecords()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicalrecordService.recoverMedications(person);
        });
        String expectedMessage = "Aucun dossier médical trouvé";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

//    @Test
//    public void testRecoverAllergies_RecordFound() {
//        // Simuler qu'un dossier médical correspondant est trouvé
//        doReturn(List.of(medicalrecord)).when(dataLoaderService).getMedicalrecords();
//
//        List<String> allergies = medicalrecordService.recoverAllergies(person);
//
//        assertNotNull(allergies, "La liste des allergies ne doit pas être null");
//        assertEquals(1, allergies.size(), "La liste devrait contenir 1 allergie");
//        assertTrue(allergies.contains("Allergy1"));
//    }

    @Test
    public void testRecoverAllergies_NoRecordFound() {
        // Simuler qu'aucun dossier médical n'est trouvé
        lenient().when(dataLoaderService.getMedicalrecords()).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicalrecordService.recoverAllergies(person);
        });
        String expectedMessage = "Aucun dossier médical trouvé";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
