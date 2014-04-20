package joinbased.nogrid;

import java.util.TreeSet;

public class BitMap{
	

	    int[] bits = null;
	    
		int length;  
		private final  int[] bitValue = {
				0x80000000,  
				0x40000000,  
				0x20000000,  
				0x10000000,  
				0x08000000,  
				0x04000000,  
				0x02000000,  
				0x01000000,  
				0x00800000,  
				0x00400000,  
				0x00200000,  
				0x00100000,  
				0x00080000,  
				0x00040000,  
				0x00020000,  
				0x00010000,  
				0x00008000,  
				0x00004000,  
				0x00002000,  
				0x00001000,  
				0x00000800,  
				0x00000400,  
				0x00000200,  
				0x00000100,  
				0x00000080,  
				0x00000040,  
				0x00000020,  
				0x00000010,  
				0x00000008,  
				0x00000004,  
				0x00000002,  
				0x00000001  
	         };
		/**
		 * 
		 * @param array 用来构造BitMap的所有数值
		 */
		public BitMap(Integer[] array){
			int max = 0;
			for(int i = 0; i < array.length; i++)
				max = Math.max(max, array[i]);
			this.length = max / 32 + 1;
			bits = new int[this.length];
			for(int i = 0; i < this.length; i++)
				bits[i] = 0;
			for(int i = 0; i < array.length; i++){									
					bits[(array[i]-1)/32] += bitValue[31-(array[i]-1)%32];
				
			}
		}
		/**
		 * 
		 * @param set Integer对象填充的TreeSet
		 */
		public BitMap(TreeSet<Integer> set){
			Integer[] tempInt = set.toArray(new Integer[0]);
			this.length = tempInt[ tempInt.length - 1 ] / 32 + 1;
			bits = new int[this.length];
			for(int i = 0; i < this.length; i++)
				bits[i] = 0;
			for(int i = 0; i < tempInt.length; i++){
				bits[(tempInt[i]-1)/32] += bitValue[31-(tempInt[i]-1)%32];
			}
		}
		
		/**
		 * 
		 * @param size 构造一个大小为size的BitMap
		 * 此size为BitMap中int数组的数目
		 */
		public BitMap(int size){
			this.length = size;
			bits = new int[size];
			for(int i = 0; i < size; i++)
				bits[i] = 0;			
		}
		/**
		 * 比较两个BitMap是否只有一个元素不同
		 * @param m1 待比较的BitMap
		 * @param m2 待比较的BitMap
		 * @return 两个BitMap是否只有一个元素不同
		 */
		public static boolean Compare(BitMap m1, BitMap m2){
			int difnum = 0;
			int max = Math.max(m1.length, m2.length);
			BitMap bit = new BitMap(max);
			for(int i = 0; i < max; i++){
				if(i >= m1.length)
					bit.bits[i] = m2.bits[i];
				else
				if(i >= m2.length)
					bit.bits[i] = m1.bits[i];
				else bit.bits[i] = m1.bits[i]^m2.bits[i];
			}
			for(int i = 0; i < bit.length; i++){
				if(bit.bits[i] == 0) continue;
				else{
					int temp = bit.bits[i];
					while(temp != 0){
						temp &= temp-1;
						difnum++;
						if(difnum > 2) 
							return false;
					}
				}
			}
			if(difnum == 2)
				return true;
			else return false;
		}
		
		public String toString(){
			String str = "";
			for(int i = this.length-1; i >= 0; i--){
				if(i == this.length-1)
					str += Integer.toBinaryString(bits[i]);
				else{
					String temp = Integer.toBinaryString(bits[i]);
					int loop = 32 - temp.length();
					while(loop > 0){
						str += "0";
						loop--;
					}
					str += temp;
				}
			}
			return str;
		}
	
}


