package dm.categorizer.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Instances {
	private List<Instance> instances;
	private Map<Object, Integer> clazzKeyMap;

	private List<Set<Integer>> attrValues;

	public Instances() {

	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	public Map<Object, Integer> getClazzKeyMap() {
		return clazzKeyMap;
	}

	public void setClazzKeyMap(Map<Object, Integer> clazzKeyMap) {
		this.clazzKeyMap = clazzKeyMap;
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

	public Object getClazzByKey(int key) {
		if (clazzKeyMap != null) {
			for (Map.Entry<Object, Integer> entry : clazzKeyMap.entrySet()) {
				if (entry.getValue().intValue() == key) {
					return entry.getKey();
				}
			}
		}
		return null;
	}

	public int getKeyByClazz(Object clazz) {
		if (clazzKeyMap != null) {
			Integer i = clazzKeyMap.get(clazz);
			if (i != null)
				return i.intValue();
		}
		return -1;
	}

	public int getClazzCount() {
		if (clazzKeyMap == null) {
			return 0;
		} else {
			return clazzKeyMap.size();
		}
	}
}
