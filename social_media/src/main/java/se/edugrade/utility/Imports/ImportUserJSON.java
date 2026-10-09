package se.edugrade.utility.Imports;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import se.edugrade.ImportDTO.ImportUserDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ImportUserJSON{
    public static List<ImportUserDTO> importUserJSON(Path filename) {
        System.out.println(" 📩 Importing Users from JSON");
        System.out.println("Reading from: " + filename); //Vilken sökväg importen tar

        try {
            final ObjectMapper om = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String json = Files.readString(filename);

            List<ImportUserDTO> post = om.readValue(json, new TypeReference<List<ImportUserDTO>>() {
            });

            String printout = om.writerWithDefaultPrettyPrinter().writeValueAsString(post);
            System.out.println(printout);

            return post;
        } catch (IOException e) {
            throw new RuntimeException("Import failed", e);
        }
    }
}


