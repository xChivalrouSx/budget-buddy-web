package chivalrous.budgetbuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.dto.BudgetSummaryDetailDTO;
import chivalrous.budgetbuddy.dto.response.BudgetDetailResponse;
import chivalrous.budgetbuddy.dto.response.BudgetGroupStoreTypeResponse;
import chivalrous.budgetbuddy.dto.response.BudgetResponse;
import chivalrous.budgetbuddy.dto.response.BudgetSummaryResponse;
import chivalrous.budgetbuddy.model.Budget;
import chivalrous.budgetbuddy.model.User;
import chivalrous.budgetbuddy.repository.BudgetRepository;
import chivalrous.budgetbuddy.util.PriceUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {

	private final BudgetRepository budgetRepository;
	private final UserService userService;

	public List<BudgetResponse> getBudgetsByPeriod(String period) {
		User user = userService.getAuthenticatedUser();
		return budgetResponseFromBudget(budgetRepository.getBudgetsByPeriod(period, user.getId()));
	}

	private List<BudgetResponse> budgetResponseFromBudget(List<Budget> budgets) {
		return budgets.stream().map(BudgetResponse::fromBudget).collect(Collectors.toList());
	}

	public BudgetSummaryResponse getBudgetSummaryByPeriod(String period) {
		User user = userService.getAuthenticatedUser();
		List<Budget> currentPeriodBudgets = budgetRepository.getBudgetsByPeriod(period, user.getId());
		return calculateSummary(currentPeriodBudgets, period);
	}

	public List<BudgetSummaryResponse> getAllBudgetSummary() {
		User user = userService.getAuthenticatedUser();
		List<BudgetSummaryResponse> budgetSummaryList = new ArrayList<>();

		List<String> distinctPeriodList = budgetRepository.getAllDistinctPeriod();
		for (String tmpPeriod : distinctPeriodList) {
			List<Budget> currentPeriodBudgets = budgetRepository.getBudgetsByPeriod(tmpPeriod, user.getId());
			if (currentPeriodBudgets.isEmpty()) {
				break;
			}
			budgetSummaryList.add(calculateSummary(currentPeriodBudgets, tmpPeriod));
		}

		return budgetSummaryList;
	}

	private BudgetSummaryResponse calculateSummary(List<Budget> currentPeriodBudgets, String period) {
		List<BudgetSummaryDetailDTO> resultList = new ArrayList<>();

		List<Budget> outcomePeriodBudgets = currentPeriodBudgets.stream().filter(p -> !p.isIncome()).collect(Collectors.toList());

		Double totalPrice = outcomePeriodBudgets.stream()
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		resultList.add(new BudgetSummaryDetailDTO("Total Spend (Toplam Ödeme)", PriceUtil.formatPrice(totalPrice), true, 2));

		Double totalPriceWithoutInstallment = outcomePeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() == 1)
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		resultList.add(
				new BudgetSummaryDetailDTO("Total Spend Without Installment (Taksitsiz Toplam)", PriceUtil.formatPrice(totalPriceWithoutInstallment), true, 3));

		Double totalPriceWithInstallment = outcomePeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() > 1)
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		resultList.add(new BudgetSummaryDetailDTO("Total Spend With Installment (Taksitli Toplam)", PriceUtil.formatPrice(totalPriceWithInstallment), true, 4));

		Double totalPriceEndingInstallment = outcomePeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() > 1 && p.getTotalInstallment() == p.getPaidInstallment())
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		resultList.add(
				new BudgetSummaryDetailDTO("Total Ending Installment (Biten Taksitli Toplam)", PriceUtil.formatPrice(totalPriceEndingInstallment), true, 51));

		Double totalPriceStartingInstallment = outcomePeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() > 1 && p.getPaidInstallment() == 1)
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		resultList.add(new BudgetSummaryDetailDTO("Total Starting Installment (Eklenen Taksitli Toplam)", PriceUtil.formatPrice(totalPriceStartingInstallment),
				true, 6));

		List<Budget> incomePeriodBudgets = currentPeriodBudgets.stream().filter(p -> p.isIncome()).collect(Collectors.toList());
		Double totalIncome = incomePeriodBudgets.stream()
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		resultList.add(new BudgetSummaryDetailDTO("Total Income (Toplam Gelir)", PriceUtil.formatPrice(totalIncome), true, 1));

		Double cardSpendingResult = currentPeriodBudgets.stream()
				.filter(p -> !p.getBank().equals(""))
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		resultList.add(new BudgetSummaryDetailDTO("Total Card Spending", PriceUtil.formatPrice(cardSpendingResult), false, -1));
		resultList.add(new BudgetSummaryDetailDTO("Total Not Card Spending", PriceUtil.formatPrice(totalPrice - cardSpendingResult), false, -2));
		resultList.add(new BudgetSummaryDetailDTO("Result (Income - Spend)", PriceUtil.formatPrice(totalIncome - totalPrice), false, -3));

		return new BudgetSummaryResponse(period, resultList);
	}

	public BudgetDetailResponse getBudgetDetailByPeriod(String period) {
		User user = userService.getAuthenticatedUser();
		List<Budget> currentPeriodBudgets = budgetRepository.getBudgetsByPeriod(period, user.getId());
		BudgetDetailResponse budgetDetailResponse = calculateDetail(currentPeriodBudgets, period);
		budgetDetailResponse.setBudgets(budgetResponseFromBudget(currentPeriodBudgets));
		return budgetDetailResponse;
	}

	private BudgetDetailResponse calculateDetail(List<Budget> currentPeriodBudgets, String period) {
		Map<String, Double> mapForStoreTypeTotalPrice = currentPeriodBudgets.stream().filter(p -> p.getStoreType() != null)
				.collect(Collectors.groupingBy(Budget::getStoreType, Collectors.summingDouble(Budget::getPriceForInstallment)));

		BudgetDetailResponse budgetDetailResponse = new BudgetDetailResponse();
		budgetDetailResponse.setPriceAsBudgetStoreType(new ArrayList<>());
		for (Map.Entry<String, Double> entry : mapForStoreTypeTotalPrice.entrySet()) {
			budgetDetailResponse.getPriceAsBudgetStoreType().add(new BudgetGroupStoreTypeResponse(entry.getKey(), PriceUtil.formatPrice(entry.getValue())));
		}
		return budgetDetailResponse;
	}
}
