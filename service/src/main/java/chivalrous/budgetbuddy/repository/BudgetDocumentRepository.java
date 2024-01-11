package chivalrous.budgetbuddy.repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import chivalrous.budgetbuddy.constant.BbCollection;
import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.exception.FirebaseException;
import chivalrous.budgetbuddy.model.Budget;

@Repository
public class BudgetDocumentRepository {

	public void saveBudgets(List<Budget> budgets) {
		Firestore db = FirestoreClient.getFirestore();
		for (Budget budgetProcess : budgets) {
			db.collection(BbCollection.BUDGET.getName()).document(budgetProcess.getId()).set(budgetProcess);
		}
	}

	public void deleteBudgetsWithPeriodAndBank(String period, String bank) {
		try {
			Firestore db = FirestoreClient.getFirestore();
			db.collection(BbCollection.BUDGET.getName())
					.whereEqualTo("period", period)
					.whereEqualTo("bank", bank)
					.get().get().getDocuments().forEach(x -> x.getReference().delete());
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new FirebaseException(ErrorMessage.FIREBASE_DATA_COULD_NOT_DELETE, e);
		}
	}

}
