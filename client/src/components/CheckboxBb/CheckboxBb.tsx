import { Checkbox } from "primereact/checkbox";

interface CheckBoxBbProps {
	onChange: (e: any) => void;
	label: string;
	id: string;
	name?: string;
	checked: boolean;
	className?: string;
	fullwidth?: boolean;
	error?: boolean;
	errorHelperText?: string;
}

const CheckBoxBb = (props: CheckBoxBbProps) => {
	const fullwidthClass = props.fullwidth ? "w-full " : "";
	let className = props.className ?? "";
	className += props.error ? " p-invalid" : "";
	className += props.error && props.errorHelperText ? " mb-2" : "";

	return (
		<div className={fullwidthClass + className + " flex align-items-start"}>
			<Checkbox
				id={props.id}
				name={props.name}
				className={className}
				onChange={props.onChange}
				checked={props.checked}
			/>
			<label htmlFor={props.id} className="ml-2">
				{props.label}
			</label>
		</div>
	);
};

export default CheckBoxBb;
