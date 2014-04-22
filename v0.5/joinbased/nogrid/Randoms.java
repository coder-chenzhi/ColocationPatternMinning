package joinbased.nogrid;
import java.util.Random;

/**
 * @author Daniel Grant
 * ����ʵ���п����õ����������
 */


public class Randoms {
	
		/**
		 * �������num���ڵ�����
		 * @param num ��Ҫ�����Ľڵ����
		 * @return һ����Ϊ2����Ϊnum�Ķ�ά���飬��һ��Ϊ�����꣬�ڶ���Ϊ������
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
		 * ����num���������ҷ��ӷ���Ϊsigma2��ƽ��ֵΪavg����̬�ֲ�����
		 * ������Ϊÿ�������Ľڵ����
		 * @param num ���ɵ�������ĸ���
		 * @param avg ƽ��ֵ
		 * @param sigma2 ����
		 * @return ����һ��������̬�ֲ���int����
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
		 * ����num���������ҷ��ӷ���Ϊsigma2��ƽ��ֵΪavg����̬�ֲ�����
		 * ������Ϊÿ�������Ľڵ����
		 * @param num ���ɵ�������ĸ���
		 * @param avg ƽ��ֵ
		 * @param sigma2 ����
		 * @return ����һ��������̬�ֲ���Integer����		
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
