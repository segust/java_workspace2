package Start;

import java.util.Set;

import model.Example;
import model.Node;
import util.loadData;

public class Main {
	public static void main(String[] args) {
		loadData loader = new loadData();
		Set<Example> examples = loader.getData();
		Node root = new Node(examples, "¸ù½Úµã");
		root.grow();
	}
}
