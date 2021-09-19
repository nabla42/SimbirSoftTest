import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nabla.utills.HtmlParser;

import java.util.List;

public class HtmlParserTest {
    private static HtmlParser parser;
    @BeforeAll
    public static void setUp() {
        parser = HtmlParser.getInstance();
    }
    @Test
    public void complicatedTagRemoverTest() {
        String complicateTagText = "<script>Some text.</script>Other text.";
        String actual = parser.complicatedTagRemover(complicateTagText, "script");
        Assertions.assertEquals("Other text.", actual);
    }
    @Test
    public void complicatedTagRemoverWithParametersTest() {
        String complicateTagText = "<script params1=\"a\" params2=\"b\">Some text.</script>Other text.";
        String actual = parser.complicatedTagRemover(complicateTagText, "script");
        Assertions.assertEquals("Other text.", actual);
    }
    @Test
    public void complicatedTagRemoverWithoutTagsTest() {
        String complicateTagText = "Some text. Other text.";
        String actual = parser.complicatedTagRemover(complicateTagText, "script");
        Assertions.assertEquals("Some text. Other text.", actual);
    }
    @Test
    public void complicatedTagRemoverTextBetweenTagsTest() {
        String complicateTagText = "<script params1=\"a\" params2=\"b\">Some text.</script>" +
                "Other text." +
                "<script params1=\"a\" params2=\"b\">Some text.</script>";
        String actual = parser.complicatedTagRemover(complicateTagText, "script");
        Assertions.assertEquals("Other text.", actual);
    }
    @Test
    public void complicatedTagRemoverTextWithSymbolsTest() {
        String complicateTagText = "<script params1=\"a\" params2=\"b\">Some text</script>" +
                "Other text <- More text." +
                "<script params1=\"a\" params2=\"b\">Some text.</script>";
        String actual = parser.complicatedTagRemover(complicateTagText, "script");
        Assertions.assertEquals("Other text <- More text.", actual);
    }

    @Test
    public void tagsRemoverTextTest() {
        String simpleTagText = "<div><br>Some text.</div>";
        String actual = parser.tagsRemover(simpleTagText);
        Assertions.assertEquals("  Some text.", actual);
    }
    @Test
    public void tagsRemoverTextWithSingleTagsTest() {
        String simpleTagText = "<br>Some text.<br>Other text.";
        String actual = parser.tagsRemover(simpleTagText);
        Assertions.assertEquals(" Some text. Other text.", actual);
    }
    @Test
    public void tagsRemoverTextWithSymbolsTest() {
        String simpleTagText = "Text with some <c@r@ct#r$<!";
        String actual = parser.tagsRemover(simpleTagText);
        Assertions.assertEquals("Text with some <c@r@ct#r$<!", actual);
    }

    @Test
    public void textSymbolsSplitterSimpleTextTest() {
        String simpleTagText = "Some text";
        List<String> actual = parser.textSymbolsSplitter(simpleTagText);
        List<String> expected = List.of("Some", "text");
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void textSymbolsSplitterTest() {
        String simpleTagText = "RegExr - t!e=s,t.t/e:s@t\\t[e`s{t~t«e»s—t©t-e+s#t";
        List<String> actual = parser.textSymbolsSplitter(simpleTagText);
        List<String> expected = List.of("RegExr", "t", "e", "s", "t",
                                        "t", "e", "s", "t",
                                        "t", "e", "s", "t",
                                        "t", "e", "s", "t",
                                        "t-e", "s", "t");
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void allMethodCorrectTest() {
        String allTagText = "<script params1=\"a\" params2=\"b\"><div><br>Some text.</div></script> <br>Other text.";
        String actualStr = parser.complicatedTagRemover(allTagText, "script");
        actualStr = parser.tagsRemover(actualStr);
        List<String> actual = parser.textSymbolsSplitter(actualStr);
        List<String> expected = List.of("Other", "text");
        Assertions.assertEquals(expected, actual);
    }
}
