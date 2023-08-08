import { Card } from "primereact/card";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import TabViewBb from "../../components/TabViewBb";
import TableBb from "../../components/TableBb";
import { TableBbColumn } from "../../components/TableBb/TableBbObjects";
import { secondHeaderColorClassName } from "../../constants/BudgetBuddyConstant";
import { BudgetDetailResponse } from "../../dto/response/BudgetDetailResponse";
import { BudgetGroupStoreTypeResponse } from "../../dto/response/BudgetGroupStoreTypeResponse";
import { BudgetResponse } from "../../dto/response/BudgetResponse";
import api from "../../utils/Api";
import { getPeriodValue } from "../../utils/DateFunctions";
import { formatCurrencyAsTR } from "../../utils/PriceFunctions";

const budgetColumns: TableBbColumn[] = [
	{
		dataField: "id",
		header: "Id",
		headerClassName: secondHeaderColorClassName,
		hidden: true,
	},
	{
		dataField: "date",
		header: "Date (Tarih)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
		style: { width: 120 },
		displayFormat: (budget: BudgetResponse) =>
			budget.date.toString().split("T")[0],
	},
	{
		dataField: "storeName",
		header: "Store Name (Mağaza Adı)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
	},
	{
		dataField: "storeType",
		header: "Store Type (Mağaza Türü)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
	},
	{
		dataField: "bank",
		header: "Bank (Banka)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
	},
	{
		dataField: "totalInstallment",
		header: "Total Installment (Toplam Taksit)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
	},
	{
		dataField: "paidInstallment",
		header: "Paid Installment (Ödenen Taksit)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
	},
	{
		dataField: "remainingInstallment",
		header: "Remaining Installment (Kalan Taksit)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
	},
	{
		dataField: "priceForInstallment",
		header: "Price For Installment (Taksit Başına Fiyat)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
		displayFormat: (budget: BudgetResponse) =>
			formatCurrencyAsTR(budget.priceForInstallment),
	},
	{
		dataField: "price",
		header: "Price (Toplam Fiyat)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
		displayFormat: (budget: BudgetResponse) =>
			formatCurrencyAsTR(budget.price),
	},
	{
		dataField: "giftPoint",
		header: "Gift Point (Hediye Puanı)",
		headerClassName: secondHeaderColorClassName,
		hidden: true,
	},
	{
		dataField: "cardType",
		header: "Card Type (Kart Türü)",
		headerClassName: secondHeaderColorClassName,
		hidden: true,
	},
	{
		dataField: "tags",
		header: "Tags (Etiketler)",
		headerClassName: secondHeaderColorClassName,
		hidden: true,
	},
	{
		dataField: "period",
		header: "Period (Dönem)",
		headerClassName: secondHeaderColorClassName,
		hidden: true,
	},
	{
		dataField: "periodInt",
		header: "Period Int (Dönem Int)",
		headerClassName: secondHeaderColorClassName,
		hidden: true,
	},
];

const budgetByStoreTypeColumns: TableBbColumn[] = [
	{
		dataField: "storeType",
		header: "Store Type (Mağaza Türü)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
	},
	{
		dataField: "totalPrice",
		header: "Price (Toplam Fiyat)",
		headerClassName: secondHeaderColorClassName,
		sortable: true,
		displayFormat: (budgetByStoreType: BudgetGroupStoreTypeResponse) =>
			formatCurrencyAsTR(budgetByStoreType.totalPrice),
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
		<TabViewBb
			content={[
				{
					header: "Payments (Harcamalar)",
					content: (
						<>
							{budgetDetail.budgets.length > 0 ? (
								<Card title={period} className="text-primary">
									<TableBb
										columns={budgetColumns}
										data={budgetDetail.budgets}
										scroolableBody={{
											scroolableBody: true,
											scroolableHeight: "calc(100vh - 23rem)",
										}}
									/>
								</Card>
							) : undefined}
						</>
					),
				},
				{
					header: "Payments By Store Type (Mağaza Türüne Göre Harcamalar)",
					content: (
						<>
							{budgetDetail.budgets.length > 0 ? (
								<Card title={period} className="text-primary">
									<TableBb
										columns={budgetByStoreTypeColumns}
										data={budgetDetail.priceAsBudgetStoreType}
										scroolableBody={{
											scroolableBody: true,
											scroolableHeight: "calc(100vh - 23rem)",
										}}
									/>
								</Card>
							) : undefined}
						</>
					),
				},
			]}
		/>
	);
};

export default DetailPage;
