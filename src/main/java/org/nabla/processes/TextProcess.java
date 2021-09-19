package org.nabla.processes;

import java.io.IOException;
import java.nio.file.Path;

public interface TextProcess {

    /**
     * Метод для обработки текста.
     *
     * @param str       исходный текст
     */
    void process(Path str) throws IOException;

    /**
     * Сохраняет объект, созданный в процессе работы класса в файл.
     *
     */
    void saveToFile() throws IOException;

    /**
     * Сохраняет обработанный текст в базу данных.
     *
     */
    void saveToDB();

    /**
     * Отображает результат обработки объекта.
     *
     */
    void show() throws IOException;
}
