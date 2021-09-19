package org.nabla.utills;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.*;

/**
 * Класс для взаимодействия с веб-страницами и формирования файла с контентом.
 */
public class FileLoader {
    private static final Logger LOGGER = Logger.getLogger(FileLoader.class.getName());
    private static FileLoader connector;

    private FileLoader() {
        try {
            Path dir = Files.createDirectories(Path.of("./log"));
            FileHandler fh = new FileHandler(dir + File.separator + "log.log");
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileLoader getInstance() {
        if(connector == null) {
            connector = new FileLoader();
        }
        return connector;
    }

    /**
     * Метод для загрузки содержимого страницы в файл.
     *
     * @param urlString     адрес страницы
     * @return              путь до файла, в который сохранился контент страницы
     */
    public Path downloadRawText(String urlString) throws IOException {
        InputStream stream;
        try {
            stream = new URL(urlString).openStream();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "URL is invalid.", ex);
            throw ex;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String fileName = stream.hashCode() + ".txt";
            Path fileDirectory = makeDirectory(String.valueOf(stream.hashCode()));
            File file = new File(fileDirectory + File.separator + fileName);

            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(file));

            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line + "\n");
            }
            bw.close();
            stream.close();

            return file.toPath();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Can't read line.", ex);
            throw ex;
        }
    }

    /**
     * Метод для формирования файла на диске.
     *
     * @param name      имя файла
     * @return          путь к предполагаемому файлу
     */
    private Path makeDirectory(String name) throws IOException {
        try {
            return Files.createDirectories(Path.of("./sources/" + name));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Can't create file directory.", ex);
            throw ex;
        }

    }

}
