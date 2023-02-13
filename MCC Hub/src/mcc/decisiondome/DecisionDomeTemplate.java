package mcc.decisiondome;

import java.util.List;

import org.bukkit.Location;

public class DecisionDomeTemplate {

	private List<List<Location>> fields;

	public DecisionDomeTemplate(List<List<Location>> fields) {
		this.fields = fields;
	}
	
	public void addField(List<Location> locations) {
		this.fields.add(locations);
	}
	
	public List<List<Location>> getFields() {
		return fields;
	}
}
