package util;

import java.util.HashSet;
import java.util.Set;

import model.Example;

public class Entropy {

	Set<Example> examples = new HashSet<Example>();
	int positiveNum = 0;
	int negativeNum = 0;
	double Entropy = 0.0;

	public Entropy(Set<Example> examples) {
		this.examples = examples;
	}

	public double CalEntropy() {
		for (Example e : examples) {

			if (e.getValue(loadData.Category).equals("yes"))
				positiveNum++;
			else
				negativeNum++;

		}
		if (positiveNum == 0 || negativeNum == 0) {
			return 0.0;
		} else {
			Entropy = (positiveNum * Math.log(positiveNum) + negativeNum * Math.log(negativeNum))
					/ (positiveNum + negativeNum) - Math.log(positiveNum + negativeNum);
			return -Entropy;
		}
	}
}
