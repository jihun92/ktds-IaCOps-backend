package com.ktds.IaCOps.common.parsing.json.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class JsonComponent {

    public String readJsonFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public List<String> readJsonFileToList(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
    
}
