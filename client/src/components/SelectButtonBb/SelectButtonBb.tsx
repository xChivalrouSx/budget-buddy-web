import { SelectButton } from "primereact/selectbutton";

interface SelectButtonBbProps {
	onChange: (e: any) => void;
	id?: string;
	name?: string;
	className?: string;
	selectOptions?: string[];
	selectedValue?: string;
}

const SelectButtonBb = (props: SelectButtonBbProps) => {
	return (
		<SelectButton
			id={props.id}
			name={props.name}
			onChange={props.onChange}
			className={props.className}
			options={props.selectOptions}
			value={props.selectedValue}
		/>
	);
};

export default SelectButtonBb;
