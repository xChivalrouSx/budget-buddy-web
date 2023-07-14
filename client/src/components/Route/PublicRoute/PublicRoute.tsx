import { Navigate, Outlet } from "react-router-dom";
import { hasLocalStorageTokens } from "../../../utils/AuthFunctions";

const PublicRoute = () => {
	return hasLocalStorageTokens() ? <Navigate replace to={"/"} /> : <Outlet />;
};

export default PublicRoute;
