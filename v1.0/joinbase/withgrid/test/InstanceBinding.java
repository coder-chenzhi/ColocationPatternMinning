package joinbase.withgrid.test;

import java.util.Iterator;
import java.util.TreeSet;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

class Item implements Comparable<Item> {
	private int value[];
	
	public Item(int length) {
		value = new int[length];
	}
	
	public Item(int[] value) {
		this.value = value;
	}
	
	public int get(int index) {
		return this.value[index];
	}
	
	public int size() {
		return this.value.length;
	}
	
	
	@Override
	public int compareTo(Item it) {
		// TODO Auto-generated method stub
		assert this.size() != it.size();
		for(int i = 0; i < this.size(); i++) {
			if (this.get(i) != it.get(i)) {
				return this.get(i) - it.get(i);
			}
		}
		return 0;
	}
	
	public int[] toArray() {
		return this.value;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Item:[");
		for (int i = 0; i < this.size(); i++) {
			str.append(this.get(i));
			if (i != this.size() - 1) {
				str.append(", ");
			}
		}
		str.append("]");
		return str.toString();
	}
	
	
}

class Instance implements Iterable<Item>{
	/**
	 * 每个实例的长度
	 */
	final private int length;
	/**
	 * 包含的实例的数目
	 */
	private int size = 0;
	
	private TreeSet<Item> value = new TreeSet<Item>();
	
	public Instance(int length) {
		this.length = length;
	}
	
	public void add(int[] item) {
		assert item.length != this.length;
		this.value.add(new Item(item));
		this.size++;
	}
	
	/*public Item get(int index) {
		assert index < 0 || index >= size;
		return this.value.get(index);
	}*/
	
	public Iterator<Item> iterator() {
		return this.value.iterator();
	}
	/**
	 * 
	 * @return 包含的实例的数目
	 */
	public int size() {
		return this.size;
	}
	
	public int length() {
		return this.length;
	}
	
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Instance: { ");
		Iterator<Item> it = this.iterator();
		while(it.hasNext()) {
			str.append(it.next());
			if(it.hasNext())
				str.append(", ");
		}
		str.append(" }");
		return str.toString();
	}
	
	public static void main(String[] args) {
		Instance ins = new Instance(4);
		ins.add(new int[]{1,2,3,6});
		ins.add(new int[]{2,4,5,9});
		ins.add(new int[]{1,3,5,7});
		System.out.println(ins);
	}
}

class InstanceBinding extends TupleBinding<Instance> {
	public void objectToEntry(Instance ins, TupleOutput to) {
		int length = ins.length();
		int size = ins.size();
		
		to.writeInt(length);
		to.writeInt(size);
		for(Item it : ins) {
			int[] item = it.toArray();
			for(int tmp : item)
				to.writeInt(tmp);
		}
	}
	
	public Instance entryToObject(TupleInput ti) {
		int length = ti.readInt();
		int size = ti.readInt();
		
		Instance ins = new Instance(length);
		for(int i = 0; i < size; i++) {
			int[] item = new int[length];
			for(int j = 0; j < length; j++)
				item[j] = ti.readInt();
			ins.add(item);
		}
		return ins;
	}
	
}
