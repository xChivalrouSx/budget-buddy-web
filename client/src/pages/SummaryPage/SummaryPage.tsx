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
import { Panel, PanelHeaderTemplateOptions } from "primereact/panel";
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

var template = (options: PanelHeaderTemplateOptions) => {
	const className = `${options.className} justify-content-start py-2`;

	return (
		<div className={className}>
			<span className={options.titleClassName}>{options.titleElement}</span>
		</div>
	);
};

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

	var delayBetweenPoints = 10;
	var started = [] as any[];
	return (
		<div className="grid">
			<div className="col-12 bg-gray-900">
				<Line
					data={{
						labels: budgetSummaries.map((item) => item.period).reverse(),
						datasets: [
							{
								label: "Total Price (Toplam Tutar)",

								data: budgetSummaries
									.map(
										(item) =>
											item.summaryDetailList.find(
												(x) => x.order === 2
											)?.value ?? 0
									)
									.reverse(),
								borderColor: "rgb(255, 99, 132)",
								backgroundColor: "rgba(255, 99, 132, 0.5)",
								animation: {
									onProgress: (context) => {
										let delay = 0;
										let index = context.currentStep;
										var chart = context.chart;
										if (!started[index]) {
											delay = index * delayBetweenPoints;
											started[index] = true;
										}
										var { x, y } =
											index > 0
												? chart
														.getDatasetMeta(0)
														.data[index - 1].getProps(
															["x", "y"],
															true
														)
												: {
														x: 0,
														y: chart.scales.y.getPixelForValue(
															100
														),
												  };

										return {
											x: {
												easing: "linear",
												duration: delayBetweenPoints,
												from: x,
												delay,
											},
											y: {
												easing: "linear",
												duration: delayBetweenPoints * 500,
												from: y,
												delay,
											},
											skip: {
												type: "boolean",
												duration: delayBetweenPoints,
												from: true,
												to: false,
												delay: delay,
											},
										};
									},
								},
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
					className="w-8 h-24rem m-auto "
				/>
			</div>

			{budgetSummaries?.map((budgetSummary: BudgetSummaryResponse) => {
				return (
					<div key={"row-cell" + budgetSummary.period} className="col-6">
						<div className="bg-gray-900 w-12 p-3 inline-flex align-items-end">
							<TableBb
								key={"summary-table-for-" + budgetSummary.period}
								clasName="w-9"
								rowClassName="text-xs"
								columns={
									[
										{
											dataField: "title",
											header: budgetSummary.period,
											dataClassName: "font-bold",
											headerClassName: "text-sm text-primary",
										},
										{
											dataField: "priceValue",
											header: "Price (Tutar)",
											headerClassName: "text-sm text-primary",
										},
									] as TableBbColumn[]
								}
								data={budgetSummary.summaryDetailList
									.filter((x) => x.showDefault)
									.sort(
										(firstItem, secondItem) =>
											firstItem.order - secondItem.order
									)
									.map((x) => {
										return {
											title: x.title,
											priceValue: formatCurrencyAsTR(x.value),
										};
									})}
							/>
							<div className="ml-3 w-3 h-full">
								<Panel
									headerTemplate={template}
									className="mb-2 text-xs"
									header="Total Card Spending"
								>
									<p className="p-0 m-0">
										{formatCurrencyAsTR(
											budgetSummary.summaryDetailList.find(
												(x) => x.order === -1
											)?.value ?? 0
										)}
									</p>
								</Panel>
								<Panel
									headerTemplate={template}
									className="mb-2 text-xs"
									header="Total Not Card Spending"
								>
									<p className="m-0">
										{formatCurrencyAsTR(
											budgetSummary.summaryDetailList.find(
												(x) => x.order === -2
											)?.value ?? 0
										)}
									</p>
								</Panel>
								<Panel
									headerTemplate={template}
									className="mb-2 text-xs"
									header="Result (Income - Spending)"
								>
									<p className="m-0">
										{formatCurrencyAsTR(
											budgetSummary.summaryDetailList.find(
												(x) => x.order === -3
											)?.value ?? 0
										)}
									</p>
								</Panel>
								<ButtonBb
									label="DETAIL >>"
									className="mt-1 w-full h-3rem"
									onClick={() => {
										btnDetailClick(budgetSummary.period);
									}}
								/>
							</div>
						</div>
					</div>
				);
			})}
		</div>
	);
};

export default SummaryPage;
