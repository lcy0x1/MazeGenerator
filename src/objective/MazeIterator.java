package objective;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MazeIterator<T extends MazeCellData<T>> {

	public static final int[][] DIRE = { { 0, -1, 4 }, { 0, 1, 8 }, { -1, 0, 1 }, { 1, 0, 2 } };

	public final T[][] value;
	public final int[][] maze;
	public final int n;
	public final Registry.Entry<T> ent;

	@SuppressWarnings("unchecked")
	public MazeIterator(int[][] maze, Registry.Entry<T> entry) {
		this.maze = maze;
		this.ent = entry;
		value = (T[][]) Array.newInstance(entry.cls, n = maze.length, maze.length);
	}

	public T iterate(int x, int y, int dire) {
		value[x][y] = ent.sup.get().setAccessDire(x, y, dire, n);
		int cell = maze[x][y];
		@SuppressWarnings("unchecked")
		T[] ans = (T[]) Array.newInstance(ent.cls, 4);
		int j = 0;
		for (int i = 0; i < DIRE.length; i++) {
			if ((cell & DIRE[i][2]) == 0)
				continue;
			int nx = x + DIRE[i][0];
			int ny = y + DIRE[i][1];
			if (nx < 0 || nx == n || ny < 0 || ny >= n)
				continue;
			if (value[nx][ny] != null)
				continue;
			ans[j++] = iterate(nx, ny, i);

		}
		value[x][y].fillData(Arrays.copyOf(ans, j));
		return value[x][y];
	}

}
