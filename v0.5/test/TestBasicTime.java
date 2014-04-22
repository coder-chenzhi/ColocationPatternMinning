package test;


class TestAutoBoxTime{
	public static void trial(){
		int length = (int)Math.pow(10, 7);
		Integer[] arr = new Integer[length];
		long start = System.currentTimeMillis();
		for (int i = 0; i < length; i++) {
			arr[i] = new Integer(0);
		}
		System.out.println(System.currentTimeMillis() - start);
	}
}

class TestPrintStringTime{
	public static void main(String[] args){
		int length = 100000;
		StringBuilder str = new StringBuilder();
		
		for(int i = 0; i < length; i++){
			str.append('a');
		}
		long start = System.currentTimeMillis(); 
		System.out.println(str);
		System.out.println("PrintTime: " + (System.currentTimeMillis() - start));
	}
}

class TestBasicTime {
	public static void main(String[] args){
		TestAutoBoxTime.trial();
	}
}
