package algorithm;

import java.util.HashMap;
import java.util.Random;

import model.Configuration;
import model.SMOStruct;

/**
 * 完整的SMO算法
 */
public class SMO {

	public SMO() {
	}

	/**
	 * 
	 * @param dataMatIn 训练数据
	 * @param labels 训练集标签
	 * @param sStruct SMO缓存
	 * @return sStruct SMO缓存中的运算结果
	 */
	public SMOStruct run(float[][] dataMatIn, int[] labels, SMOStruct sStruct)// SMO算法过程
	{
		int m = dataMatIn.length;
		float[] alphas = new float[m];
		float b = 0;
		float[][] eCache = new float[m][2];
		for (int i = 0; i < m; i++) {
			alphas[i] = 0;
			eCache[i][0] = 0;
			eCache[i][1] = 0;
		}
		sStruct.setDataMatIn(dataMatIn);
		sStruct.setLabels(labels);
		sStruct.setB(b);
		sStruct.setM(m);
		sStruct.seteCache(eCache);
		sStruct.setAlphas(alphas);
		// 以上完成数据的加载，初始化参数

		int iter = 0;
		boolean isEntireSet = true;// 是否要遍历整个数据集
		int alphaPairsChanged = 0;// 运行之后改变的α对的数量

		// 算法的大循环，循环中有几个参数，iter为循环的最大次数；alphaPairsChanged表示每一次循环中，有没有α对的改变，如果没有改变说明收敛；
		// 由于在算法即将收敛的时候，非支持向量和outliner的α值大多已经确定，即为0或C，它们的值很可能不会再变化了。因此，不必每次都改变所有α的值，
		// 而是在一定情况下，只改变那些0到C之间的α，等于0或者C的不变。
		// entireSet即是表示是否要遍历整个数据集，使用这个参数是为了加快迭代的速度，其唯一不同在于寻找α的方式
		// update函数的作用是在已经寻找到一个αi的情况下，找到最适合的αj更新。
		while ((iter < Configuration.MAX_ITER)
				&& ((alphaPairsChanged > 0) || (isEntireSet))) {
			alphaPairsChanged = 0;// 本次循环改变的α总数，最后需要用这个值来判断是否下次要遍历整个数据集
			if (isEntireSet) {// 遍历整个数据集
				for (int i = 0; i < sStruct.getM(); i++)
					// 优化所有的α
					alphaPairsChanged += update(i, sStruct);
			} else {// 只遍历一部分在0到C之间的α
			// 使用countNoBounds参数来表示0到C之间的α的个数
				int countNoBounds = 0;
				for (int i = 0; i < sStruct.getM(); i++)
					if ((sStruct.getAlphas()[i] > 0)
							&& (sStruct.getAlphas()[i] < Configuration.C))
						// 0<αi<C
						countNoBounds++;
				int[] noBoundAlphas = new int[countNoBounds];
				int x = 0;
				for (int i = 0; i < sStruct.getM(); i++) {
					if ((sStruct.getAlphas()[i] > 0)
							&& (sStruct.getAlphas()[i] < Configuration.C))
						noBoundAlphas[x++] = i;// 将那些满足条件的α的下标存到noBoundAlphas数组中
				}
				
				for (int i = 0; i < countNoBounds; i++) {
					// 把大于0小于C的α取出来优化，并记录下变化了的α总数
					alphaPairsChanged += update(noBoundAlphas[i], sStruct);
				}// 对那些满足条件的α进行处理
			}
			iter++;

			if (isEntireSet) {// 如果这次遍历的所有α，则下次就只遍历0到C之间的
				isEntireSet = false;// 如果这次访问的是整个数据集，下次就只访问非边界数据，这样可以提高速度
			}
			if (alphaPairsChanged == 0) {
				isEntireSet = true;// 如果这次访问的是非边界数据并且没有α对的改变，那么下次就访问整个数据集
			}
		}

		float[] W = calcWs(sStruct.getAlphas(), sStruct.getDataMatIn(),
				sStruct.getLabels());
		sStruct.setW(W);
		return sStruct;

	}

