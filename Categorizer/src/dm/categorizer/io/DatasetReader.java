package dm.categorizer.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import dm.categorizer.data.Instance;
import dm.categorizer.data.Instances;

public class DatasetReader {
	public DatasetReader() {

	}

	public Instances getInstances(String path) {
		BufferedReader in = null;
		if ((in = getInputer(path)) == null) {
			return null;
		}
		Instances instances = new Instances();
		setClazzTypes(instances, in);
		setAttrValues(instances, in);
		setInstances(instances, in);
		return instances;
	}

	private BufferedReader getInputer(String path) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			return in;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Read out all the instances from data file and add them to Instances
	 * Object.
	 * 
	 * @param instances
	 * @param in
	 */
	private void setInstances(Instances instances, BufferedReader in) {
		String line = null;

		try {
			// locate @data
			while ((line = in.readLine()) != null
					&& (!line.trim().equals("@data")))
				;
			List<Instance> list = new LinkedList<Instance>();
			Instance ins;
			// start reading instances;
			while ((line = in.readLine()) != null) {
				if ((ins = parseInstance(line.trim())) != null) {
					list.add(ins);
				}
			}
			instances.setInstances(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Instance parseInstance(String line) {
		String[] attributes = line.split(",");
		if (attributes.length != 36)
			return null;
		Instance ins = new Instance();
		ins.target = attributes[0];
		int[] attr = new int[attributes.length - 1];
		for (int i = 0; i < attr.length; i++) {
			String at = attributes[i + 1];
			if (at.trim().equals("?")) {
				attr[i] = -1;
				continue;
			}
			try {
				attr[i] = Integer.parseInt(at);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return null;
			}
		}
		ins.attributes = attr;
		return ins;
	}

	private void setClazzTypes(Instances instances, BufferedReader in) {
		String line = null;
		try {
			// locate the first @attribute
			while ((line = in.readLine()) != null
					&& (!line.trim().startsWith("@attribute")))
				;
			// find the substring surrounded by {}
			line = line.substring(line.indexOf('{') + 1, line.indexOf('}'));
			Set<Object> clazzTypes = new HashSet<Object>();
			for (String type : line.split(",")) {
				String tp = type.trim();
				if (tp.length() > 0) {
					clazzTypes.add(tp);
				}
			}
			instances.setClazzTypes(clazzTypes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setAttrValues(Instances instances, BufferedReader in) {
		String line = null;
		try {
			List<Set<Integer>> list = new LinkedList<Set<Integer>>();
			while ((line = in.readLine()) != null) {
				if (line.trim().length() == 0)
					break;
				// find the substring surrounded by {}
				line = line.substring(line.indexOf('{') + 1, line.indexOf('}'));
				Set<Integer> attrValues = new HashSet<Integer>();
				for (String type : line.split(",")) {
					attrValues.add(Integer.parseInt(type.trim()));
				}
				list.add(attrValues);
			}
			instances.setAttrValues(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
