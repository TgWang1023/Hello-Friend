package edu.ucsb.cs48s19.translate;

// Imports the Google Cloud client library

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class Translator {

    public static String zh_CN_to_en(String zh_CH_text) {
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        // Translates some text into Russian
        Translation translation =
                translate.translate(
                        zh_CH_text,
                        TranslateOption.sourceLanguage("en"),
                        TranslateOption.targetLanguage("zh-CN"));

        System.out.printf("Original text: %s%n", zh_CH_text);
        System.out.printf("Translated text: %s%n", translation.getTranslatedText());

        return translation.getTranslatedText();
    }

    public static void main(String... args) throws Exception {
        // Instantiates a client
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        // The text to translate
        String text = "Hello, world!";

        // Translates some text into Russian
        Translation translation =
                translate.translate(
                        text,
                        TranslateOption.sourceLanguage("en"),
                        TranslateOption.targetLanguage("zh-CN"));

        System.out.printf("Text: %s%n", text);
        System.out.printf("Translation: %s%n", translation.getTranslatedText());
    }
}