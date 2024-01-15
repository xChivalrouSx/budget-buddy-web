import { InputNumber } from "primereact/inputnumber";
import { CssClassName } from "../../constants/CssClassName";

interface InputNumberBbProps {
	onChange: (e: any) => void;
	label: string;
	id: string;
	name?: string;
	value?: number;
	minFractionDigits?: number;
	maxFractionDigits?: number;
	className?: string;
	fullwidth?: boolean;
	error?: boolean;
	errorHelperText?: string;
}

const InputNumberBb = (props: InputNumberBbProps) => {
	const fullwidthClass = props.fullwidth ? "w-full " : "";
	let className = props.className ?? "";
	className += props.error ? " p-invalid" : "";
	className += props.error && props.errorHelperText ? " mb-2" : "";

	return (
		<>
			<span
				className={"p-float-label " + fullwidthClass + className}
				style={{ position: "relative" }}
			>
				<InputNumber
					id={props.id}
					name={props.name}
					className={fullwidthClass + className}
					inputClassName={fullwidthClass + className}
					onValueChange={props.onChange}
					value={props.value}
					minFractionDigits={props.minFractionDigits ?? 2}
					maxFractionDigits={props.maxFractionDigits ?? 2}
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
		</>
	);
};

export default InputNumberBb;
