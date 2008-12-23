package dm.categorizer.ui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import dm.categorizer.algorithm.Categorizer;
import dm.categorizer.algorithm.NaiveBayesCategorizer;
import dm.categorizer.data.Instances;
import dm.categorizer.evaluate.Evaluator;
import dm.categorizer.io.DatasetReader;

public class CategorizerFrame extends JFrame {
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 700;

	private File trainFile;
	private File testFile;

	private JTextArea logArea;
	private JTextField trainFileField;
	private JTextField testFileField;
	private JButton trainFileButton;
	private JButton testFileButton;
	private JButton startButton;
	private JComboBox algorithmComboBox;
	private boolean resultCalculated;
	private JFileChooser chooser;

	private Categorizer categorizers[];

	public CategorizerFrame() {
		super("Categorizer");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		int startX = (Toolkit.getDefaultToolkit().getScreenSize().width - WINDOW_WIDTH) / 2;
		int startY = (Toolkit.getDefaultToolkit().getScreenSize().height - WINDOW_HEIGHT) / 2;
		this.setBounds(startX, startY, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		refresh();
		categorizers = new Categorizer[1];
		categorizers[0] = new NaiveBayesCategorizer();
	}

	private void initComponents() {
		BorderLayout borderLayout = new BorderLayout();
		this.setLayout(borderLayout);
		this.add("East", new JPanel());
		this.add("West", new JPanel());
		this.add("North", new JPanel());
		this.add("South", new JPanel());
		JPanel panel = new JPanel(new BorderLayout());
		this.add("Center", panel);

		JPanel emptyPane = new JPanel(new BorderLayout());

		emptyPane.add("North", new JLabel(
				"Select appropriate train/test set to start your experiment."));
		JPanel filePanel = new JPanel();
		BoxLayout bl = new BoxLayout(filePanel, BoxLayout.Y_AXIS);
		filePanel.setLayout(bl);
		// train file panel
		JPanel trainFilePanel = new JPanel(new BorderLayout());
		BorderLayout l = new BorderLayout();
		l.setHgap(5);
		trainFilePanel.setLayout(l);
		trainFileButton = new JButton("Browse");
		trainFileField = new JTextField("no file selected");
		trainFileField.setEditable(false);
		trainFilePanel.add("West", new JLabel("Train Set:"));
		trainFilePanel.add("Center", trainFileField);
		trainFilePanel.add("East", trainFileButton);
		trainFileButton.addActionListener(new SelectFileAction());
		// test file panel
		JPanel testFilePanel = new JPanel();
		l = new BorderLayout();
		l.setHgap(5);
		testFilePanel.setLayout(l);
		testFileButton = new JButton("Browse");
		testFileField = new JTextField("no file selected");
		testFileField.setEditable(false);
		testFilePanel.add("West", new JLabel("Test Set: "));
		testFilePanel.add("Center", testFileField);
		testFilePanel.add("East", testFileButton);
		testFileButton.addActionListener(new SelectFileAction());
		// start panel
		JPanel startPane = new JPanel();
		startPane.setLayout(new BoxLayout(startPane, BoxLayout.X_AXIS));
		algorithmComboBox = new JComboBox(new String[] { "Naive Bayes" });
		startButton = new JButton("        Start        ");
		startButton.addActionListener(new StartAction());
		startPane.add(new JLabel("Select Algorithm:"));
		startPane.add(algorithmComboBox);
		startPane.add(Box.createHorizontalGlue());
		startPane.add(startButton);
		//
		filePanel.add(Box.createVerticalStrut(10));
		filePanel.add(trainFilePanel);
		filePanel.add(Box.createVerticalStrut(10));
		filePanel.add(testFilePanel);
		filePanel.add(Box.createVerticalStrut(15));
		filePanel.add(startPane);
		filePanel.add(Box.createVerticalStrut(15));

		emptyPane.add("Center", filePanel);
		panel.add("North", emptyPane);

		JPanel resultPane = new JPanel(new BorderLayout());
		JLabel resultLabel = new JLabel("Result:");
		resultPane.add("North", resultLabel);
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setLineWrap(false);
		resultPane.add("Center", new JScrollPane(logArea));
		panel.add("Center", resultPane);
	}

	private void refresh() {
		if (resultCalculated) {
		} else {
			this.logArea.setText("");
		}
	}

	private void evaluate(Instances testset) {
		sb = new StringBuffer();
		Evaluator eval = new Evaluator(testset);
		int space1 = 10, space2 = 3;
		println("\n===================Summary===================\n");
		println("Total Instances:\t" + eval.getTotalNum());
		println("Correctly Classified Instances:\t"
				+ eval.getCorrectlyClassifiedNum());
		println("Incorrectly Classified Instances:\t"
				+ eval.getIncorrectlyClassifiedNum());
		println("Accuracy:\t" + eval.getAccuracy());

		println("\n==========Detailed Accuracy By Class=========\n");
		print("Precision", space1);
		print("Recall", space1);
		print("F-Measure", space1);
		println("Class");
		double[] precision = eval.getPrecision();
		double[] recall = eval.getRecall();
		double[] fmeasure = eval.getFMeasure();
		for (int i = 0; i < testset.getClazzCount(); i++) {
			print(precision[i], 5, space1);
			print(recall[i], 5, space1);
			print(fmeasure[i], 5, space1);
			println(testset.getClazzByKey(i) + "");
		}

		println("\n===============Confusion Matrix==============\n");
		for (int i = 0; i < testset.getClazzCount(); i++) {
			print("" + (char) ('a' + i), space2);
		}
		println("<-- classified as");
		int no = 0;
		for (int[] line : eval.getConfusionMatrix()) {
			for (int d : line) {
				print(d, space2);
			}
			print("|", 2);
			println("" + ((char) ('a' + no)) + " = "
					+ testset.getClazzByKey(no));
			no++;
		}
		logArea.setText(sb.toString());
	}

	class StartAction extends AbstractAction {
		public void actionPerformed(ActionEvent arg0) {
			if (trainFile != null && testFile != null) {
				Categorizer categorizer = categorizers[algorithmComboBox
						.getSelectedIndex()];
				DatasetReader reader = new DatasetReader();
				Instances trainset = reader.getInstances(trainFile);
				Instances testset = reader.getInstances(testFile);

				System.out.println("Start training...");
				categorizer.train(trainset);
				System.out.println("training complete.");
				System.out.println("Start categorizing...");
				categorizer.categorize(testset);
				System.out.println("categorization complete.");
				evaluate(testset);
				resultCalculated = true;
				refresh();
			}
		}
	}

	class SelectFileAction extends AbstractAction {
		public void actionPerformed(ActionEvent arg0) {
			if (chooser == null) {
				chooser = new JFileChooser();
				chooser.setFileFilter(new FileFilter() {

					@Override
					public boolean accept(File arg0) {
						if (arg0.isDirectory()
								|| arg0.getAbsolutePath().endsWith(".arff")) {
							return true;
						}
						return false;
					}

					@Override
					public String getDescription() {
						return "*.arff";
					}
				});
			}
			int returnVal = chooser.showOpenDialog(CategorizerFrame.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (arg0.getSource().equals(trainFileButton)) {
					trainFile = chooser.getSelectedFile();
					trainFileField.setText(trainFile.getAbsolutePath());
				} else {
					testFile = chooser.getSelectedFile();
					testFileField.setText(testFile.getAbsolutePath());
				}
				resultCalculated = false;
			}

		}
	}

	StringBuffer sb = new StringBuffer();

	private void print(String content) {
		sb.append(content);
	}

	private void println(String content) {
		sb.append(content + "\n");
	}

	public void print(String content, int length) {
		length = Math.max(length, 2) - 1;
		int strlen = content.length();
		if (strlen > length) {
			content = content.substring(0, length);
			strlen = length;
		}
		print(content);
		for (int i = 0; i < (length - strlen + 1); i++)
			print(" ");
	}

	public void print(int num, int length) {
		length = Math.max(length, 2) - 1;
		int strlen = lengthOf(num);
		if (strlen > length) {
			for (int i = 0; i < (strlen - length); i++) {
				num = num / 10;
			}
			strlen = length;
		}
		print(num + "");
		for (int i = 0; i < (length - strlen + 1); i++)
			print(" ");
	}

	public void print(double num, int nlength, int flength) {
		flength = Math.max(flength, 2) - 1;
		nlength = Math.min(flength, nlength);
		int intlen = lengthOf((int) num);
		int fraclen = nlength - intlen - 1;
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(fraclen);
		format.setMinimumFractionDigits(fraclen);
		print(format.format(num));
		for (int i = 0; i < (flength - nlength + 1); i++)
			print(" ");
	}

	private int lengthOf(int num) {
		int len = 1;
		while ((num = num / 10) != 0)
			len++;
		return len;
	}
}
