export interface TableBbColumn {
	header: string;
	dataField: string;
	dataClassName?: string;
	headerClassName?: string;
	hidden?: boolean;
	displayFormat?: (value: any) => string;
	style?: React.CSSProperties;
	sortable?: boolean;
}

export interface TableScrollable {
	scroolableBody?: boolean;
	scroolableHeight?: string;
}
