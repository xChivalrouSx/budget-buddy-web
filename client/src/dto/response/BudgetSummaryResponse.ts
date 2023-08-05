export interface BudgetSummaryResponse {
	period: string;
	totalPrice: number;
	totalPriceWithoutInstallment: number;
	totalPriceWithInstallment: number;
	totalPriceEndingInstallment: number;
	totalPriceStartingInstallment: number;
}
