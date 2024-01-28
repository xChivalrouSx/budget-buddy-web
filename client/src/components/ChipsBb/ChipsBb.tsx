import { Chips } from "primereact/chips";
import { CssClassName } from "../../constants/CssClassName";

interface ChipsBbBbProps {
	onChange: (e: any) => void;
	label: string;
	id: string;
	name?: string;
	value?: string[];
	className?: string;
	fullwidth?: boolean;
	error?: boolean;
	errorHelperText?: string;
}

const ChipsBb = (props: ChipsBbBbProps) => {
	const fullwidthClassName = props.fullwidth ? "w-full " : "";
	let className = props.className ?? "";
	className += props.error ? " p-invalid" : "";
	className += props.error && props.errorHelperText ? " mb-2" : "";

	return (
		<span className={"p-float-label " + fullwidthClassName + className}>
			<Chips
				id={props.id}
				name={props.name}
				className={fullwidthClassName + className}
				onChange={props.onChange}
				value={props.value}
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

export default ChipsBb;
