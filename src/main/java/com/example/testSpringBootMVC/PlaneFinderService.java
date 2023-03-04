package com.example.testSpringBootMVC;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PlaneFinderService {
    private final PlaneRepository repo;
    private URL acURL;
    private final ObjectMapper om;

    @SneakyThrows
    public PlaneFinderService(PlaneRepository repo) {
        this.repo = repo;

        acURL = new URL("http://192.168.1.193/ajax/aircraft");
        om = new ObjectMapper();
    }

    private void printE(String msg){
        System.out.println("easontesting PlaneFinderService: "+msg);
    }

    public Iterable<Aircraft> getAircraft() throws IOException {
        List<Aircraft> positions = new ArrayList<>();

        JsonNode aircraftNodes = null;
        try {
            printE("getAircraft1");
            aircraftNodes = om.readTree(acURL)
                    .get("aircraft");
            printE("getAircraft2");
            aircraftNodes.iterator().forEachRemaining(node -> {
                try {
                    printE("getAircraft3");
                    positions.add(om.treeToValue(node, Aircraft.class));
                    printE("getAircraft4");
                } catch (JsonProcessingException e) {
                    printE("getAircraft5");
                    e.printStackTrace();
                    printE("getAircraft6");
                }
            });
        } catch (IOException e) {
            //e.printStackTrace();
            printE("getAircraft7");
            System.out.println("\n>>> IO Exception: " + e.getLocalizedMessage() +
                    ", generating and providing sample data.\n");
            printE("getAircraft8");
            return saveSamplePositions();
        }

        if (positions.size() > 0) {
            printE("getAircraft10");
            positions.forEach(System.out::println);
            printE("getAircraft11");
            repo.deleteAll();
            printE("getAircraft12");
            return repo.saveAll(positions);
        } else {
            printE("getAircraft14");
            System.out.println("\n>>> No positions to report, generating and providing sample data.\n");
            printE("getAircraft15");
            return saveSamplePositions();
        }
    }

    private Iterable<Aircraft> saveSamplePositions() {
        repo.deleteAll();

        // Spring Airlines flight 001 en route, flying STL to SFO, at 30000' currently over Kansas City
        Aircraft ac1 = new Aircraft("SAL001", "N12345", "SAL001", "LJ",
                30000, 280, 440,
                39.2979849, -94.71921);

        // Spring Airlines flight 002 en route, flying SFO to STL, at 40000' currently over Denver
        Aircraft ac2 = new Aircraft("SAL002", "N54321", "SAL002", "LJ",
                40000, 65, 440,
                39.8560963, -104.6759263);

        // Spring Airlines flight 002 en route, flying SFO to STL, at 40000' currently just past DEN
        Aircraft ac3 = new Aircraft("SAL002", "N54321", "SAL002", "LJ",
                40000, 65, 440,
                39.8412964, -105.0048267);

        printE("saveSamplePositions");
        return repo.saveAll(Arrays.asList(ac1, ac2, ac3));
    }
}

