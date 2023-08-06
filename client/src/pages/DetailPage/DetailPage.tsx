import { Card } from "primereact/card";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import TableBb from "../../components/TableBb";
import { TableBbColumn } from "../../components/TableBb/TableBbObjects";
import { BudgetDetailResponse } from "../../dto/response/BudgetDetailResponse";
import { BudgetResponse } from "../../dto/response/BudgetResponse";
import api from "../../utils/Api";
import { getPeriodValue } from "../../utils/DateFunctions";
import { formatCurrencyAsTR } from "../../utils/PriceFunctions";

const columns: TableBbColumn[] = [
	{
		dataField: "id",
		header: "Id",
		hidden: true,
	},
	{
		dataField: "date",
		header: "Date (Tarih)",
		sortable: true,
		style: { width: 120 },
		displayFormat: (budget: BudgetResponse) =>
			budget.date.toString().split("T")[0],
	},
	{
		dataField: "storeName",
		header: "Store Name (Mağaza Adı)",
		sortable: true,
	},
	{
		dataField: "storeType",
		header: "Store Type (Mağaza Türü)",
		sortable: true,
	},
	{
		dataField: "bank",
		header: "Bank (Banka)",
		sortable: true,
	},
	{
		dataField: "totalInstallment",
		header: "Total Installment (Toplam Taksit)",
		sortable: true,
	},
	{
		dataField: "paidInstallment",
		header: "Paid Installment (Ödenen Taksit)",
		sortable: true,
	},
	{
		dataField: "remainingInstallment",
		header: "Remaining Installment (Kalan Taksit)",
		sortable: true,
	},
	{
		dataField: "priceForInstallment",
		header: "Price For Installment (Taksit Başına Fiyat)",
		sortable: true,
		displayFormat: (budget: BudgetResponse) =>
			formatCurrencyAsTR(budget.priceForInstallment),
	},
	{
		dataField: "price",
		header: "Price (Toplam Fiyat)",
		sortable: true,
		displayFormat: (budget: BudgetResponse) =>
			formatCurrencyAsTR(budget.price),
	},
	{
		dataField: "giftPoint",
		header: "Gift Point (Hediye Puanı)",
		hidden: true,
	},
	{
		dataField: "cardType",
		header: "Card Type (Kart Türü)",
		hidden: true,
	},
	{
		dataField: "tags",
		header: "Tags (Etiketler)",
		hidden: true,
	},
	{
		dataField: "period",
		header: "Period (Dönem)",
		hidden: true,
	},
	{
		dataField: "periodInt",
		header: "Period Int (Dönem Int)",
		hidden: true,
	},
];

const DetailPage = () => {
	const { period } = useParams();

	const [budgetDetail, setBudgetDetail] = useState<BudgetDetailResponse>({
		budgets: [],
		priceAsBudgetStoreType: [],
	});

	const getBudgetsDetail = () => {
		const periodParams = getPeriodValue(period ?? "");
		api.get(
			"/budget-detail/" + periodParams.year + "/" + periodParams.month
		).then((response: BudgetDetailResponse) => {
			setBudgetDetail(response);
		});
	};

	useEffect(() => {
		getBudgetsDetail();
	}, [period]);

	return (
		<>
			{budgetDetail.budgets.length > 0 ? (
				<Card title={period} className="text-primary">
					<TableBb columns={columns} data={budgetDetail.budgets} />
				</Card>
			) : undefined}
		</>
	);
};

export default DetailPage;
