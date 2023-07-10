package chivalrous.budgetbuddy.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
	FIREBASE_CAN_NOT_INITIALIZE("101", "Error while connecting to google services."),
	DATE_CANNOT_PARSE("201", "Improper date format."),
	FILE_COULD_NOT_READ("301", "Document could not read.");

	private final String message;
	private final String code;

}
