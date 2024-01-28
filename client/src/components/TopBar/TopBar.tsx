import { useFormik } from "formik";
import { Dialog } from "primereact/dialog";
import { Dropdown } from "primereact/dropdown";
import { FileUpload } from "primereact/fileupload";
import { Menu } from "primereact/menu";
import { Menubar } from "primereact/menubar";
import { useRef, useState } from "react";
import { CurrencyExchange } from "react-bootstrap-icons";
import { useLocation, useNavigate } from "react-router-dom";
import {
	BudgetDocumentImportRequest,
	BudgetSingleImportRequest,
	SingleImportDTO,
	TagAutoRequest,
} from "../../dto/request/BudgetDocumentImportRequest";
import { SuccessResponse } from "../../dto/response/SuccessResponse";
import api from "../../utils/Api";
import { hasLocalStorageTokens, removeTokens } from "../../utils/AuthFunctions";
import { getPeriodAsString } from "../../utils/DateFunctions";
import ButtonBb from "../ButtonBb";
import CalenderBb from "../CalenderBb";
import CheckBoxBb from "../CheckboxBb/CheckboxBb";
import ChipsBb from "../ChipsBb";
import InputNumberBb from "../InputNumberBb";
import InputTextBb from "../InputTextBb";
import styles from "./TopBar.module.css";
import TopBarValidation from "./TopBarValidation";

interface BudgetBank {
	bank: string;
	inputValue: string;
}

const FormikForDocumentInitialValues = {
	file: null,
	bank: { bank: "", inputValue: "" } as BudgetBank,
	year: new Date().getFullYear(),
	month: new Date().getMonth() + 1,
};

const FormikForBudgetInitialValues = {
	bank: { bank: "", inputValue: "" } as BudgetBank,
	year: new Date().getFullYear(),
	month: new Date().getMonth() + 1,
	date: new Date(),
	price: 0 as number,
	description: "",
	income: false as boolean,
};

const FormikForAutoTagInitialValues = {
	tag: "",
	storeType: "",
	storeNameKeywords: [],
};

