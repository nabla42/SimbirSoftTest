package org.nabla.processes;

import org.nabla.db.DBService;
import org.nabla.utills.HtmlParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.*;

/**
 * Класс для создания статистики по количеству уникальных слов в тексте.
 */
public class StatisticMaker implements TextProcess {
    private static final Logger LOGGER =
            Logger.getLogger(StatisticMaker.class.getName());
    private final HtmlParser parser;
    private final DBService dbService;
    private static StatisticMaker statisticMaker;
    private Map<String, Integer> map;
    private Path DIRECTORY;


    private StatisticMaker(){
        try {
            Path dir = Files.createDirectories(Path.of("./log"));
            FileHandler fh = new FileHandler(dir + File.separator + "log.log");
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);

        } catch (IOException e) {
            e.printStackTrace();
        }
        dbService = new DBService();
        parser = HtmlParser.getInstance();
        map = new HashMap<>();
    }

    public static StatisticMaker getInstance() {
        if(statisticMaker == null) {
            statisticMaker = new StatisticMaker();
        }
        return statisticMaker;
    }

    @Override
    public void process(Path text) throws IOException{
        if(!map.isEmpty())
            map.clear();
        try {
            String res = load(text);
            res = parser.complicatedTagRemover(res, "script");
            res = parser.complicatedTagRemover(res, "style");
            res = parser.tagsRemover(res);
            List<String> splittedStringList = parser.textSymbolsSplitter(res);

            toMap(splittedStringList);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Can't load  source file.",ex);
            throw ex;
        }
    }
    public Map<String, Integer> getResult() {
        return new HashMap<>(map);
    }

    public void saveToFile() throws IOException{
        try{
            if(DIRECTORY == null)
                throw new IOException("Nothing to save.");
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(
                            DIRECTORY.toFile() +
                                    File.separator +
                                    "statistic.txt"));
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                bw.write(entry.getKey() + " = " + entry.getValue() + "\n");
            }
            bw.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Can't save statistic.",ex);
            throw ex;
        }
    }

    public void saveToDB() {
        dbService.createTable();
        dbService.uploadStatistic(map, DIRECTORY.getFileName().toString(), 50);
    }

    public void show() {
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }

    /**
     * Отображение списка строк в map.
     *
     * @param stringList        список строк
     */
    private void toMap(List<String> stringList) {
        for(String str: stringList) {
                map.put((str).toLowerCase(Locale.ROOT),
                        map.getOrDefault(str.toLowerCase(Locale.ROOT), 0) + 1);
        }
    }

    /**
     * Загрузка файла с жесткого диска.
     *
     * @param path      путь к файлу
     * @return          изменяемую строку с содержимым файла
     */
    private String load(Path path) throws IOException{
        StringBuilder sb = new StringBuilder();
        DIRECTORY = path.getParent();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path.toString()));
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Can't load source file.",ex);
            throw ex;
        }
        return sb.toString();
    }

}
