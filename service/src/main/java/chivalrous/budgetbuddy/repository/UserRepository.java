package chivalrous.budgetbuddy.repository;

import org.springframework.stereotype.Repository;

import com.google.firebase.cloud.FirestoreClient;

import chivalrous.budgetbuddy.model.User;

@Repository
public class UserRepository {

	private static final String USER_COLLECTION = "user";

	public void createUser(User user) {
		FirestoreClient.getFirestore().collection(USER_COLLECTION).document(user.getId()).set(user);
	}

}
