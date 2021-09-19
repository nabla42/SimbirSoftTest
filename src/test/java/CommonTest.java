import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nabla.processes.StatisticMaker;
import org.nabla.utills.FileLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class CommonTest {
    private static FileLoader loader;
    private static StatisticMaker process;
    @BeforeAll
    static void setup() {
        loader = FileLoader.getInstance();
        process = StatisticMaker.getInstance();
    }
    @Test
    public void allResultsNotEqualsTest() throws IOException {
        Path path = loader.downloadRawText("https://www.simbirsoft.com");
        process.process(path);
        process.saveToFile();
        Map<String, Integer> mapFirst = process.getResult();

        path = loader.downloadRawText("https://finance.simbirsoft.com/");
        process.process(path);
        process.saveToFile();
        Map<String, Integer> mapSecond = process.getResult();

        Assertions.assertNotEquals(mapFirst, mapSecond);
    }
}
