package test;
import java.util.*;

import join_based.nogrid.*;

/**
 * 
 * @author Daniel
 * ����ͨ��һ����ʱ�����Բ�ͬ�Ĳ���NEW��������ټ��������У���ӡ�����ж��Ƿ�ʼ����һ������
 */

public class TestTemp {
	public static void main(String[] argc){
		Integer[] value;
		ArrayList<Integer[]> list = new ArrayList<Integer[]>();
		for(int i = 0; i < 5; i++){
			value = Randoms.createGaussianInteg(i+1, 10, 2);
			list.add(value);
		}
		for(int i = 0; i < 5; i++){
			Integer[] temp = list.get(i);
			for(int j = 0; j < temp.length; j++)
				System.out.print(temp[i]+" ");
			System.out.println();
		}
	}
}
