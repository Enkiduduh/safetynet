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
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
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
    private Medicalrecord originalRecord;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Création d'un objet Person pour les tests
        person = new Person();
        person.setFirstName("Ilyes");
        person.setLastName("Soumar");
        // Autres champs non utilisés ici

        // Création d'un dossier médical correspondant (original)
        originalRecord = new Medicalrecord();
        originalRecord.setFirstName("Ilyes");
        originalRecord.setLastName("Soumar");
        originalRecord.setBirthdate(LocalDate.of(1988, 11, 4));
        originalRecord.setMedications(Arrays.asList("doliprane:1000mg"));
        originalRecord.setAllergies(Arrays.asList("peanut"));

        // Préparer une liste contenant le dossier médical original
        List<Medicalrecord> mockRecords = new ArrayList<>();
        mockRecords.add(originalRecord);

        // Forcer le stub pour que dataLoaderService.getMedicalrecords() retourne mockRecords
        doReturn(mockRecords).when(dataLoaderService).getMedicalrecords();

        // Optionnel : Remplacer la liste interne chargée via @PostConstruct dans MedicalrecordService par notre liste mockée
        ReflectionTestUtils.setField(medicalrecordService, "medicalrecords", mockRecords);
    }

//    @Test
//    public void testUpdateMedicalrecord_RecordFound() {
//        // Préparer une liste contenant le dossier médical original
//        List<Medicalrecord> mockRecords = new ArrayList<>();
//        mockRecords.add(originalRecord);
//
//        // Forcer le stub pour que dataLoaderService.getMedicalrecords() retourne mockRecords
//        doReturn(mockRecords).when(dataLoaderService).getMedicalrecords();
//        System.out.println("Mock records: " + mockRecords);
//
//        // Créer un dossier de mise à jour avec les mêmes firstName et lastName que originalRecord
//        Medicalrecord updateRecord = new Medicalrecord();
//        updateRecord.setFirstName("Ilyes");
//        updateRecord.setLastName("Soumar");
//        // Modifier les champs modifiables
//        updateRecord.setBirthdate(LocalDate.of(1980, 1, 1));  // Nouvelle date de naissance
//        updateRecord.setMedications(Arrays.asList("newMed:100mg"));
//        updateRecord.setAllergies(Arrays.asList("newAllergy"));
//
//        // Appeler la méthode à tester
//        try {
//            medicalrecordService.updateMedicalrecord(updateRecord);
//            System.out.println("Mise à jour réussie.");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Récupérer le dossier mis à jour dans la liste (celui qui correspond aux critères)
//        Medicalrecord updatedRecord = mockRecords.stream()
//                .filter(m -> m.getFirstName().equalsIgnoreCase("Ilyes") &&
//                        m.getLastName().equalsIgnoreCase("Soumar"))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Dossier non trouvé après mise à jour"));
//
//        System.out.println("Updated record retrieved: " + updatedRecord);
//
//        // Vérifier que le dossier mis à jour a bien les nouvelles valeurs
//        assertEquals(LocalDate.of(1980, 1, 1), updatedRecord.getBirthdate(), "La date de naissance doit être mise à jour");
//        assertEquals(Arrays.asList("newMed:100mg"), updatedRecord.getMedications(), "Les médicaments doivent être mis à jour");
//        assertEquals(Arrays.asList("newAllergy"), updatedRecord.getAllergies(), "Les allergies doivent être mises à jour");
//
//        // Vérifier que la méthode saveMedicalrecords a été appelée avec la liste mockRecords
//        verify(dataLoaderService, times(1)).saveMedicalrecords(mockRecords);
//    }




    @Test
    public void testCalculAge_NoRecordFound() {
        // Simuler qu'aucun dossier médical n'est trouvé
        lenient().when(dataLoaderService.getMedicalrecords()).thenReturn(new ArrayList<>());

        int age = medicalrecordService.calculAge(person);
        assertEquals(0, age, "Si aucun dossier n'est trouvé, l'âge doit être 0");
    }


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
