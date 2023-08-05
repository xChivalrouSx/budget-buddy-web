import axios from "axios";
import { removeTokens } from "./AuthFunctions";

const serviceBaseUrl = process.env.REACT_APP_BUDGET_BUDDY_SERVICE_BASE_URL;

const instance = axios.create({
	baseURL: serviceBaseUrl,
	responseType: "json",
	headers: {
		"Content-Type": "application/json",
	},
});

instance.interceptors.response.use(
	(response) => {
		return response;
	},
	(error) => {
		const errorMessage = error?.response?.data?.message ?? "";
		const errorCode = error?.response?.data?.errorCode ?? "";
		if (errorCode === "007") {
			removeTokens();
			api.removeAuthHeader();
			window.location.reload();
		} else if (errorMessage !== "" && errorCode !== "") {
			console.log(
				"Known error from service. (FIX ME: show notification )",
				error
			);
			return Promise.resolve(error);
		}

		return Promise.reject(error);
	}
);

const api = {
	get<T = any>(url: string, params?: any) {
		return instance.get<T>(url, { ...params }).then((response) => {
			return response.data;
		});
	},

	post<T = any>(url: string, data: any) {
		return instance.post<T>(url, data).then((response) => {
			return response.data;
		});
	},

	put<T = any>(url: string, data: any, fullResponse = false) {
		return instance.put<T>(url, data).then((response) => {
			return fullResponse ? response : response.data;
		});
	},

	delete(url: string) {
		return instance.delete(url).then(() => {
			return true;
		});
	},

	setAuthHeader(accessToken: string | null) {
		if (accessToken && accessToken !== "undefined") {
			instance.defaults.headers.common[
				"Authorization"
			] = `Bearer ${accessToken}`;
		}
	},

	removeAuthHeader() {
		delete instance.defaults.headers.common["Authorization"];
	},
};

export default api;
