package joinbase.withgrid;

public class ResizingIntArray {

	private int array[];
	public ResizingIntArray() { 
		array = new int[0];
	}
	
	public void set(int index, int value) {
		if (index >= array.length)
			resize(2*(index + 1));
		array[index] = value;
	}
	
	public int get(int index) {
		assert index > array.length;
		return array[index];
	}
	
	/**
	 * 
	 * @return 数组的长度，注意不是大小
	 */
	public int length() {
		return array.length;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[");
		for (int i = 0; i < array.length; i++) {
			str.append(array[i]);
			if (i != array.length - 1)
				str.append(", ");
		}
		str.append("]");
		return str.toString();
	}
	
	private void resize(int capacity)
	{
		int[] copy = new int[capacity];
		for (int i = 0; i < array.length; i++)
			copy[i] = array[i];
		for (int i = array.length; i < capacity; i++)
			copy[i] = 0;
		array = copy;
	}
	
	public static void main(String[] args) {
		ResizingIntArray tmp = new ResizingIntArray();
		tmp.set(5, 6);
		tmp.set(1, 2);
		System.out.println(tmp);
	}
}
