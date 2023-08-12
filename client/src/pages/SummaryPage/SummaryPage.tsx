import {
	CategoryScale,
	Chart as ChartJS,
	Legend,
	LineElement,
	LinearScale,
	PointElement,
	Title,
	Tooltip,
} from "chart.js";
import { useEffect, useState } from "react";
import { Line } from "react-chartjs-2";
import { useNavigate } from "react-router-dom";
import ButtonBb from "../../components/ButtonBb";
import TableBb from "../../components/TableBb";
import { TableBbColumn } from "../../components/TableBb/TableBbObjects";
import { BudgetSummaryResponse } from "../../dto/response/BudgetSummaryResponse";
import api from "../../utils/Api";
import { formatCurrencyAsTR } from "../../utils/PriceFunctions";

ChartJS.register(
	CategoryScale,
	LinearScale,
	PointElement,
	LineElement,
	Title,
	Tooltip,
	Legend
);

const titleValue: string[] = [
	"Total (Toplam)",
	"Total Price Without Installment (Taksitsiz Toplam)",
	"Total Price With Installment (Taksitli Toplam)",
	"Total Price Ending Installment (Biten Taksitli Toplam)",
	"Total Price Starting Installment (Eklenen Taksitli Toplam)",
];

const SummaryPage = () => {
	const navigate = useNavigate();

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

	const btnDetailClick = (period: string) => {
		navigate("/budget-detail/" + period);
	};

	return (
		<div className="grid">
			<div className="col-12 bg-gray-900 mb-3">
				<Line
					data={{
						labels: budgetSummaries.map((item) => item.period).reverse(),
						datasets: [
							{
								label: "Total Price (Toplam Tutar)",
								data: budgetSummaries
									.map((item) => item.totalPrice)
									.reverse(),
								borderColor: "rgb(255, 99, 132)",
								backgroundColor: "rgba(255, 99, 132, 0.5)",
							},
						],
					}}
					options={{
						responsive: true,
						plugins: {
							legend: {
								position: "top" as const,
							},
							title: {
								display: true,
								text: "Monthly Total Price (Aylık Toplam Tutar)",
							},
						},
					}}
					className="w-8 h-26rem m-auto "
				/>
			</div>

			{budgetSummaries?.map((budgetSummary: BudgetSummaryResponse) => {
				return (
					<div key={"row-cell" + budgetSummary.period} className="col-6">
						<div className="bg-gray-900 w-12 p-3 inline-flex align-items-end">
							<TableBb
								key={"summary-table-for-" + budgetSummary.period}
								clasName="w-10"
								rowClassName="h-3rem"
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
											header: "Price (Tutar)",
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
							<ButtonBb
								label="DETAIL >>"
								className="ml-2 mb-1 w-2 h-3rem"
								onClick={() => {
									btnDetailClick(budgetSummary.period);
								}}
							/>
						</div>
					</div>
				);
			})}
		</div>
	);
};

export default SummaryPage;
