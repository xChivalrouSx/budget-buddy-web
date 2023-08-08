import { TabPanel, TabView } from "primereact/tabview";
import { TabViewBbElement } from "./TabViewBbObjects";

interface TabViewBbProps {
	content: TabViewBbElement[];
}

const TabViewBb = (props: TabViewBbProps) => {
	return (
		<TabView>
			{props.content.map((content) => {
				return (
					<TabPanel
						key={"tab-panel-" + content.header}
						header={content.header}
					>
						{content.content}
					</TabPanel>
				);
			})}
		</TabView>
	);
};

export default TabViewBb;
