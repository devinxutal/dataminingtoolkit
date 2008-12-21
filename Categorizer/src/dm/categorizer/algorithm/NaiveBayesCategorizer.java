package dm.categorizer.algorithm;

import java.util.HashMap;
import java.util.Map;

import dm.categorizer.data.Instance;
import dm.categorizer.data.Instances;

public class NaiveBayesCategorizer implements Categorizer {
	double[] p_clazz;

	Map[][] p_attribute; // per class per possible value

	@Override
	public void categorize(Instances testset) {
		for (Instance i : testset.getInstances()) {
			int clazz_max = 0;
			double p_max = 0;
			for (int clazz = 0; clazz < testset.getClazzCount(); clazz++) {
				double p = calculate_p(i, clazz);
				if (p_max < p) {
					p_max = p;
					clazz_max = clazz;
				}
			}
			i.clazz = clazz_max;
		}
	}

	private double calculate_p(Instance ins, int clazz) {
		double p = p_clazz[clazz];
		for (int i = 0; i < ins.attributes.length; i++) {
			int value = ins.attributes[i];
			if (value < 0) {
				value = 0;
			}
			p = p*((Map<Integer, Double>) (p_attribute[clazz][i])).get(value);
		}
		return p;
	}

	@Override
	public void train(Instances trainset) {
		int total = 0;
		// initialize
		p_clazz = new double[trainset.getClazzCount()];
		p_attribute = new Map[trainset.getClazzCount()][trainset.getAttrCount()];
		int[] instances_per_class = new int[trainset.getClazzCount()];
		Map[][] occurance_per_value = new Map[trainset.getClazzCount()][trainset
				.getAttrCount()];
		for (int i = 0; i < trainset.getClazzCount(); i++) {
			for (int j = 0; j < trainset.getAttrCount(); j++) {
				occurance_per_value[i][j] = new HashMap<Integer, Double>();
				p_attribute[i][j] = new HashMap<Integer, Double>();
				for (Integer value : trainset.getAttrValues().get(j)) {
					occurance_per_value[i][j].put(value, 0.0);
				}
			}
		}
		// start counting
		int instance_count = 0;
		for (Instance i : trainset.getInstances()) {
			System.out.println("processing instance " + instance_count++);
			instances_per_class[i.target]++;
			total++;
			for (int a = 0; a < trainset.getAttrCount(); a++) {

				if (i.attributes[a] >= 0) {
					double occurance = ((Map<Integer, Double>) (occurance_per_value[i.target][a]))
							.get(i.attributes[a]);
					occurance += 1;
					((Map<Integer, Double>) (occurance_per_value[i.target][a]))
							.put(i.attributes[a], occurance);
				} else {
					int valueCount = trainset.getAttrValues().get(a).size();
					double weight = 1 / (double) valueCount;
					Map<Integer, Double> map = ((Map<Integer, Double>) (occurance_per_value[i.target][a]));
					for (Integer val : map.keySet()) {
						double occurance = map.get(val);
						map.put(val, occurance + weight);
					}
				}
			}
		}
		// calculate p
		double pp = 0.005;
		double m = 0.001;
		for (int i = 0; i < trainset.getClazzCount(); i++) {
			p_clazz[i] = instances_per_class[i] / (double) total;
			for (int j = 0; j < trainset.getAttrCount(); j++) {
				for (Integer val : trainset.getAttrValues().get(j)) {
					double occurance = ((Map<Integer, Double>) (occurance_per_value[i][j]))
							.get(val);
					double total_instance = (double) instances_per_class[i];
					double p = (occurance + m * pp) / (total_instance + m);
					p_attribute[i][j].put(val, p);
				}
			}
		}
		print_p();
	}

	private void print_p() {
		System.err.println("============p class===========");
		for (int i = 0; i < p_clazz.length; i++) {
			System.err.println("P(clazz[" + i + "]) = " + p_clazz[i]);
		}
		for (int i = 0; i < p_clazz.length; i++) {
			for (int j = 0; j < p_attribute[0].length; j++) {
				for (Map.Entry<Integer, Double> ent : ((Map<Integer, Double>) p_attribute[i][j])
						.entrySet()) {
					System.err.println("P(A_" + j + "_" + ent.getKey()
							+ "|class[" + i + "]) = " + ent.getValue());
				}
			}
		}
	}
}
