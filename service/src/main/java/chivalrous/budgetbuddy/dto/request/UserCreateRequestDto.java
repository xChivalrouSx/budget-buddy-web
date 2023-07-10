package chivalrous.budgetbuddy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateRequestDto {

	private String username;
	private String password;

}
