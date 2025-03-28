package com.project.safetynet.controller;

import com.project.safetynet.model.Firestation;
import com.project.safetynet.model.Medicalrecord;
import com.project.safetynet.model.Person;
import com.project.safetynet.service.DataLoaderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {
    private final DataLoaderService dataLoaderService;

    public DataController(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

}
