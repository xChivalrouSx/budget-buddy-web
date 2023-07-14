import { Password } from "primereact/password";
import { CssClassName } from "../../constants/CssClassName";

interface InputPasswordBbProps {
	onChange: (e: any) => void;
	label: string;
	id: string;
	name?: string;
	value?: string;
	className?: string;
	inputClassName?: string;
	fullwidth?: boolean;
	feedback?: boolean;
	autoComplete?: "on" | "off";
	error?: boolean;
	errorHelperText?: string;
}

const InputPasswordBb = (props: InputPasswordBbProps) => {
	const fullwidthClassName = props.fullwidth ? "w-full " : "";
	const inputClassName = props.inputClassName ?? "";
	let className = props.className ?? "";
	className += props.error ? " p-invalid" : "";
	className += props.error && props.errorHelperText ? " mb-2" : "";

	return (
		<span className={"p-float-label " + fullwidthClassName + className}>
			<Password
				inputId={props.id}
				name={props.name}
				className={fullwidthClassName + className}
				inputClassName={fullwidthClassName + inputClassName}
				feedback={props.feedback}
				onChange={props.onChange}
				value={props.value}
				autoComplete={props.autoComplete}
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

export default InputPasswordBb;
