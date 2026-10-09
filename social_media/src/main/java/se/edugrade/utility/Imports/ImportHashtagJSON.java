package se.edugrade.utility.Imports;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import se.edugrade.ImportDTO.ImportHashtagDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ImportHashtagJSON {
    public static List<ImportHashtagDTO> importHashtagJSON(Path filename) {
        System.out.println(" 📩 Importing Hashtag from JSON");
        System.out.println("Reading from: " + filename); //Vilken sökväg importen tar

        try {
            // Gör en Objectmapper (Jackson biblotek för json)
            final ObjectMapper om = new ObjectMapper()
                    .registerModule(new JavaTimeModule()) //Hanterar Localdate
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String json = Files.readString(filename); // Läser Jsonfilen

            //Konventerar json till en lista av ImportHashtagDto
            List<ImportHashtagDTO> post = om.readValue(json, new TypeReference<List<ImportHashtagDTO>>() {
            });

            // Skriver ut json
            String printout = om.writerWithDefaultPrettyPrinter().writeValueAsString(post);
            System.out.println(printout);

            // Retunderar listan
            return post;
        } catch (IOException e) {
            throw new RuntimeException("Import failed", e);
        }
    }
}
