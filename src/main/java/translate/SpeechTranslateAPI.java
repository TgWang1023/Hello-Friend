package translate; 
// Cloud Translation
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

// Speech to Text
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

// Java native
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.TargetDataLine;

public class SpeechTranslateAPI {

  	public static void main(String args[]) {

		ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
		try (SpeechClient client = SpeechClient.create()) {
	
			responseObserver =
				new ResponseObserver<StreamingRecognizeResponse>() {
					ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();
					public void onStart(StreamController controller) {}
					public void onResponse(StreamingRecognizeResponse response) {
						responses.add(response);
					}
	
					public void onComplete() {
						for (StreamingRecognizeResponse response : responses) {
							StreamingRecognitionResult result = response.getResultsList().get(0);
							SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
							System.out.printf("Transcript : %s\n", alternative.getTranscript());
							try { translate(alternative.getTranscript()); } 
							catch(Exception e) { System.out.println("Translation error."); }
						}
					}
	
					public void onError(Throwable t) {
						System.out.println(t);
					}
			};
	
			ClientStream<StreamingRecognizeRequest> clientStream =
				client.streamingRecognizeCallable().splitCall(responseObserver);
		
			RecognitionConfig recognitionConfig =
				RecognitionConfig.newBuilder()
					.setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
					.setLanguageCode("en-US")
					.setSampleRateHertz(16000)
					.build();
			StreamingRecognitionConfig streamingRecognitionConfig =
				StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).build();
		
			StreamingRecognizeRequest request =
				StreamingRecognizeRequest.newBuilder()
					.setStreamingConfig(streamingRecognitionConfig)
					.build(); // The first request in a streaming call has to be a config
		
			clientStream.send(request);
			// SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true,
			// bigEndian: false
			AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
			DataLine.Info targetInfo =
				new Info(
					TargetDataLine.class,
					audioFormat); // Set the system information to read from the microphone audio stream
		
			if (!AudioSystem.isLineSupported(targetInfo)) {
				System.out.println("Microphone not supported");
				System.exit(0);
			}
			// Target data line captures the audio stream the microphone produces.
			TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			System.out.println("Start speaking");
			long startTime = System.currentTimeMillis();
			// Audio Input Stream
			AudioInputStream audio = new AudioInputStream(targetDataLine);
			while (true) {
				long estimatedTime = System.currentTimeMillis() - startTime;
				byte[] data = new byte[6400];
				audio.read(data);
				if (estimatedTime > 60000) { // 60 seconds
					System.out.println("Stop speaking.");
					targetDataLine.stop();
					targetDataLine.close();
					break;
				}
				request =
					StreamingRecognizeRequest.newBuilder()
						.setAudioContent(ByteString.copyFrom(data))
						.build();
				clientStream.send(request);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		responseObserver.onComplete();
	}

	public static void translate(String text) throws Exception {
		// Instantiates a client
		Translate translate = TranslateOptions.getDefaultInstance().getService();

		// Translates some text
		Translation translation =
			translate.translate(
				text,
				TranslateOption.sourceLanguage("en"),
				TranslateOption.targetLanguage("zh-CN"));

		System.out.printf("Translation: %s%n", translation.getTranslatedText());
	}
}