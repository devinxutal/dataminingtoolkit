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

	private void check() {
		if (!evaluated) {
			evaluate();
			evaluated = true;
		}
	}

	private void evaluate() {
		calculateConfusionMatrix();
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
