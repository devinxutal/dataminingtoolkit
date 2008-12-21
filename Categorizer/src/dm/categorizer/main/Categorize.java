package dm.categorizer.main;

import java.io.File;

import dm.categorizer.algorithm.Categorizer;
import dm.categorizer.algorithm.NaiveBayesCategorizer;
import dm.categorizer.data.Instances;
import dm.categorizer.evaluate.Evaluator;
import dm.categorizer.io.DatasetReader;

public class Categorize {
	public static void main(String args[]) {
		if (args.length != 2) {
			printUsage();
			System.exit(0);
		}
		File trainFile = new File(args[0]);
		File testFile = new File(args[1]);
		if (!(trainFile.exists()) || trainFile.isDirectory()) {
			System.out.println("Error occured: train file dosen't exist.");
			System.exit(0);
		}
		if (!(testFile.exists()) || trainFile.isDirectory()) {
			System.out.println("Error occured: test file dosen't exist.");
			System.exit(0);
		}
		process(trainFile, testFile);
	}

	private static void process(File train, File test) {
		Categorizer categorizer = new NaiveBayesCategorizer();
		DatasetReader reader = new DatasetReader();
		Instances trainset = reader.getInstances(train);
		Instances testset = reader.getInstances(test);

		System.out.println("Start training...");
		categorizer.train(trainset);
		System.out.println("training complete.");
		System.out.println("Start categorizing...");
		categorizer.categorize(testset);
		System.out.println("categorization complete.");
		evaluate(testset);
	}

	private static void evaluate(Instances testset) {
		Evaluator eval = new Evaluator(testset);
		System.out.println("===============Confusion Matrix==============");
		for (int[] line : eval.getConfusionMatrix()) {
			for (int d : line) {
				System.out.print(d + "\t");
			}
			System.out.println("");
		}
	}

	private static void printUsage() {
		System.out
				.println("Usage: java Categorize {path of train set} {path of test set}");
	}
}
