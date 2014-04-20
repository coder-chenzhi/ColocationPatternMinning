package joinbased.nogrid;
import java.util.Random;

/**
 * @author Daniel Grant
 * 产生实验中可能用到的随机数据
 */


public class Randoms {
	
		/**
		 * 随机生成num个节点坐标
		 * @param num 所要产生的节点个数
		 * @return 一个行为2，列为num的二维数组，第一行为横坐标，第二行为纵坐标
		 */
		public static int[][] createRandomPoint(int num){
			Random temp = new Random();
			int[][] data = new int[2][];
			data[0] = new int[num];
			data[1] = new int[num];
			for(int i = 0; i < num; i++){
				
				data[0][i] = Math.abs(temp.nextInt()%1000);
				data[1][i] = Math.abs(temp.nextInt()%1000);
				
			}			
			return data;
		}

		/**
		 * 生成num个数，并且服从方差为sigma2，平均值为avg的正态分布序列
		 * 用来作为每种特征的节点个数
		 * @param num 生成的随机数的个数
		 * @param avg 平均值
		 * @param sigma2 方差
		 * @return 返回一个服从正态分布的int数组
		 */
		public static int[] createGaussian(int num, int avg, int sigma2){
			Random temp = new Random();
			int[] array = new int[num];
			for(int i = 0; i < num; i ++){				
				array[i] = (int)(temp.nextGaussian()
								* Math.sqrt(sigma2) + avg);
				if(array[i] <= 0) 
					array[i] = avg;
				
			}			
			return array;
		}
		/**
		 * 生成num个数，并且服从方差为sigma2，平均值为avg的正态分布序列
		 * 用来作为每种特征的节点个数
		 * @param num 生成的随机数的个数
		 * @param avg 平均值
		 * @param sigma2 方差
		 * @return 返回一个服从正态分布的Integer数组		
		 */
		public static Integer[] createGaussianInteg(int num, int avg, int sigma2){
			Random temp = new Random();
			Integer[] array = new Integer[num];
			for(int i = 0; i < num; i ++){				
				array[i] = (int)(temp.nextGaussian()
								* Math.sqrt(sigma2) + avg);
				if(array[i] <= 0) 
					array[i] = avg;
				
			}			
			return array;
		}
		
}
