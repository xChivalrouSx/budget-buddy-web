package chivalrous.budgetbuddy.repository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

import chivalrous.budgetbuddy.constant.BbCollection;
import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.exception.FirebaseException;
import chivalrous.budgetbuddy.model.Budget;

@Repository
public class BudgetRepository {

	public List<Budget> getBudgetsByPeriod(String period, String userId) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			ApiFuture<QuerySnapshot> future = db.collection(BbCollection.BUDGET.getName()).whereEqualTo("period", period).whereEqualTo("userId", userId).get();
			return future.get().getDocuments().stream().map(p -> p.toObject(Budget.class)).collect(Collectors.toList());
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new FirebaseException(ErrorMessage.FIREBASE_DATA_COULD_NOT_FOUND, e);
		}
	}

}
