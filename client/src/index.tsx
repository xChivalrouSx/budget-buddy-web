import ReactDOM from "react-dom";
import { HashRouter } from "react-router-dom";
import App from "./App";
import "./index.css";

import "primeflex/primeflex.css";
import "primeicons/primeicons.css";
import "primereact/resources/primereact.min.css";
import "primereact/resources/themes/soho-dark/theme.css";
import { hasLocalStorageTokens, setApiAuthHeader } from "./utils/AuthFunctions";

function render() {
	if (hasLocalStorageTokens()) {
		setApiAuthHeader();
	}

	ReactDOM.render(
		<HashRouter>
			<App />
		</HashRouter>,
		document.getElementById("root")
	);
}

render();

if (process.env.NODE_ENV === "development" && module.hot) {
	module.hot.accept("./App", render);
}
