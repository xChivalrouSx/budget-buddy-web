package chivalrous.budgetbuddy.exception;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class BbAuthException extends RuntimeException {

	private final ErrorMessage error;
	private Exception exception;

}
