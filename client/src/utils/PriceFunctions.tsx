export const formatCurrencyAsTR = (value: number): string => {
	return value.toLocaleString("tr-TR", { style: "currency", currency: "TRY" });
};
