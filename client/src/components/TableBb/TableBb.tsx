import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { TableBbColumn } from "./TableBbObjects";

interface TableBbProps {
	columns: TableBbColumn[];
	data: any[];
	clasName?: string;
	rowClassName?: string;
}

const TableBb = (props: TableBbProps) => {
	const rowClassName = (data: any) =>
		props.rowClassName ? props.rowClassName : "";
	return (
		<DataTable
			value={props.data}
			className={props.clasName}
			rowClassName={rowClassName}
		>
			{props.columns.map((col) => (
				<Column
					key={col.dataField}
					field={col.dataField}
					header={col.header}
					className={col.dataClassName}
					headerClassName={col.headerClassName}
					hidden={col.hidden}
					body={col.displayFormat}
					style={col.style}
					sortable={col.sortable}
				/>
			))}
		</DataTable>
	);
};

export default TableBb;
