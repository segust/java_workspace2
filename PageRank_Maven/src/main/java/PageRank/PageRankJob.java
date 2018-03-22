package PageRank;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class PageRankJob {
	public static final double d = 0.85;
	private static final double nodecount = 10;
	private static final double threshold = 0.01;// 收敛邻接点

	public static enum MidNodes {
		// 记录已经收敛的个数
		Map, Reduce
	};

	public static class PageRankMaper extends Mapper<Object, Text, Text, Text> {
		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			PageRankNode node = PageRankNode.InstanceFormString(value.toString());
			node.setOldPR(node.getNewPR());
			context.write(new Text(node.getId()), new Text(PageRankNode.toStringWithOutID(node)));

			for (String str : node.getDestNodes()) {
				String outPR = new Double(node.getNewPR() / (double) node.getNumDest()).toString();
				context.write(new Text(str), new Text(outPR));
			}
		}
	}

	public static class PageRankJobReducer extends Reducer<Text, Text, Text, Text> {
		private double missMass = Double.NEGATIVE_INFINITY;

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			PageRankNode currentNode = new PageRankNode(key.toString());
			double inPR = 0.0;

			for (Text val : values) {
				String[] temp = val.toString().trim().split("\\s+");
				if (temp.length == 1) // 此时候只输出一个PR值
				{
					inPR += Double.valueOf(temp[0]);
				} else if (temp.length >= 4) {// 此时输出的是含有邻接点的节点全信息
					currentNode = PageRankNode.InstanceFormString(key.toString() + "\t" + val.toString());
				} else if (temp.length == 3) { // 此时输出的点没有出度
					context.getCounter("PageRankJobReducer", "errornode").increment(1);
					currentNode = PageRankNode.InstanceFormString(key.toString() + "\t" + val.toString());
				}
			}
			if (currentNode.getNumDest() >= 1) {
				double newPRofD = (1 - PageRankJob.d) / (double) PageRankJob.nodecount + PageRankJob.d * inPR;
				currentNode.setNewPR(newPRofD);
				context.write(new Text(currentNode.getId()), new Text(PageRankNode.toStringWithOutID(currentNode)));
			} else if (currentNode.getNumDest() == 0) {

				missMass = currentNode.getOldPR();// 得到dangling节点的上一次的PR值，传播到下一个分布Pr的job
			}

			double partPR = (currentNode.getNewPR() - currentNode.getOldPR())
					* (currentNode.getNewPR() - currentNode.getOldPR());
			if (partPR <= threshold) {
				context.getCounter(MidNodes.Reduce).increment(1);
			}
		}

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			// 将total记录到文件中
			Configuration conf = context.getConfiguration();
			String path = conf.get("PageRankMassPath");// 注意此处的path路径设置------------------

			if (missMass == Double.NEGATIVE_INFINITY) {
				return;
			}
			FileSystem fs = FileSystem.get(context.getConfiguration());
			FSDataOutputStream out = fs.create(new Path(path + "/" + "missMass"), false);
			out.writeDouble(missMass);
			out.close();
		}
	}
}
