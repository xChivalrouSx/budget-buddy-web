export interface BudgetSummaryResponse {
	period: string;
	totalIncome: number;
	totalPrice: number;
	totalPriceWithoutInstallment: number;
	totalPriceWithInstallment: number;
	totalPriceEndingInstallment: number;
	totalPriceStartingInstallment: number;
}
