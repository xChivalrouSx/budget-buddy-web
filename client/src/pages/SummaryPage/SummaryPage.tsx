import { useEffect, useState } from "react";
import TableBb from "../../components/TableBb";
import { TableBbColumn } from "../../components/TableBb/TableBbObjects";
import { BudgetSummaryResponse } from "../../dto/response/BudgetSummaryResponse";
import api from "../../utils/Api";
import { formatCurrencyAsTR } from "../../utils/PriceFunctions";

const titleValue: string[] = [
	"Toplam",
	"Taksitsiz Toplam",
	"Taksitli Toplam",
	"Biten Taksitli Toplam",
	"Eklenen Taksitli Toplam",
];

const SummaryPage = () => {
	const [budgetSummaries, setBudgetSummaries] = useState<
		BudgetSummaryResponse[]
	>([]);

	useEffect(() => {
		api.get("/budget-summary-all").then(
			(response: BudgetSummaryResponse[]) => {
				setBudgetSummaries(response);
			}
		);
	}, []);

	return (
		<>
			{budgetSummaries?.map((budgetSummary: BudgetSummaryResponse) => {
				console.log(budgetSummary);
				return (
					<div
						key={"main-grid-for-" + budgetSummary.period}
						className="grid p-3"
					>
						<div
							key={"row-cell" + budgetSummary.period}
							className="col-6 col-offset-3 card bg-gray-900 p-4"
						>
							<TableBb
								key={"summary-table-for-" + budgetSummary.period}
								clasName="w-9"
								columns={
									[
										{
											dataField: "title",
											header: budgetSummary.period,
											dataClassName: "font-bold",
											headerClassName: "text-xl text-primary",
										},
										{
											dataField: "priceValue",
											header: "Tutar",
											headerClassName: "text-xl text-primary",
										},
									] as TableBbColumn[]
								}
								data={Object.entries(budgetSummary)
									.filter(([key, value]) => key !== "period")
									.map(([key, value], index) => {
										return {
											title: titleValue[index],
											priceValue: formatCurrencyAsTR(value),
										};
									})}
							/>
						</div>
					</div>
				);
			})}
		</>
	);
};

export default SummaryPage;
