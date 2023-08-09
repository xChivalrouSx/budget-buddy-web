import { Button } from "primereact/button";

interface ButtonBbProps {
	onClick?: (event: any) => void;
	type?: "button" | "submit" | "reset";
	label?: string;
	id?: string;
	name?: string;
	className?: string;
	fullwidth?: boolean;
	icon?: string;
	useText?: boolean;
}

const ButtonBb = (props: ButtonBbProps) => {
	const useText = props.useText ?? true;
	const fullwidthClass = props.fullwidth ? "w-full " : "";
	const className = props.className ?? "";
	return (
		<Button
			id={props.id}
			name={props.name}
			type={props.type}
			label={props.label}
			className={fullwidthClass + className}
			onClick={props.onClick}
			icon={props.icon}
			text={!useText}
		/>
	);
};

export default ButtonBb;