const TopBar = () => {
	const menuRight = useRef(null);
	const location = useLocation();
	const navigate = useNavigate();

	const [showAddDocumentDialog, setShowAddDocumentDialog] = useState(false);
	const [showAddBudgetDialog, setShowAddBudgetDialog] = useState(false);
	const [showAddAutoTagDialog, setShowAddAutoTagDialog] = useState(false);

	const innerMenuItems = [
		{
			label: "Budget",
			items: [
				{
					label: "Import from File",
					icon: "pi pi-fw pi-plus",
					command: () => {
						setShowAddDocumentDialog(true);
					},
				},
				{
					label: "Import Single",
					icon: "pi pi-fw pi-plus",
					command: () => {
						setShowAddBudgetDialog(true);
					},
				},
			],
		},
		{
			label: "Tag",
			items: [
				{
					label: "Add Auto Tag",
					icon: "pi pi-fw pi-plus",
					command: () => {
						setShowAddAutoTagDialog(true);
					},
				},
			],
		},
	];

	const formikForDocument = useFormik({
		initialValues: FormikForDocumentInitialValues,
		validationSchema: TopBarValidation,
		validateOnBlur: true,
		validateOnChange: true,
		onSubmit: () => {
			api.postMultipart("/budget-document/import", {
				file: formikForDocument.values.file,
				bank: formikForDocument.values.bank.inputValue,
				year: formikForDocument.values.year,
				month: formikForDocument.values.month,
			} as BudgetDocumentImportRequest).then((response: SuccessResponse) => {
				setShowAddDocumentDialog(false);
				runAfterImportRequest(
					formikForDocument.values.year,
					formikForDocument.values.month
				);
			});
		},
	});

	const formikForBudget = useFormik({
		initialValues: FormikForBudgetInitialValues,
		validationSchema: undefined,
		validateOnBlur: true,
		validateOnChange: true,
		onSubmit: () => {
			api.post("/budget-document/single-import", {
				bank: formikForBudget.values.bank.inputValue, // TODO: Implement bank selection later (Should handle duplicate entry)
				year: formikForBudget.values.year,
				month: formikForBudget.values.month,
				income: formikForBudget.values.income,
				singleImportDTO: {
					date: formikForBudget.values.date,
					price: formikForBudget.values.price,
					description: formikForBudget.values.description,
				} as SingleImportDTO,
			} as BudgetSingleImportRequest).then((response: SuccessResponse) => {
				setShowAddBudgetDialog(false);
				runAfterImportRequest(
					formikForBudget.values.year,
					formikForBudget.values.month
				);
			});
		},
	});

	const formikForAutoTag = useFormik({
		initialValues: FormikForAutoTagInitialValues,
		validationSchema: undefined,
		validateOnBlur: true,
		validateOnChange: true,
		onSubmit: () => {
			api.post("/tag/auto", {
				tag: formikForAutoTag.values.tag,
				storeType: formikForAutoTag.values.storeType,
				storeNameKeywords: formikForAutoTag.values.storeNameKeywords,
			} as TagAutoRequest).then((response: SuccessResponse) => {
				setShowAddAutoTagDialog(false);
				formikForAutoTag.setValues(FormikForAutoTagInitialValues);
			});
		},
	});

	const runAfterImportRequest = (year: number, month: number) => {
		const importedPeriod = getPeriodAsString(year, month);
		if (location.pathname === "/budget-detail/" + importedPeriod) {
			navigate(0);
		} else {
			navigate("/budget-detail/" + importedPeriod);
		}
	};

	const setFile = async (event: any) => {
		formikForDocument.setFieldValue("file", event.files[0]);
	};

	const LogOutClick = () => {
		removeTokens();
		navigate("/login");
	};

	return (
		<>
			<Menubar
				className="h-4rem"
				start={
					<CurrencyExchange
						className={styles.siteIcon}
						onClick={() => {
							navigate("/");
						}}
					/>
				}
				end={
					hasLocalStorageTokens() && (
						<>
							<Menu
								model={innerMenuItems}
								ref={menuRight}
								popup
								popupAlignment="right"
							/>
							<ButtonBb
								icon="pi pi-bars"
								useText={false}
								onClick={(event) => {
									if (menuRight?.current) {
										(menuRight.current as Menu).toggle(event);
									}
								}}
							/>
							<ButtonBb label="Log Out" onClick={LogOutClick} />
						</>
					)
				}
			/>
			<Dialog
				header="Import Document"
				visible={showAddDocumentDialog}
				style={{ width: "20vw" }}
				onHide={() => setShowAddDocumentDialog(false)}
			>
				<form onSubmit={formikForDocument.handleSubmit}>
					<div className="card flex justify-content-center">
						<FileUpload mode="basic" accept="*/*" onSelect={setFile} />
					</div>
					<div className="mt-5">
						<Dropdown
							id="bank"
							name="bank"
							value={formikForDocument.values.bank}
							onChange={formikForDocument.handleChange}
							options={[
								{
									bank: "Yapı Kredi",
									inputValue: "yapi-kredi",
								} as BudgetBank,
								{
									bank: "Enpara",
									inputValue: "enpara",
								} as BudgetBank,
							]}
							optionLabel="bank"
							placeholder="Select a Bank"
							className="w-full"
						/>
					</div>
					<div className="mt-5">
						<InputTextBb
							fullwidth
							id="year"
							name="year"
							label="Year"
							onChange={formikForDocument.handleChange}
							value={formikForDocument.values.year.toString()}
							error={
								formikForDocument.touched.year &&
								Boolean(formikForDocument.errors.year)
							}
							errorHelperText={formikForDocument.errors.year}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<InputTextBb
							fullwidth
							id="month"
							name="month"
							label="Month"
							onChange={formikForDocument.handleChange}
							value={formikForDocument.values.month.toString()}
							error={
								formikForDocument.touched.month &&
								Boolean(formikForDocument.errors.month)
							}
							errorHelperText={formikForDocument.errors.month}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<ButtonBb
							fullwidth
							label={"IMPORT DOCUMENT"}
							type="button"
							onClick={() => {
								formikForDocument.submitForm();
							}}
						/>
					</div>
				</form>
			</Dialog>

			<Dialog
				header="Import Budget"
				visible={showAddBudgetDialog}
				style={{ width: "20vw" }}
				onHide={() => setShowAddBudgetDialog(false)}
			>
				<form onSubmit={formikForBudget.handleSubmit}>
					<div className="mt-5">
						<InputTextBb
							fullwidth
							id="year"
							name="year"
							label="Year"
							onChange={formikForBudget.handleChange}
							value={formikForBudget.values.year.toString()}
							error={
								formikForBudget.touched.year &&
								Boolean(formikForBudget.errors.year)
							}
							errorHelperText={formikForBudget.errors.year}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<InputTextBb
							fullwidth
							id="month"
							name="month"
							label="Month"
							onChange={formikForBudget.handleChange}
							value={formikForBudget.values.month.toString()}
							error={
								formikForBudget.touched.month &&
								Boolean(formikForBudget.errors.month)
							}
							errorHelperText={formikForBudget.errors.month}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<CalenderBb
							fullwidth
							id="date"
							name="date"
							label="Date"
							onChange={formikForBudget.handleChange}
							value={formikForBudget.values.date}
							dateFormat="yy-mm-dd"
							error={
								formikForBudget.touched.date &&
								Boolean(formikForBudget.errors.date)
							}
							// errorHelperText={moment(formikForBudget.errors.date)}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<InputNumberBb
							fullwidth
							id="price"
							name="price"
							label="Price"
							onChange={formikForBudget.handleChange}
							value={formikForBudget.values.price}
							minFractionDigits={2}
							maxFractionDigits={2}
							error={
								formikForBudget.touched.price &&
								Boolean(formikForBudget.errors.price)
							}
							errorHelperText={formikForBudget.errors.price}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<InputTextBb
							fullwidth
							id="description"
							name="description"
							label="Description"
							onChange={formikForBudget.handleChange}
							value={formikForBudget.values.description}
							error={
								formikForBudget.touched.description &&
								Boolean(formikForBudget.errors.description)
							}
							errorHelperText={formikForBudget.errors.description}
							className="block"
						/>
					</div>
					<div className="mt-5 card flex justify-content-center">
						<CheckBoxBb
							id="income"
							name="income"
							label="Income"
							onChange={formikForBudget.handleChange}
							checked={formikForBudget.values.income}
							error={
								formikForBudget.touched.income &&
								Boolean(formikForBudget.errors.income)
							}
							errorHelperText={formikForBudget.errors.income}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<ButtonBb
							fullwidth
							label={"IMPORT BUDGET"}
							type="button"
							onClick={() => {
								formikForBudget.submitForm();
							}}
						/>
					</div>
				</form>
			</Dialog>

			<Dialog
				header="Add Auto Tag"
				visible={showAddAutoTagDialog}
				style={{ width: "20vw" }}
				onHide={() => setShowAddAutoTagDialog(false)}
			>
				<form onSubmit={formikForAutoTag.handleSubmit}>
					<div className="mt-5">
						<InputTextBb
							fullwidth
							id="tag"
							name="tag"
							label="Tag"
							onChange={formikForAutoTag.handleChange}
							value={formikForAutoTag.values.tag.toString()}
							error={
								formikForAutoTag.touched.tag &&
								Boolean(formikForAutoTag.errors.tag)
							}
							errorHelperText={formikForAutoTag.errors.tag}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<InputTextBb
							fullwidth
							id="storeType"
							name="storeType"
							label="Store Type"
							onChange={formikForAutoTag.handleChange}
							value={formikForAutoTag.values.storeType.toString()}
							error={
								formikForAutoTag.touched.storeType &&
								Boolean(formikForAutoTag.errors.storeType)
							}
							errorHelperText={formikForAutoTag.errors.storeType}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<ChipsBb
							fullwidth
							id="storeNameKeywords"
							name="storeNameKeywords"
							label="Store Name Keywords For Tag"
							onChange={formikForAutoTag.handleChange}
							value={formikForAutoTag.values.storeNameKeywords}
							error={
								formikForAutoTag.touched.storeNameKeywords &&
								Boolean(formikForAutoTag.errors.storeNameKeywords)
							}
							// errorHelperText={formikForAutoTag.errors.storeNameKeywords}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<ButtonBb
							fullwidth
							label={"ADD AUTO TAG"}
							type="button"
							onClick={() => {
								formikForAutoTag.submitForm();
							}}
						/>
					</div>
				</form>
			</Dialog>
		</>
	);
};

export default TopBar;
