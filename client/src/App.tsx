import { Navigate, Route, Routes } from "react-router-dom";
import PrivateRoute from "./components/Route/PrivateRoute";
import PublicRoute from "./components/Route/PublicRoute";
import Layout from "./hoc/Layout";
import DetailPage from "./pages/DetailPage";
import LoginPage from "./pages/LoginPage";
import SummaryPage from "./pages/SummaryPage";

const App = () => {
	let routes = (
		<Routes>
			<Route path="/login" element={<PublicRoute />}>
				<Route path="/login" element={<LoginPage />} />
			</Route>
			<Route path="/" element={<PrivateRoute />}>
				<Route path="/" element={<SummaryPage />} />
				<Route path="/budget-detail/:period" element={<DetailPage />} />
				<Route path="*" element={<Navigate replace to={"/"} />} />
			</Route>
		</Routes>
	);

	return <Layout>{routes}</Layout>;
};

export default App;
