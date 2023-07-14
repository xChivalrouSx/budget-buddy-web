import api from "./Api";

export const bbTokenString = "bbToken";

export const hasLocalStorageTokens = (): boolean => {
	return localStorage.getItem(bbTokenString) !== null;
};

export const getRefreshTokenFromStorage = (): string | null => {
	return localStorage.getItem(bbTokenString) ?? null;
};

export const setTokensOnLocalStorage = (bbToken: string) => {
	localStorage.setItem(bbTokenString, bbToken);
};

export const removeTokens = () => {
	localStorage.removeItem(bbTokenString);
};

export const setApiAuthHeader = () => {
	const tokenString = localStorage.getItem(bbTokenString);
	api.setAuthHeader(tokenString);
};
