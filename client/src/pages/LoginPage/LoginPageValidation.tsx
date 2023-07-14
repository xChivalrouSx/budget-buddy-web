import * as yup from "yup";

const LoginPageValidation = yup.object().shape({
	username: yup.string().required("Username can not be empty."),
	password: yup.string().required("Password can not be empty."),
	passwordConfirm: yup
		.string()
		.test(
			"passwordsShouldMatch",
			"Password fields should match.",
			(value, context) => {
				if (context.parent.type !== "Sign Up") {
					return true;
				}
				if (value === "") {
					return false;
				}
				return value === context.parent.password;
			}
		),
});

export default LoginPageValidation;
