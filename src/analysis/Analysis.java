package analysis;

import algorithms.Algorithm;
import algorithms.AlgorithmHolder;
import algorithms.Padding;
import utils.Conversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public final class Analysis {
    private final AlgorithmHolder holder;

    private final OptionsIterator keyOptions;
    private final String keyTemplate;
    public final byte[] initialVector;
    public final Padding padding;
    private final String k;

    private boolean flagKey = true;

    public Analysis(AlgorithmHolder holder, String partialKey, byte[] initialVector, Padding padding) {
        this.holder = holder;
        this.k = partialKey;
        this.keyTemplate = partialKey;
        long starCounter = partialKey.chars().filter(ch -> ch == '*').count();
        this.keyOptions = new OptionsIterator((int) starCounter);
        this.initialVector = initialVector;
        this.padding = padding;
    }

    public Analysis(AlgorithmHolder holder, String partialKey, byte[] initialVector) {
        this(holder, partialKey, initialVector, null);
    }

    public Analysis(AlgorithmHolder holder, String partialKey, Padding padding) {
        this(holder, partialKey, null, padding);
    }

    public Analysis(AlgorithmHolder holder, String partialKey) {
        this(holder, partialKey, null, null);
    }


    public OptionsIterator getOptions() {
        return keyOptions;
    }


    private List<String> generateHexValues() {
        List<String> hexValues = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            hexValues.add(String.format("%02X", i));
            System.out.println("hex value: " + String.format("%02X", i));
        }
        return hexValues;
    }

//    private String substantiate(String template, List<String> hexValues) {
//        int starIndex = template.indexOf("*");
//        int hexValueIndex = 0;
//
//        while (starIndex != -1 && hexValueIndex < hexValues.size()) {
//            template = template.substring(0, starIndex) + hexValues.get(hexValueIndex) + template.substring(starIndex + 1);
//            starIndex = template.indexOf("*", starIndex + 1);
//            hexValueIndex++;
//            System.out.println("\ntemplate_temp: "+ template);
//        }
//
//        return template;
//    }

    private String substantiate(String template, byte[] stars) {
        System.out.println("template: "+ template);
        for (byte star : stars) {
            template = template.replaceFirst("\\*", String.format("%02X", star));
            System.out.println("\ntemplate_temp: "+ template);
        }
        return template;
    }


//    TODO: ниже три функции это разработка

//    private List<String> generateSubstitutions(String template) {
//        List<String> substitutions = new ArrayList<>();
//        generateSubstitutionsRecursive(template, 0, substitutions, "");
//        return substitutions;
//    }
//
//    private void generateSubstitutionsRecursive(String template, int index, List<String> substitutions, String current) {
//        if (index == template.length()) {
//            substitutions.add(current);
//            return;
//        }
//
//        char currentChar = template.charAt(index);
//        if (currentChar == '*') {
//            for (char c = '0'; c <= 'F'; c++) {
//                if (Character.isDigit(c) || (c >= 'A' && c <= 'F')) {
//                    generateSubstitutionsRecursive(template, index + 1, substitutions, current + c);
//                }
//            }
//        } else {
//            generateSubstitutionsRecursive(template, index + 1, substitutions, current + currentChar);
//        }
//    }

//    public byte[] analyze(byte[] data, Consumer<Double> callback) {
//        if (!keyOptions.hasNext()) {
//            return Conversions.hex(keyTemplate);
//        }
//
//        while (keyOptions.hasNext() && flagKey) {
//            String currentTemplate = keyTemplate;
//
//            // Получаем список всех возможных замен
//            List<String> substitutions = generateSubstitutions(currentTemplate);
//
//            for (String substitution : substitutions) {
//                byte[] nextKey = Conversions.hex(substitution);
//
//                // Выводим сгенерированный ключ в консоль
//                System.out.println("Generated Key: " + substitution);
//
//                Algorithm algorithm = holder.instantiate(nextKey, initialVector, padding);
//                if (callback != null) {
//                    callback.accept(keyOptions.getSeen().divide(keyOptions.getLength()).doubleValue());
//                }
//                if (LangDetector.isText(algorithm.decryptString(data).getBytes())) {
//                    return nextKey;
//                }
//            }
//        }
//        return null;
//    }


    private List<String> generatePermutations(String template, List<String> hexValues) {
        List<String> permutations = new ArrayList<>();
        generatePermutationsRecursive(template, 0, permutations, "", hexValues);
        return permutations;
    }

    private void generatePermutationsRecursive(String template, int index, List<String> permutations, String current, List<String> hexValues) {
        if (index == template.length()) {
            permutations.add(current);
            return;
        }

        char currentChar = template.charAt(index);
        if (currentChar == '*') {
            for (String hexValue : hexValues) {
                generatePermutationsRecursive(template, index + 1, permutations, current + hexValue, hexValues);
            }
        } else {
            generatePermutationsRecursive(template, index + 1, permutations, current + currentChar, hexValues);
        }
    }

//    public byte[] analyze(byte[] data, Consumer<Double> callback) {
//        List<String> hexValues = generateHexValues();
//
//        if (!keyOptions.hasNext()) {
//            return Conversions.hex(keyTemplate);
//        } else {
//            while (keyOptions.hasNext()) {
//                List<String> permutations = generatePermutations(keyTemplate, hexValues);
//                System.out.println(permutations);
//                for (String substitution : permutations) {
//                    byte[] nextKey = Conversions.hex(substitution);
//                    Algorithm algorithm = holder.instantiate(nextKey, initialVector, padding);
////                    if (callback != null) {
////                        callback.accept(keyOptions.getSeen().divide(keyOptions.getLength()).doubleValue());
////                    }
//                    if (LangDetector.isText(algorithm.decryptString(data).getBytes())) {
//                        return nextKey;
//                    }
//                }
//            }
//            return null;
//        }
//    }

    public byte[] analyze(byte[] data, Consumer<Double> callback) {
        if (!keyOptions.hasNext()) {
            return Conversions.hex(keyTemplate);
        }
        else while (keyOptions.hasNext()) {
            byte[] nextKey = Conversions.hex(substantiate(keyTemplate, keyOptions.next()));
            Algorithm algorithm = holder.instantiate(nextKey, initialVector, padding);
            if (callback != null) {
                callback.accept(keyOptions.getSeen().divide(keyOptions.getLength()).doubleValue());
            }
            if (LangDetector.isText(algorithm.decrypt(data))) {
                return nextKey;
            }
        }
        return null;
    }

    public byte[] analyze(byte[] data) {
        return analyze(data, null);
    }
}
