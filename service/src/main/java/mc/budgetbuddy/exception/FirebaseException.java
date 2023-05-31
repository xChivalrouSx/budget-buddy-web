package mc.budgetbuddy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mc.budgetbuddy.constant.ErrorMessage;

@Getter
@AllArgsConstructor
public class FirebaseException extends RuntimeException {

	private final ErrorMessage error;

}
