package chivalrous.budgetbuddy.dto.response;

import java.util.Date;
import java.util.List;

import chivalrous.budgetbuddy.model.Budget;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BudgetResponse {

	private String id;
	private Date date;
	private String storeName;
	private String storeType;
	private Double price;
	private Double giftPoint;
	private String cardType;
	private int totalInstallment;
	private int paidInstallment;
	private int remainingInstallment;
	private Double priceForInstallment;
	private List<String> tags;
	private String period;
	private int periodInt;
	private String bank;

	public static BudgetResponse fromBudget(Budget budget) {
		return BudgetResponse.builder()
				.id(budget.getId())
				.date(budget.getDate())
				.storeName(budget.getStoreName())
				.storeType(budget.getStoreType())
				.price(budget.getPrice())
				.giftPoint(budget.getGiftPoint())
				.cardType(budget.getCardType())
				.totalInstallment(budget.getTotalInstallment())
				.paidInstallment(budget.getPaidInstallment())
				.remainingInstallment(budget.getRemainingInstallment())
				.priceForInstallment(budget.getPriceForInstallment())
				.tags(budget.getTags())
				.period(budget.getPeriod())
				.periodInt(budget.getPeriodInt())
				.bank(budget.getBank())
				.build();
	}
}
