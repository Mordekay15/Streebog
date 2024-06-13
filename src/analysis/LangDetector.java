package analysis;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import utils.Conversions;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


public final class LangDetector {
    private static final Set<Language> SUPPORTED_LANGUAGES = Set.of(Language.RUSSIAN, Language.ENGLISH);
    private static final LanguageDetector detector = LanguageDetectorBuilder.fromAllLanguages().build();
    private static final Map<String, Double> russian = readCharMapFromResources(Language.RUSSIAN.name().toLowerCase());
    private static final Map<String, Double> english = readCharMapFromResources(Language.ENGLISH.name().toLowerCase());

    private static Map<String, Double> readCharMapFromResources(String language) {
        InputStream stream = LangDetector.class.getResourceAsStream("/languages/" + language + ".json");
        Type map = new TypeToken<HashMap<String, Double>>() {
        }.getType();
        if (stream == null) {
            return Map.of();
        }
        else {
            return new Gson().fromJson(new InputStreamReader(stream), map);
        }
    }


    private static boolean isNonPrintable(String text) {
        List<Character> unprintable = new ArrayList<>();
        for (char ch : text.toCharArray()) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
            if (!Character.UnicodeBlock.CYRILLIC.equals(block) && !Character.UnicodeBlock.BASIC_LATIN.equals(block)) {
                unprintable.add(ch);
            }
        }
        return unprintable.size() > text.length() / 5;
    }

    private static boolean proveLanguageAffinity(Map<String, Double> language, String text) {
        String upperText = text.toUpperCase();
        List<String> chars = language.keySet().stream().sorted().collect(Collectors.toList());
        double[] expected = chars.stream().mapToDouble((String s) -> language.get(s) * upperText.length()).toArray();
        long[] actual = chars.stream().mapToLong((String s) -> upperText.split(s).length - 1).toArray();
        return new ChiSquareTest().chiSquareTest(expected, actual, 0.1);
    }


    public static String detectLanguage(String text) {
        String encoded = new String(text.getBytes(Conversions.encoding));
        if (isNonPrintable(encoded)) {
            return null;
        }
        else if (proveLanguageAffinity(russian, encoded)) {
            return Language.RUSSIAN.name();
        }
        else if (proveLanguageAffinity(english, encoded)) {
            return Language.ENGLISH.name();
        }
        else {
            Language language = detector.detectLanguageOf(encoded);
            return language == Language.UNKNOWN || !SUPPORTED_LANGUAGES.contains(language) ? null : language.name();
        }
    }

    public static boolean isText(byte[] data) {
        return detectLanguage(Conversions.raw(data)) != null;
    }
}
