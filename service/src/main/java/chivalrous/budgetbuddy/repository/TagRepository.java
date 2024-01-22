package chivalrous.budgetbuddy.repository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.cloud.FirestoreClient;

import chivalrous.budgetbuddy.constant.BbCollection;
import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.exception.FirebaseException;
import chivalrous.budgetbuddy.model.Tag;

@Repository
public class TagRepository {

	public List<Tag> findByUser(String userId) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			ApiFuture<QuerySnapshot> future = db.collection(BbCollection.TAG.getName()).whereEqualTo("userId", userId).get();
			if (future.get().getDocuments().isEmpty()) {
				return null;
			}
			return future.get().getDocuments().stream().map(p -> p.toObject(Tag.class)).collect(Collectors.toList());
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new FirebaseException(ErrorMessage.FIREBASE_DATA_TAG_COULD_NOT_GET, e);
		}
	}

	public Tag findByUserAndTag(String tag, String userId) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			ApiFuture<QuerySnapshot> future = db.collection(BbCollection.TAG.getName()).whereEqualTo("tag", tag).whereEqualTo("userId", userId).get();
			if (future.get().getDocuments().isEmpty()) {
				return null;
			}
			return future.get().getDocuments().stream().map(p -> p.toObject(Tag.class)).findFirst().orElse(null);
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new FirebaseException(ErrorMessage.FIREBASE_DATA_TAG_COULD_NOT_GET, e);
		}
	}

	public void saveOrUpdateTag(Tag tag) {
		Firestore db = FirestoreClient.getFirestore();
		db.collection(BbCollection.TAG.getName()).document(tag.getId()).set(tag, SetOptions.merge());
	}

}
