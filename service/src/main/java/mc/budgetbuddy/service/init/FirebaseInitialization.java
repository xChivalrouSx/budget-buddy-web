package mc.budgetbuddy.service.init;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.RequiredArgsConstructor;
import mc.budgetbuddy.config.Settings;
import mc.budgetbuddy.constant.ErrorMessage;
import mc.budgetbuddy.exception.FirebaseException;

@Service
@RequiredArgsConstructor
public class FirebaseInitialization {

	private final Settings settings;

	@PostConstruct
	public void initialization() {
		try (FileInputStream serviceAccount = new FileInputStream(settings.getFirebaseAccountKeyPath())) {
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();
			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			throw new FirebaseException(ErrorMessage.FIREBASE_CAN_NOT_INITIALIZE);
		}
	}
}
