package chivalrous.budgetbuddy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
	UNEXPECTED_ERROR("000", "Unexpected error occured."),
	AUTHENTICATION_ERROR("001", "Authentication error."),
	AUTHENTICATION_NOT_FOUND("002", "Authentication error."),
	AUTHENTICATION_NOT_VALID("003", "Authentication token not valid."),
	USER_COULD_NOT_CREATE("004", "User could not create."),
	USER_CREATION_DISABLE("005", "User creation is disabled."),
	BAD_CREDENTIALS("006", "Username or password not valid."),
	SESSION_EXPIRED("007", "Session expired."),
	FIREBASE_COULD_NOT_INITIALIZE("101", "Error while connecting to google services."),
	FIREBASE_DATA_COULD_NOT_GET("102", "Error while getting data."),
	FIREBASE_DATA_TAG_COULD_NOT_GET("102.1", "Error while getting data."),
	FIREBASE_DATA_COULD_NOT_DELETE("103", "Error while deleting data."),
	DATE_CANNOT_PARSE("201", "Improper date format."),
	FILE_COULD_NOT_READ("301", "Document could not read."),
	DOCUMENT_FORMAT_NOT_VALID("302", "Document format not valid."),
	INVALID_YEAR("303", "Year is not valid."),
	INVALID_MONTH("304", "Month is not valid."),
	INVALID_DECIMAL_FORMAT("305", "Decimal format is not valid."),
	BUDGET_BANK_NOT_FOUND("306", "Bank document not supported.");

	private final String code;
	private final String message;

}
