package dm.categorizer.main;

import java.io.File;

import dm.categorizer.algorithm.Categorizer;
import dm.categorizer.algorithm.NaiveBayesCategorizer;
import dm.categorizer.data.Instances;
import dm.categorizer.evaluate.Evaluator;
import dm.categorizer.io.DatasetReader;
import dm.categorizer.util.PrintUtil;

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
		int space1 = 10, space2 = 3;
		System.out.println("\n===================Summary===================\n");
		System.out.println("Total Instances:\t" + eval.getTotalNum());
		System.out.println("Correctly Classified Instances:\t"
				+ eval.getCorrectlyClassifiedNum());
		System.out.println("InCorrectly Classified Instances:\t"
				+ eval.getIncorrectlyClassifiedNum());
		System.out.println("Accuracy:\t" + eval.getAccuracy());

		System.out.println("\n==========Detailed Accuracy By Class=========\n");
		PrintUtil.print("Precision", space1);
		PrintUtil.print("Recall", space1);
		PrintUtil.print("F-Measure", space1);
		System.out.println("Class");
		double[] precision = eval.getPrecision();
		double[] recall = eval.getRecall();
		double[] fmeasure = eval.getFMeasure();
		for (int i = 0; i < testset.getClazzCount(); i++) {
			PrintUtil.print(precision[i], 5, space1);
			PrintUtil.print(recall[i], 5, space1);
			PrintUtil.print(fmeasure[i], 5, space1);
			System.out.println(testset.getClazzByKey(i));
		}

		System.out.println("\n===============Confusion Matrix==============\n");
		for (int i = 0; i < testset.getClazzCount(); i++) {
			PrintUtil.print("" + (char) ('a' + i), space2);
		}
		System.out.println("<-- classified as");
		int no = 0;
		for (int[] line : eval.getConfusionMatrix()) {
			for (int d : line) {
				PrintUtil.print(d, space2);
			}
			PrintUtil.print("|", 2);
			System.out.println("" + ((char) ('a' + no)) + " = "
					+ testset.getClazzByKey(no));
			no++;
		}
	}

	private static void printUsage() {
		System.out
				.println("Usage: java Categorize {path of train set} {path of test set}");
	}
}
