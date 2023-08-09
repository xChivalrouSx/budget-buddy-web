import * as yup from "yup";

const TopBarValidation = yup.object().shape({
	year: yup.string().required("Document year should not be empty."),
	month: yup.string().required("Document month should not be empty."),
	bank: yup
		.mixed()
		.test("bankShouldSelect", "Bank should select.", (value) => {
			return value !== null;
		}),
	file: yup
		.mixed()
		.test("fileShouldSelect", "File should select.", (value) => {
			return value !== null;
		}),
});

export default TopBarValidation;
