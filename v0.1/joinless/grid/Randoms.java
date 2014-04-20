package joinless.grid;
import java.util.HashSet;
import java.util.Random;

/**
 * @author Daniel Grant
 * ����ʵ���п����õ����������
 */


public class Randoms {
	
		/**
		 * ����[low...up]��Χ�ڵ�num�����ظ������
		 * @param low �½�
		 * @param up �Ͻ�
		 * @param num ������ĸ���
		 * @return ����[low...up]��Χ�ڵ�num�����ظ����������������HashSet��
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
