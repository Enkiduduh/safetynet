package com.project.safetynet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.safetynet.model.Firestation;
import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import com.project.safetynet.service.DataLoaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class DataLoaderServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private DataLoaderService dataLoaderService;

    @Mock
    private ObjectMapper objectMapper;

    // On utilisera un dossier temporaire pour ne pas toucher aux fichiers de production
    @TempDir
    Path tempDir;

    private File testFile;

    @BeforeEach
    public void setup() throws IOException {
        objectMapper = new ObjectMapper().findAndRegisterModules();
        // Création d'un fichier JSON de test dans le dossier temporaire
        testFile = tempDir.resolve("data.json").toFile();
        // Initialiser le fichier JSON avec des données de base
        Map<String, Object> initialData = new HashMap<>();
        initialData.put("persons", new ArrayList<>());
        initialData.put("firestations", new ArrayList<>());
        initialData.put("medicalrecords", new ArrayList<>());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(testFile, initialData);

        // Instancier le DataLoaderService
        dataLoaderService = new DataLoaderService();
        // Injecter le chemin du fichier de test dans dataLoaderService via Reflection (supposant qu'il utilise le champ filePath)
        ReflectionTestUtils.setField(dataLoaderService, "filePath", testFile.getAbsolutePath());
    }

    @Test
    public void testGetDataFromJson() {
        // Appel de la méthode privée via une méthode publique ou en testant indirectement les save*
        Map<String, Object> data = dataLoaderService.getDataFromJson();
        assertNotNull(data);
        assertTrue(data.containsKey("persons"));
        assertTrue(data.containsKey("firestations"));
        assertTrue(data.containsKey("medicalrecords"));
    }

    @Test
    public void testSavePersons() throws IOException {
        List<Person> persons = new ArrayList<>();
        Person p = new Person();
        p.setFirstName("Test");
        p.setLastName("User");
        p.setAddress("123 Test St");
        p.setCity("Testville");
        p.setZip("12345");
        p.setPhone("0000000000");
        p.setEmail("test@example.com");
        persons.add(p);

        dataLoaderService.savePersons(persons);

        // Lire le fichier et vérifier que la clé "persons" contient bien notre liste
        Map<String, Object> data = objectMapper.readValue(testFile, new TypeReference<Map<String, Object>>() {});
        assertTrue(data.containsKey("persons"));
        List<?> personsFromFile = (List<?>) data.get("persons");
        assertEquals(1, personsFromFile.size());
        // Le contenu est généralement sous forme de Map, on peut vérifier quelques champs
        Map<?, ?> personMap = (Map<?, ?>) personsFromFile.get(0);
        assertEquals("Test", personMap.get("firstName"));
        assertEquals("User", personMap.get("lastName"));
    }

    @Test
    public void testSaveFirestations() throws IOException {
        List<Firestation> firestations = new ArrayList<>();
        Firestation f = new Firestation("123 Fire St", 1);
        firestations.add(f);

        dataLoaderService.saveFirestations(firestations);

        Map<String, Object> data = objectMapper.readValue(testFile, new TypeReference<Map<String, Object>>() {});
        assertTrue(data.containsKey("firestations"));
        List<?> firestationsFromFile = (List<?>) data.get("firestations");
        assertEquals(1, firestationsFromFile.size());
        Map<?, ?> firestationMap = (Map<?, ?>) firestationsFromFile.get(0);
        assertEquals("123 Fire St", firestationMap.get("address"));
        // La valeur de station devrait être 1
        // Selon la sérialisation, elle peut être un int ou un Integer
        assertEquals(1, ((Number) firestationMap.get("station")).intValue());
    }

    @Test
    public void testSaveMedicalrecords() throws IOException {
        List<Medicalrecord> medicalrecords = new ArrayList<>();
        Medicalrecord m = new Medicalrecord();
        m.setFirstName("Med");
        m.setLastName("Record");
        m.setBirthdate(LocalDate.of(2000, 1, 1));
        m.setMedications(Arrays.asList("med1:50mg"));
        m.setAllergies(Arrays.asList("allergy1"));
        medicalrecords.add(m);

        dataLoaderService.saveMedicalrecords(medicalrecords);

        Map<String, Object> data = objectMapper.readValue(testFile, new TypeReference<Map<String, Object>>() {});
        assertTrue(data.containsKey("medicalrecords"));
        List<?> medRecordsFromFile = (List<?>) data.get("medicalrecords");
        assertEquals(1, medRecordsFromFile.size());
        Map<?, ?> medRecordMap = (Map<?, ?>) medRecordsFromFile.get(0);
        assertEquals("Med", medRecordMap.get("firstName"));
        // Vérifier la date en fonction du format JSON, par exemple "01/01/2000"
        assertEquals("01/01/2000", medRecordMap.get("birthdate"));
    }
}
