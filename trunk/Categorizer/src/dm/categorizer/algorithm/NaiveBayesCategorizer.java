package dm.categorizer.algorithm;

import dm.categorizer.data.Instance;
import dm.categorizer.data.Instances;

public class NaiveBayesCategorizer implements Categorizer {

	@Override
	public void categorize(Instances testset) {
		for (Instance i : testset.getInstances()) {
			i.clazz = i.target;
		}
	}

	@Override
	public void train(Instances trainset) {
		for (Instance i : trainset.getInstances()) {
			// do nothing
		}
	}

}
