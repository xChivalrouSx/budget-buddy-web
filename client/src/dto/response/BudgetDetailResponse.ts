import { BudgetGroupStoreTypeResponse } from "./BudgetGroupStoreTypeResponse";
import { BudgetResponse } from "./BudgetResponse";

export interface BudgetDetailResponse {
	budgets: BudgetResponse[];
	priceAsBudgetStoreType: BudgetGroupStoreTypeResponse[];
}
