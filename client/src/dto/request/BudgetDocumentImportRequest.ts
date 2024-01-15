export interface BudgetDocumentImportRequest {
	file: File | null;
	bank: string;
	year: number;
	month: number;
}

export interface BudgetDocumentSingleImportRequest {
	bank: string;
	year: number;
	month: number;
	singleImportDTO: SingleImportDTO;
}

export interface SingleImportDTO {
	date: Date;
	price: number;
	description: string;
}
