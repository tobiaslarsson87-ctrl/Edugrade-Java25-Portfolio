package se.edugrade.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import se.edugrade.dto.UsersDTO;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExportJSON {
    /**
     * Exportera List<UsersDTO> till JSON.
     * <p><strong>ANVÄNDNINGSEXEMPEL</p>
     * <pre>
     *     List<UsersDTO> userProfiles = UsersService.showAll();
     *     Path filePath = Path.of(exported_data, users.json)
     *     ExportJSON.exportUserData(userProfiles, filePath)
     * </pre>
     *
     * @param users En lista av: UsersDTO objekt
     * @param path  Sökväg samt filnamn
     */
    public static void exportUserData(List<UsersDTO> users, Path path) {
        final String Y = Colors.rgb(200, 200, 25);
        final String R = Colors.rgb(200, 0, 25);
        final String G = Colors.rgb(0, 200, 25);
        final String D = Colors.rgb(150, 150, 150);
        final String X = Colors.reset();


        try {
            System.out.println(Y + "<Exporting user profiles to JSON>" + X);
            final ObjectMapper om = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            Files.createDirectories(path.getParent());
            String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(users);
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
                writer.write(json);
            }

            System.out.println(G + "✅[EXPORT COMPLETED]✅" + X);
            System.out.println(D + "📂Path:" + X + " " + path);

        } catch (IOException e) {
            throw new RuntimeException(R + "[EXPORT FAILED]" + X, e);
        }
    }
}
