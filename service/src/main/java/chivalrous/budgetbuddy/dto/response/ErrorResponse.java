package chivalrous.budgetbuddy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

	private String message;
	private String errorCode;

}
