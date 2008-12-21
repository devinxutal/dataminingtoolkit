package dm.categorizer.algorithm;

import dm.categorizer.data.Instances;

public interface Categorizer {
	void train(Instances trainset);
	void categorize(Instances testset);
}