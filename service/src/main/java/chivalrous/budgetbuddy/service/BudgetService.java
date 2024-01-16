package chivalrous.budgetbuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
		List<Budget> outcomePeriodBudgets = currentPeriodBudgets.stream().filter(p -> !p.isIncome()).collect(Collectors.toList());
		Double totalPrice = outcomePeriodBudgets.stream()
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		Double totalPriceWithoutInstallment = outcomePeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() == 1)
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		Double totalPriceWithInstallment = outcomePeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() > 1)
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		Double totalPriceEndingInstallment = outcomePeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() > 1 && p.getTotalInstallment() == p.getPaidInstallment())
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		Double totalPriceStartingInstallment = outcomePeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() > 1 && p.getPaidInstallment() == 1)
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();

		List<Budget> incomePeriodBudgets = currentPeriodBudgets.stream().filter(p -> p.isIncome()).collect(Collectors.toList());
		Double totalIncome = incomePeriodBudgets.stream()
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();

		return new BudgetSummaryResponse(
				period,
				PriceUtil.formatPrice(totalIncome),
				PriceUtil.formatPrice(totalPrice),
				PriceUtil.formatPrice(totalPriceWithoutInstallment),
				PriceUtil.formatPrice(totalPriceWithInstallment),
				PriceUtil.formatPrice(totalPriceEndingInstallment),
				PriceUtil.formatPrice(totalPriceStartingInstallment));
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
