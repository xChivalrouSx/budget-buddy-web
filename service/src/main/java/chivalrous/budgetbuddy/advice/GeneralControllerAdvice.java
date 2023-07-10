package chivalrous.budgetbuddy.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import chivalrous.budgetbuddy.dto.response.ErrorResponse;
import chivalrous.budgetbuddy.exception.FirebaseException;

@ControllerAdvice
public class GeneralControllerAdvice {

	@ExceptionHandler(value = FirebaseException.class)
	public ResponseEntity<ErrorResponse> handleException(FirebaseException exception) {
		ErrorResponse response = new ErrorResponse(exception.getError().getMessage(), exception.getError().getCode());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
