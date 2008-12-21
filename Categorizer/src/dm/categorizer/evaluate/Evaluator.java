package dm.categorizer.evaluate;

import dm.categorizer.data.Instance;
import dm.categorizer.data.Instances;

public class Evaluator {
	private Instances instances;

	private boolean evaluated = false;

	private int[][] confusionMatrix;
	private int total;
	private int correct;
	private int incorrect;

	private double[] precision;

	private double[] recall;

	private double[] fmesure;

	private double[] tprate;

	private double[] fprate;;

	public Evaluator(Instances result) {
		this.instances = result;
	}

	public int getTotalNum() {
		check();
		return total;
	}

	public int getCorrectlyClassifiedNum() {
		check();
		return correct;
	}

	public int getIncorrectlyClassifiedNum() {
		check();
		return incorrect;
	}

	public int getUnclassifiedNum() {
		check();
		return total - correct - incorrect;
	}

	public double getAccuracy() {
		check();
		return correct / (double) total;
	}

	public int[][] getConfusionMatrix() {
		check();
		return this.confusionMatrix;
	}

	public double[] getPrecision() {
		check();
		return this.precision;
	}

	public double[] getRecall() {
		check();
		return this.recall;
	}

	public double[] getFMeasure() {
		check();
		return this.fmesure;
	}

	public double[] getTPRate() {
		check();
		return this.tprate;
	}

	public double[] getFPRate() {
		check();
		return this.fprate;
	}

	private void check() {
		if (!evaluated) {
			evaluate();
			evaluated = true;
		}
	}

	private void evaluate() {
		calculateConfusionMatrix();
		calculateOthers();
	}

	private void calculateOthers() {
		precision = new double[instances.getClazzCount()];
		recall = new double[instances.getClazzCount()];
		// a | b
		// ---|---
		// c | d
		for (int i = 0; i < instances.getClazzCount(); i++) {
			int a = confusionMatrix[i][i];
			int b = 0, c = 0;
			for (int j = 0; j < instances.getClazzCount(); j++) {
				b += confusionMatrix[i][j];
				c += confusionMatrix[j][i];
			}
			b -= a;
			c -= a;
			precision[i] = a / (double) (a + c);
			recall[i] = a / (double) (a + b);
		}
	}

	private void calculateConfusionMatrix() {
		incorrect = correct = 0;
		confusionMatrix = new int[instances.getClazzCount()][instances
				.getClazzCount()];
		total = instances.getInstances().size();
		for (Instance i : instances.getInstances()) {
			if (i.clazz >= 0) {
				if (i.clazz == i.target)
					correct++;
				else
					incorrect++;

				confusionMatrix[i.target][i.clazz]++;
			}
		}
	}
}
