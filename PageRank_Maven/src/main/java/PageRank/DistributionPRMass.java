package PageRank;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DistributionPRMass {
	public class GraphMapper extends Mapper<Object, Text, Text, Text> {
		private double missingMass = 0.0;
		private int nodeCnt = 0;

		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			Configuration conf = context.getConfiguration();

			missingMass = (double) conf.getFloat("MissingMass", 0.0f);// 该值等于1-totalMass
			nodeCnt = conf.getInt("NodeCount", 0);
		}

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			PageRankNode currentNode = PageRankNode.InstanceFormString(value.toString().trim());
			currentNode.setOldPR(currentNode.getNewPR());

			double p = currentNode.getNewPR();
			double pnew = (1 - PageRankJob.d) / (double) (nodeCnt - 1)
					+ PageRankJob.d * missingMass / (double) (nodeCnt - 1);
			// double pnew=missingMass/(double)(nodeCnt-1);
			currentNode.setNewPR(p + pnew);
			context.write(new Text(currentNode.getId()), new Text(PageRankNode.toStringWithOutID(currentNode)));
		}

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {

		}
	}
}
