package com.example.testSpringBootMVC;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class DataLoader {
    private final AircraftRepository repository;

    @PostConstruct
    private void loadData(){
        System.out.println("easontesting-DataLoader-loadData1");
        saveSamplePositions();
        System.out.println("easontesting-DataLoader-loadData2");
    }

    private Iterable<Aircraft> saveSamplePositions() {
        System.out.println("easontesting-DataLoader-saveSamplePositions");
        repository.deleteAll();

        // Spring Airlines flight 001 en route, flying STL to SFO, at 30000' currently over Kansas City
        Aircraft ac1 = new Aircraft("SAL001", "N12345", "SAL001", "LJ",
                30000, 280, 440,
                39.2979849, -94.71921);

        // Spring Airlines flight 002 en route, flying SFO to STL, at 40000' currently over Denver
        Aircraft ac2 = new Aircraft("SAL002", "N54321", "SAL002", "LJ",
                40000, 65, 440,
                39.8560963, -104.6759263);

        // Spring Airlines flight 002 en route, flying SFO to STL, at 40000' currently just past DEN
        Aircraft ac3 = new Aircraft("SAL003", "N54323", "SAL002", "LJ",
                40000, 65, 440,
                39.8412964, -105.0048267);

        return repository.saveAll(Arrays.asList(ac1, ac2, ac3));
    }
}
