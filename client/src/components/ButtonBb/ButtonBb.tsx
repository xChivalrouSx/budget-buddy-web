import { Button } from "primereact/button";

interface ButtonBbProps {
	onClick?: () => void;
	type?: "button" | "submit" | "reset";
	label?: string;
	id?: string;
	name?: string;
	className?: string;
	fullwidth?: boolean;
}

const ButtonBb = (props: ButtonBbProps) => {
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
		/>
	);
};

export default ButtonBb;
