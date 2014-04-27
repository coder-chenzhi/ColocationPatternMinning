package joinbase.withgrid.test;

import joinbase.withgrid.Pattern;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;


public class PatternBinding extends TupleBinding<Pattern> {

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
