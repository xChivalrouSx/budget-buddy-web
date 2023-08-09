export interface BudgetDocumentImportRequest {
	file: File | null;
	bank: string;
	year: number;
	month: number;
}
