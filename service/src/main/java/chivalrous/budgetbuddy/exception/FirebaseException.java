package chivalrous.budgetbuddy.exception;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FirebaseException extends RuntimeException {

	private final ErrorMessage error;
	private final Exception exception;

}
