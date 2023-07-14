import { Card } from "primereact/card";
import TopBar from "../components/TopBar";
import styles from "./Layout.module.css";

const Layout = (props: any) => {
	return (
		<div>
			<TopBar />
			<Card className={styles.hFillPage}>{props.children}</Card>
			<TopBar />
		</div>
	);
};

export default Layout;
