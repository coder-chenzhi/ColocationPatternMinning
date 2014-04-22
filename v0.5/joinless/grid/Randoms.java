package joinless.grid;
import java.util.HashSet;
import java.util.Random;

/**
 * @author Daniel Grant
 * 产生实验中可能用到的随机数据
 */


public class Randoms {
	
		/**
		 * 产生[low...up]范围内的num个不重复随机数
		 * @param low 下界
		 * @param up 上界
		 * @param num 随机数的个数
		 * @return 返回[low...up]范围内的num个不重复随机数，放在容器HashSet中
		 */
		public static HashSet<Integer> createRandom(int low, int up, int num){
			Random temp = new Random();
			HashSet<Integer> set = new HashSet<Integer>();

			for(int i = 0; i < num; i++){
				int tempint = Math.abs(temp.nextInt())%(up-low+1)+low;
				if(!set.add(new Integer(tempint))){
					i--;
				}
			}
			return set;
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
