package objective;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Registry {

	public static class Entry<T extends MazeIterator.MazeCellData<T>> {

		public final Class<T> cls;
		public final Supplier<T> sup;
		public final String name;

		public Entry(String name, Class<T> cls, Supplier<T> sup) {
			this.name = name;
			this.cls = cls;
			this.sup = sup;
			LIST.add(this);
		}

		public double execute(int[][] ans, int x, int y) {
			return new MazeIterator<T>(ans, this).iterate(x, y).getResult();
		}

	}

	public static final List<Entry<?>> LIST = new ArrayList<>();

	static {
		new Entry<FarpointObjective>("furthest pair distance", FarpointObjective.class, FarpointObjective::new);
		new Entry<BranchCountObjective>("branch count", BranchCountObjective.class, BranchCountObjective::new);
	}

}
