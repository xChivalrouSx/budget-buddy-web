import { Menubar } from "primereact/menubar";
import { CurrencyExchange } from "react-bootstrap-icons";
import { useNavigate } from "react-router-dom";
import { hasLocalStorageTokens, removeTokens } from "../../utils/AuthFunctions";
import ButtonBb from "../ButtonBb";
import styles from "./TopBar.module.css";

const TopBar = () => {
	const navigate = useNavigate();

	// FIXME: Fill the items of menu bar items
	const items = [
		{
			// label: 'Home',
		},
	];

	const LogOutClick = () => {
		removeTokens();
		navigate("/login");
	};

	return (
		<Menubar
			className="h-4rem"
			model={items}
			start={<CurrencyExchange className={styles.siteIcon} />}
			end={
				hasLocalStorageTokens() && (
					<ButtonBb label="Log Out" onClick={LogOutClick} />
				)
			}
		/>
	);
};

export default TopBar;