	/**
	 * 预测函数
	 * @param Ws 多个ω组成的向量
	 * @param dataMat 测试数据
	 * @param b 分割超平面截距b
	 * @return float 预测结果
	 */
	public float predictFunction(float[] Ws, float[] dataMat, float b) {
		float predict = 0;
		for (int i = 0; i < Ws.length; i++)
			predict += Ws[i] * dataMat[i];
		predict += b;
		return predict;
	}

	/**
	 * 根据公式，由训练集和α得到w的函数
	 * @param alphas α
	 * @param dataMatIn 输入数据集合
	 * @param labels 输入标签集合
	 * @return w
	 */
	private float[] calcWs(float[] alphas, float[][] dataMatIn, int[] labels) {
		int m = dataMatIn.length;// 行数
		int n = dataMatIn[0].length;// 列数
		float[] w = new float[n];
		for (int i = 0; i < m; i++) {
			if (alphas[i] != 0)
				for (int j = 0; j < n; j++) {
					w[j] += alphas[i] * labels[i] * dataMatIn[i][j];// 由于大部分α的值为0，而非0的α对应支持向量，所以起作用的只有支持向量
				}
		}

		return w;
	}

	/**
	 * 对已经选择好了的i进行更新，这里要用一种启发式的方法选择J，即selectJ函数
	 * @param i 选择好的i
	 * @param oS SMO缓存
	 * @return 如果α对有改变，则返回1，否则返回0，这样便于后面的计数。
	 */
	private int update(int i, SMOStruct smoStruct) {
		float c = Configuration.C;
		float Ei = calcEk(smoStruct, i);
		float L = 0;
		float H = 0;
		float eta = 0;// H，L，eta都是后面要用到的局部变量

		if ((smoStruct.getLabels()[i] * Ei < 0)
				&& (smoStruct.getAlphas()[i] < Configuration.C)
				|| ((smoStruct.getLabels()[i] * Ei > 0) && (smoStruct.getAlphas()[i] > 0))) {
		// 这里是alpha不满足KKT条件的情况，即需要进行优化的情况
		// 引入toler参数是因为计算过程中因为除不尽，无法得到精确相等值，可以这样理解：
		// 原始KKT条件：
		// αi=0 <=> yi*ui>=1
		// 0<αi<C <=> yi*ui=1
		// αi=C <=> yi*ui<=1
		// 即：
		// αi<C <=> yi*ui>=1
		// αi>0 <=> yi*ui<=1
		// 逆命题即:
		// αi<C <=> yi*ui<1
		// αi>0 <=> yi*ui>1
			HashMap<String, Object> jEj = selectJ(i, smoStruct, Ei);// 用一个类把j和Ej都传过来
			int j = (Integer) jEj.get("J");
			float Ej = (Float) jEj.get("Ej");
			float alphaIold = smoStruct.getAlphas()[i];
			float alphaJold = smoStruct.getAlphas()[j];

			if (smoStruct.getLabels()[i] != smoStruct.getLabels()[j])// 确定L和H
			{
				L = Math.max(0, smoStruct.getAlphas()[j] - smoStruct.getAlphas()[i]);
				H = Math.min(c, c + smoStruct.getAlphas()[j] - smoStruct.getAlphas()[i]);
			} else {
				L = Math.max(0, smoStruct.getAlphas()[j] + smoStruct.getAlphas()[i] - c);
				H = Math.min(c, smoStruct.getAlphas()[j] + smoStruct.getAlphas()[i]);
			}

			if (L >= H) {
				// Low >= High 是不会出现的情况，如果出现说明不能更新;
				return 0;
			}

			float xij = kernel(smoStruct.getDataMatIn()[i], smoStruct.getDataMatIn()[j]);
			float xii = kernel(smoStruct.getDataMatIn()[i], smoStruct.getDataMatIn()[i]);
			float xjj = kernel(smoStruct.getDataMatIn()[j], smoStruct.getDataMatIn()[j]);
			eta = 2 * xij - xii - xjj;
			if (eta >= 0) {
				// eta >= 0 是错误情况;
				return 0;
			}
			// 如果程序运行到现在还没有返回false，说明应该对αi和αj进行修改
			smoStruct.getAlphas()[j] -= smoStruct.getLabels()[j] * (Ei - Ej) / eta;
			smoStruct.getAlphas()[j] = chooseAlpha(smoStruct.getAlphas()[j], H, L);
			updateEk(smoStruct, j);// 更新Ej，并将Ej标记为有效

			if (Math.abs(smoStruct.getAlphas()[j] - smoStruct.getAlphas()[i]) < Configuration.TOLER) {
				// 因为这里是需要对有效的α对改变进行计数,如果j改变得不明显,则视为是没有改变
				return 0;// 否则αi也没有必要改变了
			}
			smoStruct.getAlphas()[i] += smoStruct.getLabels()[j] * smoStruct.getLabels()[i]
					* (alphaJold - smoStruct.getAlphas()[j]);
			updateEk(smoStruct, i);

			// 对b的更新
			float b1 = smoStruct.getB() - Ei - smoStruct.getLabels()[i]
					* (smoStruct.getAlphas()[i] - alphaJold)
					* kernel(smoStruct.getDataMatIn()[i], smoStruct.getDataMatIn()[i])
					- smoStruct.getLabels()[j] * (smoStruct.getAlphas()[j] - alphaJold)
					* kernel(smoStruct.getDataMatIn()[i], smoStruct.getDataMatIn()[j]);
			float b2 = smoStruct.getB() - Ej - smoStruct.getLabels()[i]
					* (smoStruct.getAlphas()[i] - alphaIold)
					* kernel(smoStruct.getDataMatIn()[i], smoStruct.getDataMatIn()[j])
					- smoStruct.getLabels()[j] * (smoStruct.getAlphas()[j] - alphaJold)
					* kernel(smoStruct.getDataMatIn()[j], smoStruct.getDataMatIn()[j]);
			if ((smoStruct.getAlphas()[i] > 0) && (smoStruct.getAlphas()[i] < c))
				smoStruct.setB(b1);
			else if ((smoStruct.getAlphas()[j] > 0) && (smoStruct.getAlphas()[j] < c))
				smoStruct.setB(b2);
			else
				smoStruct.setB((float) ((b1 + b2) / 2.0));
			return 1;// 修改成功，改变的α对的计数加1
		} else {
			return 0;
		}
	}

