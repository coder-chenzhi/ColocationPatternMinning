package basic;
import java.util.Random;

/**
 * @author Daniel Grant
 * a class to create some random data which are useful in our program.
 */


public class Randoms {
	/**
	 * 
	 * @param num is the number of point class we should create.
	 * @return the array of point we create of which the coordinate is random
	 */
		public static int[][] createRand(int num){
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
		 * ����num����������Ϊsigma2��ƽ��ֵΪavg����̬�ֲ����У�����Ϊ2ʱЧ���Ϻ�
		 * @param num is the number of random data we should create
		 * @param avg is the average of random data
		 * @param sigma2 is the variance of random data
		 * @return a array of random data which are under normal distribution
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
		
}
