package chivalrous.budgetbuddy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import chivalrous.budgetbuddy.dto.response.BudgetResponse;
import chivalrous.budgetbuddy.model.User;
import chivalrous.budgetbuddy.repository.BudgetRepository;
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

}
