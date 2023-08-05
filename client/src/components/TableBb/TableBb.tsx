import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { TableBbColumn } from "./TableBbObjects";

interface TableBbProps {
	columns: TableBbColumn[];
	data: any[];
	clasName?: string;
}

const TableBb = (props: TableBbProps) => {
	return (
		<div className="card">
			<DataTable value={props.data} className={props.clasName}>
				{props.columns.map((col) => (
					<Column
						key={col.dataField}
						field={col.dataField}
						header={col.header}
						className={col.dataClassName}
						headerClassName={col.headerClassName}
					/>
				))}
			</DataTable>
		</div>
	);
};

export default TableBb;
