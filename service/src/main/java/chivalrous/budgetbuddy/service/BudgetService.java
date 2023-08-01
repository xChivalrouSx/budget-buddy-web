package chivalrous.budgetbuddy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
		return budgetRepository.getBudgetsByPeriod(period, user.getId()).stream().map(BudgetResponse::fromBudget).collect(Collectors.toList());
	}

	public BudgetSummaryResponse getBudgetSummaryByPeriod(String period) {
		User user = userService.getAuthenticatedUser();
		List<Budget> currentPeriodBudgets = budgetRepository.getBudgetsByPeriod(period, user.getId());

		Double totalPrice = currentPeriodBudgets.stream()
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		Double totalPriceWithoutInstallment = currentPeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() == 1)
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		Double totalPriceWithInstallment = currentPeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() > 1)
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		Double totalPriceEndingInstallment = currentPeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() > 1 && p.getTotalInstallment() == p.getPaidInstallment())
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();
		Double totalPriceStartingInstallment = currentPeriodBudgets.stream()
				.filter(p -> p.getTotalInstallment() > 1 && p.getPaidInstallment() == 1)
				.mapToDouble(Budget::getPriceForInstallment)
				.sum();

		return new BudgetSummaryResponse(
				period,
				PriceUtil.formatPrice(totalPrice),
				PriceUtil.formatPrice(totalPriceWithoutInstallment),
				PriceUtil.formatPrice(totalPriceWithInstallment),
				PriceUtil.formatPrice(totalPriceEndingInstallment),
				PriceUtil.formatPrice(totalPriceStartingInstallment));
	}

}
