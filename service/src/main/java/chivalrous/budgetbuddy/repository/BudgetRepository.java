package chivalrous.budgetbuddy.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
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
			if (future.get().getDocuments().isEmpty()) {
				return new ArrayList<>();
			}
			return future.get().getDocuments().stream().map(p -> p.toObject(Budget.class)).collect(Collectors.toList());
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new FirebaseException(ErrorMessage.FIREBASE_DATA_BUDGET_COULD_NOT_GET, e);
		}
	}

	public List<String> getAllDistinctPeriod() {
		try {
			Firestore db = FirestoreClient.getFirestore();
			ApiFuture<QuerySnapshot> future = db.collection(BbCollection.BUDGET.getName()).select(FieldPath.of("period")).get();
			if (future.get().getDocuments().isEmpty()) {
				return new ArrayList<>();
			}
			return future.get().getDocuments().stream()
					.map(p -> p.getString("period")).distinct()
					.sorted((item1, item2) -> item2.compareTo(item1))
					.collect(Collectors.toList());
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new FirebaseException(ErrorMessage.FIREBASE_DATA_BUDGET_COULD_NOT_GET, e);
		}

	}

	public Budget findById(String id) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			ApiFuture<QuerySnapshot> future = db.collection(BbCollection.BUDGET.getName()).whereEqualTo("id", id).get();
			if (future.get().getDocuments().isEmpty()) {
				return null;
			}
			return future.get().getDocuments().stream().map(p -> p.toObject(Budget.class)).findFirst().orElse(null);
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new FirebaseException(ErrorMessage.FIREBASE_DATA_BUDGET_COULD_NOT_GET, e);
		}
	}

	public void saveOrUpdateBudget(Budget budget) {
		Firestore db = FirestoreClient.getFirestore();
		db.collection(BbCollection.BUDGET.getName()).document(budget.getId()).set(budget, SetOptions.merge());
	}

}
