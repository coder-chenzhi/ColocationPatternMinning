package test;

public class TestSringMem {
	public static void main(String[] argc) {
		int testSize = 10000000;
		for(int i = 0; i < testSize; i++) {
			String aKey = "" + i;
			String aData = "" + i;
		}
		
		int MEGABYTES = 1024 * 1024;
		long freeMemory = Runtime.getRuntime().freeMemory() / MEGABYTES;
		long totalMemory = Runtime.getRuntime().totalMemory() / MEGABYTES;
		System.out.println("Reassign " + testSize + " String with "+
									(totalMemory - freeMemory) + "MB.");
	}
}
