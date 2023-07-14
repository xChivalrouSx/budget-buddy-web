import { Navigate, Route, Routes } from "react-router-dom";
import PrivateRoute from "./components/Route/PrivateRoute";
import PublicRoute from "./components/Route/PublicRoute";
import Layout from "./hoc/Layout";
import LoginPage from "./pages/LoginPage/LoginPage";

const App = () => {
	let routes = (
		<Routes>
			<Route path="/login" element={<PublicRoute />}>
				<Route path="/login" element={<LoginPage />} />
			</Route>
			<Route path="/" element={<PrivateRoute />}>
				<Route path="/" element={<div>MAIN PAGE</div>} />
				<Route path="*" element={<Navigate replace to={"/"} />} />
			</Route>
		</Routes>
	);

	return <Layout>{routes}</Layout>;
};

export default App;
