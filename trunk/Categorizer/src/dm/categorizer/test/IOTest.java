package dm.categorizer.test;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import dm.categorizer.data.Instance;
import dm.categorizer.data.Instances;
import dm.categorizer.io.DatasetReader;

public class IOTest {
	public static void main(String[] args) {
		DatasetReader reader = new DatasetReader();
		Instances items = reader.getInstances(new File(
				"data/soybean-large-test.arff"));
		System.out.println("Number of class: " + items.getClazzKeyMap().size());
		System.out.println("Number of attributes: "
				+ items.getAttrValues().size());
		System.out.println("===========Classes==========");
		for (Map.Entry<Object, Integer> ent : items.getClazzKeyMap().entrySet()) {
			System.out.println(ent.getKey() + "\t" + ent.getValue());
		}
		System.out.println("===========Attributes==========");
		for (Set<Integer> values : items.getAttrValues()) {
			System.out.print("[");
			for (Integer i : values) {
				System.out.print(i);
			}
			System.out.print("]\n");
		}
		System.out.println("===========Data==========");
		for (Instance i : items.getInstances()) {
			System.out.println(i.target + Arrays.toString(i.attributes));
		}
		System.out.println("Number of Instances: "
				+ items.getInstances().size());
	}
}
