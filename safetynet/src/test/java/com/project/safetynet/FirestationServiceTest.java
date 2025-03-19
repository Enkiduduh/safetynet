//package com.project.safetynet;
//
//import com.project.safetynet.model.Firestation;
//import com.project.safetynet.service.DataLoaderService;
//import com.project.safetynet.service.FirestationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
////@ExtendWith(MockitoExtension.class)
//public class FirestationServiceTest {
//
//    @Mock
//    private DataLoaderService dataLoaderService;
//
//    @InjectMocks
//    private FirestationService firestationService;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    private List<Firestation> loadFirestationsFromJson() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        File file = new File("src/main/resources/data.json");
//        try {
//            Map<String, Object> data = objectMapper.readValue(file, new TypeReference<>() {
//            });
//            List<Map<String, Object>> firestationsData = (List<Map<String, Object>>) data.get("firestations");
//            return firestationsData.stream()
//                    .map(entry -> new Firestation(
//                            (String) entry.get("address"),
//                            Integer.parseInt(entry.get("station").toString())
//                    ))
//                    .collect(Collectors.toList());
//        } catch (IOException e) {
//            throw new RuntimeException("Erreur lors du chargement du fichier JSON : " + e.getMessage(), e);
//        }
//    }
//
//    @Test
//    public void testGetFirestations() {
//        List<Firestation> firestationsFromJson = loadFirestationsFromJson();
//        when(dataLoaderService.getFirestations()).thenReturn(firestationsFromJson);
//
//        List<Firestation> firestations = firestationService.getFirestations();
//        System.out.println("Firestations récupérées : " + firestations);
//
//        assertNotNull(firestations);
//        assertFalse(firestations.isEmpty(), "La liste des firestations ne doit pas être vide");
//        assertEquals(firestationsFromJson.size(), firestations.size(), "Le nombre de firestations ne correspond pas");
//        verify(dataLoaderService, times(1)).getFirestations();
//    }
//}
