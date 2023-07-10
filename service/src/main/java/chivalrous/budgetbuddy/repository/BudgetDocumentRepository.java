package chivalrous.budgetbuddy.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import chivalrous.budgetbuddy.model.BudgetProcess;

@Repository
public class BudgetDocumentRepository {

	private static final String BUDGET_COLLECTION = "budget";

	public void saveBudgets(List<BudgetProcess> budgets) {
		Firestore db = FirestoreClient.getFirestore();
		for (BudgetProcess budgetProcess : budgets) {
			db.collection(BUDGET_COLLECTION).document(budgetProcess.getId()).set(budgetProcess);
		}
	}
}
