package util;

import java.util.*;

public class calSet {
	Set<Integer> ASet = new HashSet<Integer>();
	Set<Integer> BSet = new HashSet<Integer>();

	public Set<Integer> remove(Set<Integer> ASet, Set<Integer> BSet) {
		for (int attribute : BSet) {
			if (ASet.contains(attribute))
				ASet.remove(attribute);
		}
		return ASet;
	}

	public boolean matching(Set<Integer> ASet, Set<Integer> BSet) {
		if (ASet.size() == 1) {
			return true;
		} else {
			Integer[] Aattributes = ASet.toArray(new Integer[0]);
			Integer[] Battributes = BSet.toArray(new Integer[0]);
			for (int i = 0; i < Aattributes.length - 2; i++)
				for (int j = i + 1; j < Aattributes.length - 1; j++) {
					if (i > j) {
						int a = 0;
						a = Aattributes[i];
						Aattributes[i] = Aattributes[j];
						Aattributes[j] = a;
					}
				}
			for (int i = 0; i < Battributes.length - 2; i++)
				for (int j = i + 1; j < Battributes.length - 1; j++) {
					if (i > j) {
						int a = 0;
						a = Battributes[i];
						Battributes[i] = Battributes[j];
						Battributes[j] = a;
					}
				}

			int k = 0;
			while (k < Aattributes.length - 1
					&& Aattributes[k] == Battributes[k])
				k++;
			if (k == Aattributes.length - 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	public Integer getLastValue(Set<Integer> ASet) {
		Integer[] Aattributes = ASet.toArray(new Integer[0]);
		for (int i = 0; i < Aattributes.length - 2; i++)
			for (int j = i + 1; j < Aattributes.length - 1; j++) {
				if (i > j) {
					int a = 0;
					a = Aattributes[i];
					Aattributes[i] = Aattributes[j];
					Aattributes[j] = a;
				}
			}
		return Aattributes[Aattributes.length - 1];
	}
}
