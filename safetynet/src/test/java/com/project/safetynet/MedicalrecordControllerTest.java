package com.project.safetynet;

import com.project.safetynet.controller.MedicalrecordController;
import com.project.safetynet.service.MedicalrecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
//@WebMvcTest(MedicalrecordController.class)
public class MedicalrecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private MedicalrecordController medicalrecordController; //  Injecte le mock dans le contrôleur

    @Mock
    private MedicalrecordService medicalrecordService; // Mock du service pour intercepter les appels



    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddMedicalrecord() throws Exception {
        String medicalrecordJson = """
                {
                    "firstName" : "Ilyes",
                    "lastName" : "Soumar",
                    "birthdate" : "04/11/1988",
                    "medications" : [ "doliprane:1000mg" ],
                    "allergies" : [ "peanut" ]
                  }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/medicalrecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicalrecordJson))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteMedicalrecord() throws Exception {
        String firstName = "Ilyes";
        String lastName = "Soumar";

        mockMvc.perform(delete("/api/medicalrecord")
                        .param("firstName", firstName)
                        .param("lastName", lastName))
                .andExpect(status().isOk()) //
                .andDo(print());
    }

//    @Test
//    public void testUpdateMedicalrecord() throws Exception {
//        // Prépare un JSON de mise à jour avec les champs requis, y compris firstName et lastName
//        String updateJson = """
//                {
//                    "firstName": "Ilyes",
//                    "lastName": "Soumar",
//                    "birthdate": "01/01/1980",
//                    "medications": ["newMed:100mg"],
//                    "allergies": ["newAllergy"]
//                }
//                """;
//
//        // Simule le comportement de updateMedicalrecord (méthode void)
//        doNothing().when(medicalrecordService).updateMedicalrecord(ArgumentMatchers.any());
//
//        mockMvc.perform(put("/api/medicalrecord")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updateJson))
//                .andExpect(status().isOk())
//                // Vérifie que le message de réponse contient "Medicalrecord updated successfully."
//                .andExpect(content().string(containsString("Medicalrecord updated successfully.")));
//
//        // Vérifie que le service a bien été appelé avec un objet correspondant aux valeurs attendues
//        verify(medicalrecordService, times(1)).updateMedicalrecord(argThat(record ->
//                record.getFirstName().equals("Ilyes") &&
//                        record.getLastName().equals("Soumar") &&
//                        record.getBirthdate().equals(LocalDate.of(1980, 1, 1)) &&
//                        record.getMedications().equals(List.of("newMed:100mg")) &&
//                        record.getAllergies().equals(List.of("newAllergy"))
//        ));
//    }
}
