import { Navigate, Outlet } from "react-router-dom";
import { hasLocalStorageTokens } from "../../../utils/AuthFunctions";

const PrivateRoute = () => {
	return hasLocalStorageTokens() ? (
		<Outlet />
	) : (
		<Navigate replace to={"/login"} />
	);
};

export default PrivateRoute;
