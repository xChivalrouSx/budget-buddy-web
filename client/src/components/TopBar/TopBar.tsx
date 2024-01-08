import { useFormik } from "formik";
import { Dialog } from "primereact/dialog";
import { Dropdown } from "primereact/dropdown";
import { FileUpload } from "primereact/fileupload";
import { Menu } from "primereact/menu";
import { Menubar } from "primereact/menubar";
import { useRef, useState } from "react";
import { CurrencyExchange } from "react-bootstrap-icons";
import { useLocation, useNavigate } from "react-router-dom";
import { BudgetDocumentImportRequest } from "../../dto/request/BudgetDocumentImportRequest";
import { SuccessResponse } from "../../dto/response/SuccessResponse";
import api from "../../utils/Api";
import { hasLocalStorageTokens, removeTokens } from "../../utils/AuthFunctions";
import { getPeriodAsString } from "../../utils/DateFunctions";
import ButtonBb from "../ButtonBb";
import InputTextBb from "../InputTextBb";
import styles from "./TopBar.module.css";
import TopBarValidation from "./TopBarValidation";

interface BudgetBank {
	bank: string;
	inputValue: string;
}

const loginFormikInitialValues = {
	file: null,
	bank: { bank: "", inputValue: "" } as BudgetBank,
	year: new Date().getFullYear(),
	month: new Date().getMonth() + 1,
};

const TopBar = () => {
	const menuRight = useRef(null);
	const location = useLocation();
	const navigate = useNavigate();

	const [showAddDialog, setShowAddDialog] = useState(false);
	const innerMenuItems = [
		{
			label: "Import Document",
			items: [
				{
					label: "Yapı Kredi - Excel",
					icon: "pi pi-fw pi-plus",
					command: () => {
						setShowAddDialog(true);
					},
				},
			],
		},
	];

	// const location = useLocation()
	// const [{ route }] = matchRoutes(routes, location)

	// return route.path

	const formik = useFormik({
		initialValues: loginFormikInitialValues,
		validationSchema: TopBarValidation,
		validateOnBlur: true,
		validateOnChange: true,
		onSubmit: () => {
			api.postMultipart("/budget-document/import", {
				file: formik.values.file,
				bank: formik.values.bank.inputValue,
				year: formik.values.year,
				month: formik.values.month,
			} as BudgetDocumentImportRequest).then((response: SuccessResponse) => {
				setShowAddDialog(false);
				const importedPeriod = getPeriodAsString(
					formik.values.year,
					formik.values.month
				);
				if (location.pathname === "/budget-detail/" + importedPeriod) {
					navigate(0);
				} else {
					navigate("/budget-detail/" + importedPeriod);
				}
			});
		},
	});

	const setFile = async (event: any) => {
		formik.setFieldValue("file", event.files[0]);
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
				header="Import (Yapı Kredi - Excel)"
				visible={showAddDialog}
				style={{ width: "20vw" }}
				onHide={() => setShowAddDialog(false)}
			>
				<form onSubmit={formik.handleSubmit}>
					<div className="card flex justify-content-center">
						<FileUpload mode="basic" accept="*/*" onSelect={setFile} />
					</div>
					<div className="mt-5">
						<Dropdown
							id="bank"
							name="bank"
							value={formik.values.bank}
							onChange={formik.handleChange}
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
							onChange={formik.handleChange}
							value={formik.values.year.toString()}
							error={formik.touched.year && Boolean(formik.errors.year)}
							errorHelperText={formik.errors.year}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<InputTextBb
							fullwidth
							id="month"
							name="month"
							label="Month"
							onChange={formik.handleChange}
							value={formik.values.month.toString()}
							error={
								formik.touched.month && Boolean(formik.errors.month)
							}
							errorHelperText={formik.errors.month}
							className="block"
						/>
					</div>
					<div className="mt-5">
						<ButtonBb
							fullwidth
							label={"IMPORT DOCUMENT"}
							type="button"
							onClick={() => {
								formik.submitForm();
							}}
						/>
					</div>
				</form>
			</Dialog>
		</>
	);
};

export default TopBar;
