package chivalrous.budgetbuddy.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import chivalrous.budgetbuddy.constant.BbCollection;
import chivalrous.budgetbuddy.model.Budget;

@Repository
public class BudgetDocumentRepository {

	public void saveBudgets(List<Budget> budgets) {
		Firestore db = FirestoreClient.getFirestore();
		for (Budget budgetProcess : budgets) {
			db.collection(BbCollection.BUDGET.getName()).document(budgetProcess.getId()).set(budgetProcess);
		}
	}

}