	/**
	 * 计算Ek并存储
	 * @param smoStruct SMO缓存
	 * @param k 选择好的k
	 */
	private void updateEk(SMOStruct smoStruct, int k) {
		float Ek = calcEk(smoStruct, k);
		smoStruct.geteCache()[k][0] = 1;
		smoStruct.geteCache()[k][1] = Ek;
	}

	/**
	 * 计算第k个X的估计值，也就是计算label(wX_k + b)
	 * @param sS SMO缓存
	 * @param k 选择好的k
	 * @return Ek
	 */
	private float calcEk(SMOStruct sS, int k) {
		float Ek = 0;
		float[] absAlphas = new float[sS.getM()];
		for (int i = 0; i < sS.getM(); i++) {
			absAlphas[i] = sS.getAlphas()[i] * sS.getLabels()[i];
		}
		float[] absXk = new float[sS.getM()];
		for (int i = 0; i < sS.getM(); i++) {
			absXk[i] = kernel(sS.getDataMatIn()[i], sS.getDataMatIn()[k]);
		}

		float fXk = 0;
		for (int i = 0; i < sS.getM(); i++) {
			fXk += absAlphas[i] * absXk[i];
		}

		fXk += sS.getB();
		Ek = fXk - sS.getLabels()[k];
		return Ek;
	}

