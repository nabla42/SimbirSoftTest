import org.junit.jupiter.api.*;
import org.nabla.processes.StatisticMaker;
import org.nabla.processes.TextProcess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StatisticMakerTest {
    private static TextProcess process;
    private final Path pathToExpectedFile =
            Paths.get( new File("./src/test/content/content.txt").getPath());

    @BeforeAll
    public static void setUp() {
        process = StatisticMaker.getInstance();
    }
    @AfterEach
    public void setOut() throws IOException {
        Path pathToStatistic = Path.of(
                pathToExpectedFile.getParent() +
                        File.separator + "statistic.txt");
        if(Files.exists(pathToStatistic)) {
            Files.delete(pathToStatistic);
        }
    }

    @Test
    public void processValidFilePath() {
        Assertions.assertDoesNotThrow(() ->
                process.process(pathToExpectedFile));
    }

    @Test
    public void processInvalidFilePath() {
        Assertions.assertThrows(IOException.class, () ->
                process.process(Path.of("")));
    }

    @Test
    public void saveToFileProcessedTest() throws IOException{
        process.process(pathToExpectedFile);
        process.saveToFile();
        Assertions.assertTrue(
                Files.exists(Path.of(pathToExpectedFile.getParent() + File.separator + "statistic.txt")));
    }

    @Test
    public void saveToFileNotProcessedTest() {
        Assertions.assertThrows(IOException.class, () -> {
                process.process(Path.of(""));
                process.saveToFile();
        });
    }

}
