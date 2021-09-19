import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nabla.utills.FileLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLoaderTest {
    private static FileLoader loader;

    @BeforeAll
    static void setup() {
        loader = FileLoader.getInstance();
    }

    @Test
    public void downloadRawTextCorrectLinkTest() {
        Assertions.assertDoesNotThrow(() -> loader.downloadRawText("https://www.simbirsoft.com/"));
    }
    @Test
    public void downloadRawTextIncorrectLinkTest() {
        Assertions.assertThrows(IOException.class, () ->
            loader.downloadRawText("https://www.simbirsoft.com/test123"));
    }
    @Test
    public void downloadRawTextFileIsCreated() throws IOException {
        Path actualPath = loader.downloadRawText("https://www.simbirsoft.com/");
        Assertions.assertTrue(Files.exists(actualPath));
    }
}
