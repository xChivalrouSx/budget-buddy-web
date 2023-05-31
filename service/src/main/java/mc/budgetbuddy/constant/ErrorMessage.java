package mc.budgetbuddy.constant;

public enum ErrorMessage {
	FIREBASE_CAN_NOT_INITIALIZE("101", "Bağlantısı sırasında hata meydana geldi.");

	private final String message;
	private final String code;

	ErrorMessage(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}
}
