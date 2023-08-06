import { PeriodObject } from "../dto/PeriodObject";

export const getPeriodValue = (value: string): PeriodObject => {
	if (value.trim() === "") {
		return {} as PeriodObject;
	}
	return {
		year: parseInt(value.split("-")[0], 10),
		month: parseInt(value.split("-")[1], 10),
	} as PeriodObject;
};
