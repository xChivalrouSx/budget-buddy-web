export interface BudgetDocumentImportRequest {
	file: File | null;
	bank: string;
	year: number;
	month: number;
}

export interface BudgetSingleImportRequest {
	bank: string;
	year: number;
	month: number;
	income: boolean;
	singleImportDTO: SingleImportDTO;
}

export interface SingleImportDTO {
	date: Date;
	price: number;
	description: string;
	tags: string[];
}

export interface TagAutoRequest {
	tag: string;
	storeType: string;
	storeNameKeywords: string[];
}
