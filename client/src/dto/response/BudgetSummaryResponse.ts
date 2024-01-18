export interface BudgetSummaryResponse {
	period: string;
	summaryDetailList: BudgetSummaryDetailDTO[];
}

export interface BudgetSummaryDetailDTO {
	title: string;
	value: number;
	showDefault: boolean;
	order: number;
}
