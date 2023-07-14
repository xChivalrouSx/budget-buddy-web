import { useFormik } from "formik";

import { useNavigate } from "react-router-dom";
import ButtonBb from "../../components/ButtonBb";
import InputPasswordBb from "../../components/InputPasswordBb";
import InputTextBb from "../../components/InputTextBb";
import SelectButtonBb from "../../components/SelectButtonBb";
import { AuthRequest } from "../../dto/request/AuthRequest";
import { AuthResponse } from "../../dto/response/AuthResponse";
import { SuccessResponse } from "../../dto/response/SuccessResponse";
import api from "../../utils/Api";
import { setTokensOnLocalStorage } from "../../utils/AuthFunctions";
import LoginPageValidation from "./LoginPageValidation";

const options = ["Sign In", "Sign Up"];
const loginFormikInitialValues = {
	type: options[0],
	username: "",
	password: "",
	passwordConfirm: "",
};

const LoginPage = () => {
	const navigate = useNavigate();
	const formik = useFormik({
		initialValues: loginFormikInitialValues,
		validationSchema: LoginPageValidation,
		validateOnBlur: true,
		validateOnChange: true,
		onSubmit: () => {
			if (formik.values.type === options[1]) {
				api.post("/user", {
					username: formik.values.username,
					password: formik.values.password,
				} as AuthRequest).then((response: SuccessResponse) => {
					console.log(response);
					formik.setValues(loginFormikInitialValues);
				});
			} else if (formik.values.type === options[0]) {
				api.post("/authenticate", {
					username: formik.values.username,
					password: formik.values.password,
				} as AuthRequest).then((response: AuthResponse) => {
					console.log(response);
					if (response !== undefined) {
						setTokensOnLocalStorage(response.token);
						api.setAuthHeader(response.token);
						navigate("/");
					}
				});
			}
		},
	});

	return (
		<form onSubmit={formik.handleSubmit}>
			<div className="grid">
				<div className="col-4 col-offset-4 card bg-gray-900">
					<div className="grid">
						<div className="col-8 col-offset-2 p-fluid">
							<SelectButtonBb
								name="type"
								className="my-4"
								onChange={formik.handleChange}
								selectOptions={options}
								selectedValue={formik.values.type}
							/>
						</div>
					</div>
					<div className="grid mt-2">
						<div className="col-8 col-offset-2 flex justify-content-center">
							<InputTextBb
								fullwidth
								id="username"
								name="username"
								label="Username"
								onChange={formik.handleChange}
								value={formik.values.username}
								error={
									formik.touched.username &&
									Boolean(formik.errors.username)
								}
								errorHelperText={formik.errors.username}
							/>
						</div>
					</div>
					<div className="grid mt-4">
						<div className="col-8 col-offset-2 flex justify-content-center">
							<InputPasswordBb
								fullwidth
								id="password"
								name="password"
								label="Password"
								onChange={formik.handleChange}
								value={formik.values.password}
								inputClassName="w-full"
								feedback={false}
								autoComplete="off"
								error={
									formik.touched.password &&
									Boolean(formik.errors.password)
								}
								errorHelperText={formik.errors.password}
							/>
						</div>
					</div>
					{formik.values.type === options[1] && (
						<div className="grid mt-4">
							<div className="col-8 col-offset-2 flex justify-content-center">
								<InputPasswordBb
									fullwidth
									id="passwordConfirm"
									name="passwordConfirm"
									label="Confirm Password"
									onChange={formik.handleChange}
									value={formik.values.passwordConfirm}
									inputClassName="w-full"
									feedback={false}
									autoComplete="off"
									error={
										formik.touched.passwordConfirm &&
										Boolean(formik.errors.passwordConfirm)
									}
									errorHelperText={formik.errors.passwordConfirm}
								/>
							</div>
						</div>
					)}
					<div className="grid my-4">
						<div className="col-8 col-offset-2 flex justify-content-center">
							<ButtonBb
								fullwidth
								label={formik.values.type}
								type="submit"
							/>
						</div>
					</div>
				</div>
			</div>
		</form>
	);
};

export default LoginPage;
