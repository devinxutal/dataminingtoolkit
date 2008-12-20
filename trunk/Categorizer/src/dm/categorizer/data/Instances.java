package dm.categorizer.data;

import java.util.List;
import java.util.Set;

public class Instances {
	private List<Instance> instances;
	private Set<Object> clazzTypes;
	private List<Set<Integer>> attrValues;

	public Instances() {

	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	public Set<Object> getClazzTypes() {
		return clazzTypes;
	}

	public void setClazzTypes(Set<Object> clazzTypes) {
		this.clazzTypes = clazzTypes;
	}

	public List<Set<Integer>> getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(List<Set<Integer>> attrValues) {
		this.attrValues = attrValues;
	}

	public int getAttrCount() {
		if (attrValues != null) {
			return attrValues.size();
		} else {
			return 0;
		}
	}
}
