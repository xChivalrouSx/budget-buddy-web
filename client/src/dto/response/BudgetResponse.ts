export interface BudgetResponse {
	id: string;
	date: Date;
	storeName: string;
	storeType: string;
	price: number;
	giftPoint: number;
	cardType: string;
	totalInstallment: number;
	paidInstallment: number;
	remainingInstallment: number;
	priceForInstallment: number;
	tags: string[];
	period: string;
	periodInt: number;
	bank: string;
}
