package chivalrous.budgetbuddy.repository;

import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.exception.FirebaseException;
import chivalrous.budgetbuddy.model.User;

@Repository
public class UserRepository {

	private static final String USER_COLLECTION = "user";

	public void createUser(User user) {
		FirestoreClient.getFirestore().collection(USER_COLLECTION).document(user.getId()).set(user);
	}

	public User getUser(String username) {
		try {
			String userId = DigestUtils.md5Hex(username).toUpperCase();
			DocumentSnapshot userDocument = FirestoreClient.getFirestore().collection(USER_COLLECTION).document(userId).get().get();

			if (userDocument.exists()) {
				return userDocument.toObject(User.class);
			}
			return null;
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new FirebaseException(ErrorMessage.FIREBASE_DATA_COULD_NOT_FOUND, e);
		}
	}

}
