import { Calendar } from "primereact/calendar";
import { CssClassName } from "../../constants/CssClassName";

interface CalenderBbProps {
	onChange: (e: any) => void;
	label: string;
	id: string;
	name?: string;
	value?: Date;
	className?: string;
	inputClassName?: string;
	fullwidth?: boolean;
	error?: boolean;
	errorHelperText?: string;
	dateFormat?: string;
}

const CalenderBb = (props: CalenderBbProps) => {
	const fullwidthClassName = props.fullwidth ? "w-full " : "";
	const inputClassName = props.inputClassName ?? "";
	let className = props.className ?? "";
	className += props.error ? " p-invalid" : "";
	className += props.error && props.errorHelperText ? " mb-2" : "";

	return (
		<span className={"p-float-label " + fullwidthClassName + className}>
			<Calendar
				inputId={props.id}
				name={props.name}
				className={fullwidthClassName + className}
				inputClassName={fullwidthClassName + inputClassName}
				onChange={props.onChange}
				value={props.value}
				dateFormat={props.dateFormat}
			/>
			<label htmlFor={props.id}>{props.label}</label>
			{props.error && (
				<span
					className={CssClassName.ERROR_TEXT_COLOR}
					style={{ position: "absolute", left: 0, bottom: -15 }}
				>
					{props.errorHelperText ?? ""}
				</span>
			)}
		</span>
	);
};

export default CalenderBb;
