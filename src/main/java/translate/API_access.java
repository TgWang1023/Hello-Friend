package translate;
// Cloud Translation
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

// Speech to Text
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

// Java native
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class API_access {
    // Function returns a string that contains the translated text from the original text
    // text: Original text. source_code: Source language rep code. target_code: Target language rep code
    // For detailed availibility of text translation codes, visit https://cloud.google.com/translate/docs/languages
    public static String translate(String text, String source_code, String target_code) throws Exception {
        // Instantiates a client
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        // Translates some text 
        Translation translation =
            translate.translate(
                text,
                TranslateOption.sourceLanguage(source_code),
                TranslateOption.targetLanguage(target_code));

        return translation.getTranslatedText();
    }

    // Function returns an arraylist of strings that contains the original text from the audio file.
    // file_path: File path to a PCM audio file to transcribe. speech_code: Speech language rep code.
    // For detailed availibility of speech codes, visit https://cloud.google.com/speech-to-text/docs/languages
    public static ArrayList<String> speech(String file_path, String speech_code) throws Exception {
        try (SpeechClient speech = SpeechClient.create()) {
            Path path = Paths.get(file_path);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);
        
            // Configure request with local raw PCM audio
            RecognitionConfig config =
                RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR16)
                    .setLanguageCode(speech_code)
                    .setSampleRateHertz(16000)
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();
        
            // Use blocking call to get audio transcript
            RecognizeResponse response = speech.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();
            ArrayList<String> text = new ArrayList<String>();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                text.add(alternative.getTranscript().toString());
            }

            return text;
        }
    }

    // Function returns an arraylist of strings that contains the translated text from the audio file.
    // file_path: file path to a PCM audio file to transcribe. speech_code: Speech language rep code. source_code: Source language rep code. target_code: Target language rep code.
    // For detailed availibility of text translation codes, visit https://cloud.google.com/translate/docs/languages
    // For detailed availibility of speech codes, visit https://cloud.google.com/speech-to-text/docs/languages
    // ****** IMPORTANT: SPEECH CODES ARE DIFFERENT FROM TEXT CODES ******
    public static ArrayList<String> speech_translate(String file_path, String speech_code, String source_code, String target_code) throws Exception {
        // Speech gathering
        ArrayList<String> orig_text = speech(file_path, speech_code);
  
        // Speech translation
        ArrayList<String> result_text = new ArrayList<String>();
        for(String single_orig_text : orig_text) {
            result_text.add(translate(single_orig_text, source_code, target_code));
        }

        return result_text;
    }
}
