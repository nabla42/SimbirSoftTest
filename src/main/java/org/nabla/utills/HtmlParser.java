package org.nabla.utills;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlParser {
    private static HtmlParser parser;
    private HtmlParser() {}

    public static HtmlParser getInstance() {
        if(parser == null) {
            parser = new HtmlParser();
        }
        return parser;
    }

    /**
     * Метод для удаления тегов имеющих содержимое, которое не является контентом страницы
     * (например <script>...</script>, <style>...</style> ...).
     *
     * @param text      исходный текст
     * @param pattern   тег
     * @return          изменяемая строка без указанного тега
     */
    public String complicatedTagRemover(String text, String pattern) {
        StringBuilder processedString = new StringBuilder();
        Matcher m = Pattern.compile("<" + pattern).matcher(text);
        int endScript = 0;
        while (m.find()) {
            int startPattern = m.start();
            int endPattern = m.end();
            while(endPattern != text.length()) {
                endPattern++;
                if (text.charAt(endPattern) == '<') {
                    if (text.startsWith("</" + pattern + ">", endPattern)) {
                        processedString.append(text, endScript, startPattern);
                        endScript = endPattern + pattern.length() + 3;
                        break;
                    }
                }
            }
        }
        processedString.append(text, endScript, text.length());
        return processedString.toString();
    }

    /**
     * Метод для замены простых html тегов (например <div></div>, <a></a>) на пробел
     * (во избежание склеивания слов).
     *
     * @param text      исходный текст
     * @return          изменяемая строка без простых тегов (удовлетворяющая паттерну ниже)
     */
    public String tagsRemover(String text) {
        Pattern pattern = Pattern.compile("(<(?:\"[^\"]*\"['\"]*|'[^']*'['\"]*|[^'\">])+>)");
        String[] strings = pattern.split(text);

        return String.join(" ", strings);
    }

    /**
     * Метод для разделения текстовой строки по знакам препинания, пробелам и другим символам,
     * не являющимимися буквами или числами.
     *
     * @param text      исходный текст
     * @return          список строк, состоящих из букв или цифр
     */
    public List<String> textSymbolsSplitter(String text) {
        Pattern pattern = Pattern.compile("(\\s-\\s)|[!-,.–/:-@\\[-`{-~«»\\s—©]+");
        String[] strings = pattern.split(text);

        return Stream.of(strings).filter(s -> !s.equals("")).collect(Collectors.toList());
    }
}