	/**
	 * 核函数，这里用的线性核
	 * @param Xi 向量i
	 * @param Xj 向量j
	 * @return 向量内积
	 */
	private float kernel(float[] Xi, float[] Xj) {
		float result = 0;

		/*
		 * 如果将核函数搞成高斯核，会产生更低的训练正确率，这是因为使用的训练集更适合用线性核函数
		 */
		for (int i = 0; i < Xi.length; i++) {
			result += Xi[i] * Xj[i];
		}

		return result;
	}

	/**
	 * SMO算法中选择最佳的α的启发式方法，选择使Ei-Ej（即步长）最大的J
	 * @param i 当前i
	 * @param smoStruct SMO缓存
	 * @param Ei 当前Ei
	 * @return 返回j和ej对
	 */
	private HashMap<String, Object> selectJ(int i, SMOStruct smoStruct, float Ei) {
		HashMap<String, Object> result = new HashMap<String, Object>();

		int maxK = -1;
		float maxDeltaE = 0;
		float Ej = 0;

		// eCache[0]:1表示有意义，0表示无意义
		// eCache[1]:Ei的值
		smoStruct.geteCache()[i][0] = 1;
		smoStruct.geteCache()[i][1] = Ei;

		int countValidEcacheList = 0;// 记录已经初始化了的e的个数
		for (int l = 0; l < smoStruct.getM(); l++) {
			if (smoStruct.geteCache()[l][0] == 1)
				countValidEcacheList += 1;
		}
		if (countValidEcacheList <= 1) {//如果是第一次选择j:
			int j;
			j = selectJRand(i, smoStruct.getM());
			// 注意：不同数据集对第一个j的敏感程度不同,有的任意取一个就好，有的需要选择特定的j,本数据集从第48个数据开始效果最佳
			Ej = calcEk(smoStruct, j);
			result.put("Ej", Ej);
			result.put("J", j);
			System.out.println("选的第一个j是" + j);
			return result;
		}
		int[] validEcacheList = new int[countValidEcacheList];// 存放那些有用的eCache的下标
		int x = 0;
		for (int l = 0; l < smoStruct.getM(); l++) {
			if (smoStruct.geteCache()[l][0] == 1) {
				validEcacheList[x] = l;
				x++;
			}
		}

		for (int l = 0; l < countValidEcacheList; l++) {
			if (validEcacheList[l] == i) {
				continue;
			}
			float Ek = calcEk(smoStruct, validEcacheList[l]);
			float deltaE = Math.abs(Ei - Ek);
			if (deltaE >= maxDeltaE)// 大于当前最大步长即修改J的值
			{
				maxK = validEcacheList[l];
				maxDeltaE = deltaE;
				Ej = Ek;
			}
		}

		result.put("Ej", Ej);
		result.put("J", maxK);
		return result;
	}

	/**
	 * 随机选择一个小于m且不为i的j
	 * @param i
	 * @param m
	 * @return j
	 */
	private int selectJRand(int i, int m) {
		Random r = new Random();
		int j = i;
		while (j == i)
			j = r.nextInt(m);
		return j;
	}

	/**
	 * 将α锁定在[L, H]中
	 * @param alpha
	 * @param H 最大值
	 * @param L 最小值
	 * @return α_new
	 */
	private float chooseAlpha(float alpha, float H, float L) {
		if (alpha > H)
			alpha = H;
		if (alpha < L)
			alpha = L;
		return alpha;
	}

	/**
	 * 测试函数
	 * @param testSet 测试集数据
	 * @param testLabel 测试集标签
	 * @param Ws 训练好的ω
	 * @param b 分割超平面截距b
	 * @return
	 */
	public void test(float[][] testSet, int[] testLabel, float[] Ws, float b) {
		int[] result = new int[testSet.length];
		for (int i = 0; i < result.length; i++) {
			float r = this.predictFunction(Ws, testSet[i], b);
			if (r * testLabel[i] >= 0)
				result[i] = 1;
			else
				result[i] = -1;
		}

		int count = 0;
		for (int temp : result) {
			if (temp == 1)
				count++;
		}

		System.out.println("Ws:" + Ws[0] + "," + Ws[1] + "\tb:" + b);
		System.out.println("rate:" + (float) count / result.length);
	}
}