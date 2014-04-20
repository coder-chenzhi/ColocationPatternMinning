package test;
import java.util.*;

import join_based.nogrid.*;

/**
 * 
 * @author Daniel
 * 测试通过一个临时对象，以不同的参数NEW这个对象，再加入容器中，打印容器判断是否始终是一个对象
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
