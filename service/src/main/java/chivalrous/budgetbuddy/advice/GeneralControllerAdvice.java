package chivalrous.budgetbuddy.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.dto.response.ErrorResponse;
import chivalrous.budgetbuddy.exception.FirebaseException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GeneralControllerAdvice {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception exception) {
		log.error(exception.getMessage(), exception);
		ErrorResponse errorResponseModel = new ErrorResponse(ErrorMessage.UNEXPECTED_ERROR.getMessage(), ErrorMessage.UNEXPECTED_ERROR.getCode());
		return new ResponseEntity<>(errorResponseModel, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = FirebaseException.class)
	public ResponseEntity<ErrorResponse> handleException(FirebaseException exception) {
		log.error(exception.getMessage(), exception);
		ErrorResponse response = new ErrorResponse(exception.getError().getMessage(), exception.getError().getCode());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exception) {
		log.error(exception.getMessage(), exception);
		ErrorResponse errorResponseModel = new ErrorResponse(ErrorMessage.BAD_CREDENTIALS.getMessage(), ErrorMessage.BAD_CREDENTIALS.getCode());
		return new ResponseEntity<>(errorResponseModel, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleException(ExpiredJwtException exception) {
		log.error(exception.getMessage(), exception);
		ErrorResponse errorResponseModel = new ErrorResponse(ErrorMessage.SESSION_EXPIRED.getMessage(), ErrorMessage.SESSION_EXPIRED.getCode());
		return new ResponseEntity<>(errorResponseModel, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = { SignatureException.class, MalformedJwtException.class })
	public ResponseEntity<ErrorResponse> handleException(JwtException exception) {
		log.error(exception.getMessage(), exception);
		ErrorResponse errorResponseModel = new ErrorResponse(ErrorMessage.AUTHENTICATION_ERROR.getMessage(), ErrorMessage.AUTHENTICATION_ERROR.getCode());
		return new ResponseEntity<>(errorResponseModel, HttpStatus.UNAUTHORIZED);
	}

}
