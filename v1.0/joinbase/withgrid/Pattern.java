package joinbase.withgrid;

import java.util.*;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class Pattern{
	/**
	 * the length of pattern, e.g. the length of Pattern [1,2,3] is 3.
	 */
	final private int length;
	
	/**
	 * the prevalence of pattern
	 */
	private double prevalence = 0.0;
	
	/**
	 * the features contained by pattern
	 */
	private TreeSet<Integer> value = new TreeSet<Integer>();
	
	public Pattern(int[] num) {
		this.length = num.length;
		for (int i = 0; i < num.length; i++){
			this.value.add(num[i]);
		}
	}
	
	public Pattern(Integer[] num) {
		this.length = num.length;
		for (int i = 0; i < num.length; i++){
			// pay attention, there is adding the reference to pattern
			// it means you can add and reomve, but never reassign any variable
			this.value.add(num[i]); 
		}
	}
	
	public Pattern(Collection<Integer> c) {
		this.length = c.size();
		Iterator<Integer> it = c.iterator();
		while(it.hasNext()) {
			this.value.add(it.next());
		}
	}
	
	public Pattern(Integer a, Integer b) {
		this.length = 2;
		this.value.add(a);
		this.value.add(b);
	}
	
	public Pattern(Integer[] prefix, Integer last) {
		this.length = prefix.length + 1;
		for (int i = 0; i < prefix.length; i++){
			// pay attention, there is adding the reference to pattern
			// it means you can add and reomve, but never reassign any variable
			this.value.add(prefix[i]); 
		}
		this.value.add(last);
	}
	
	/**
	 * constructs a pattern with two other patterns whose 
	 * the pa.equalsExceptLast(pb) is true.
	 * @param pa pattern A
	 * @param pb pattern B
	 */
	public Pattern(Pattern pa, Pattern pb) {
		this.length = pa.length() + 1;
		this.value.addAll(pa.getValue());
		this.value.add(pb.getLast());
	}
	
	private TreeSet<Integer> getValue() {
		return this.value;
	}
	
	public int length() {
		return this.length;
	}
	
	public double getPrevalence() {
		return this.prevalence;
	}
	
	public Integer getLast() {
		return this.value.last();
	}
	
	public void setPrevalence(double prevalence) {
		this.prevalence = prevalence;
	}
	
	public boolean equalsExceptLast(Pattern other) {

		Integer[] fv1 = this.toArray();
		Integer[] fv2 = other.toArray();
		//System.out.println(this + " " + f);
		
		try{
			if (fv1.length != fv2.length)
				throw new Exception();
		} catch(Exception e) {
			System.out.println("Different Size！");
			e.printStackTrace();
		}
		
		if (fv1[fv1.length-1].equals(fv2[fv2.length-1])){
			//System.out.println("false");
			return false;
		}
		for(int i = 0; i < fv1.length-1; i++){
			if(!fv1[i].equals(fv2[i])){
				//System.out.println("false");
				return false;
			}
		}
		//System.out.println("true");
		return true;
	}
	
	public Integer[] toArray() {
		return this.value.toArray(new Integer[0]);
	}
	
	public String toString(){
		String str="Patterns[ ";
		Iterator<Integer> it = this.value.iterator();
		while (it.hasNext()){
			Integer in = it.next();
			str+=((int) in+" ");
		}
		str+="]";
		return str;
	}
	
	public boolean equals(Object temp){
		Integer[] tempArray =((Pattern) temp).value.toArray(new Integer[0]);	
		Integer[] array = this.value.toArray(new Integer[0]);
		
		if(array.length != tempArray.length)
			return false;
		else 
			for (int i = 0; i < array.length; i++){
				
				//注意强制转换，如果不转换，比较的将是Integer类型的引用
				if ((int) array[i] != (int) tempArray[i])
					return false;
			}
		return true;
	}
	
	public int hashCode(){
		Integer[] array = this.value.toArray(new Integer[0]);
		int result = 17;
		for (int i = 0; i < array.length; i++)
			result = 37 * result + array[i];
		return result;
	}
	
	public static void main(String[] args) {
		Pattern a = new Pattern(new Integer[]{1,4,3,2});
		Pattern b = new Pattern(new Integer[]{1,2,3,5}); 
		Pattern c = new Pattern(a, b);
		System.out.println(c);
	}
	
}

class PatternBinding extends TupleBinding<Pattern> {

	public void objectToEntry(Pattern pattern, TupleOutput to) {
		int size = pattern.length();

		to.writeInt(size);
		Integer[] value = pattern.toArray();
		for(int i = 0; i < size; i++) {
			to.writeInt(value[i]);
		}
	}

	public Pattern entryToObject(TupleInput ti) {
		int size = ti.readInt();
		int[] value = new int[size];
		for(int i = 0; i < size; i++) {
			value[i] = ti.readInt();
		}
		Pattern pattern = new Pattern(value);
		return pattern;
	}
}
