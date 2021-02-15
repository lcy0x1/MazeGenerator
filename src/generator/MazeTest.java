package generator;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import generator.MazeGen.Debugger;
import generator.MazeGen.State;
import generator.MazeGen.StateRim;

class MazeTest {

	public static void main() {
		new MazeTest();
	}

	private final Debugger deb = new Debugger();

	private MazeTest() {
		new Thread(this::thread_0).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(this::thread_1).start();
	}

	private void fill(char[][] chs, MazeGen maze, int i, int j) {
		for (int a = 0; a < 5; a++)
			for (int b = 0; b < 5; b++)
				if (chs[i * 5 + a][j * 5 + b] == '?')
					if (a == 0 || b == 0 || a == 4 || b == 4)
						chs[i * 5 + a][j * 5 + b] = 'x';
					else
						chs[i * 5 + a][j * 5 + b] = ' ';
		int conn = maze.conn[i][j];
		State st = maze.states[i][j];
		chs[i * 5 + 2][j * 5 + 2] = st == null ? ' ' : (char) ('0' + st.getInd());
		int trans = maze.inner(i + j * maze.w);
		int ai = trans % maze.w - i;
		int aj = trans / maze.w - j;
		if ((conn & 2) > 0) {
			int ti = aj;
			int tj = -ai;
			if (trans == -1) {
				if (i < maze.r && j < maze.r) {
					ti = 1;
					tj = 0;
				}
				if (i < maze.r && j > maze.r) {
					ti = 0;
					tj = -1;
				}
				if (i > maze.r && j < maze.r) {
					ti = 0;
					tj = 1;
				}
				if (i > maze.r && j > maze.r) {
					ti = -1;
					tj = 0;
				}
			}
			chs[i * 5 + 2 + ti * 2][j * 5 + 2 + tj * 2] = ' ';
		}
		if ((conn & 1) > 0) {
			int ti = -aj;
			int tj = ai;
			if (trans == -1) {
				if (i < maze.r && j < maze.r) {
					ti = 0;
					tj = 1;
				}
				if (i < maze.r && j > maze.r) {
					ti = 1;
					tj = 0;
				}
				if (i > maze.r && j < maze.r) {
					ti = -1;
					tj = 0;
				}
				if (i > maze.r && j > maze.r) {
					ti = 0;
					tj = -1;
				}
			}
			chs[i * 5 + 2 + ti * 2][j * 5 + 2 + tj * 2] = ' ';
		}
		if ((conn & 4) > 0) {
			chs[i * 5 + 2 + ai * 2][j * 5 + 2 + aj * 2] = ' ';
			chs[i * 5 + 2 + ai * 3][j * 5 + 2 + aj * 3] = ' ';
		}
	}

	private void fillAns(char[][] chs, MazeGen maze, int i, int j) {
		for (int a = 0; a < 5; a++)
			for (int b = 0; b < 5; b++)
				if (chs[i * 5 + a][j * 5 + b] == '?')
					if (a == 0 || b == 0 || a == 4 || b == 4)
						chs[i * 5 + a][j * 5 + b] = 'x';
					else
						chs[i * 5 + a][j * 5 + b] = ' ';
		int ans = maze.ans[i][j];
		if ((ans & 1) > 0)
			chs[i * 5 + 2 - 2][j * 5 + 1] = chs[i * 5 + 2 - 2][j * 5 + 2] = chs[i * 5 + 2 - 2][j * 5 + 3] = ' ';
		if ((ans & 2) > 0)
			chs[i * 5 + 2 + 2][j * 5 + 1] = chs[i * 5 + 2 + 2][j * 5 + 2] = chs[i * 5 + 2 + 2][j * 5 + 3] = ' ';
		if ((ans & 4) > 0)
			chs[i * 5 + 1][j * 5 + 2 - 2] = chs[i * 5 + 2][j * 5 + 2 - 2] = chs[i * 5 + 3][j * 5 + 2 - 2] = ' ';
		if ((ans & 8) > 0)
			chs[i * 5 + 1][j * 5 + 2 + 2] = chs[i * 5 + 2][j * 5 + 2 + 2] = chs[i * 5 + 3][j * 5 + 2 + 2] = ' ';
	}

	private void thread_0() {
		System.out.println("[builder] start");
		new MazeGen(5, new Random(), new MazeConfig(), deb).gen();
		System.out.println("[builder] end");
	}

	private void thread_1() {
		System.out.println("[console] press enter to start");
		Scanner sc = new Scanner(System.in);
		System.out.println("[console] start");
		String line;
		while (!(line = sc.nextLine()).equals("exit")) {
			if (line.equals("next")) {
				synchronized (deb) {
					deb.notify();
				}
			}
			MazeGen maze = deb.maze;
			if (line.equals("show conn")) {
				if (maze == null)
					System.out.println("no maze");
				else
					for (int x = 0; x < deb.maze.w; x++) {
						for (int y = 0; y < deb.maze.w; y++)
							System.out.print(deb.maze.conn[x][y]);
						System.out.println();
					}
			}
			if (line.equals("show state")) {
				if (maze == null)
					System.out.println("no maze");
				else
					for (int x = 0; x < maze.w; x++) {
						for (int y = 0; y < maze.w; y++) {
							State st = maze.states[x][y];
							System.out.print(st == null ? "." : st.getInd());
						}
						System.out.println();
					}
			}
			StateRim[] rims = deb.cur_rims;
			if (line.equals("show rims")) {
				if (rims == null || rims.length == 0)
					System.out.println("no rims");
				else {
					System.out.println("radius: " + rims[0].r);
					for (int i = 0; i < rims.length; i++) {
						System.out.println("rim " + i + ": x0 = " + rims[i].x0 + ", x1 = " + rims[i].x1 + ", "
								+ rims[i].state.getInd());
					}
				}
			}
			StateRim rim = deb.cur_rim;
			if (line.equals("show rim")) {
				if (rim == null)
					System.out.println("no rim");
				else {
					System.out.println("path: " + rim.path + ", loop: " + rim.loop);
					for (int i = 0; i < rim.paths.length; i++)
						System.out.print(rim.paths[i] + " ");
					System.out.println();
				}
			}
			if (line.equals("show root count")) {
				System.out.println("count: " + deb.root_count);
			}
			if (line.equals("draw")) {
				if (maze == null)
					System.out.println("no maze");
				else {
					char[][] chs = new char[maze.w * 5][maze.w * 5];
					for (int i = 0; i < maze.w * 5; i++)
						Arrays.fill(chs[i], '?');
					for (int i = 0; i < maze.w; i++)
						for (int j = 0; j < maze.w; j++) {
							try {
								fill(chs, maze, i, j);
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("err at " + i + "," + j);
							}

						}
					for (int i = 0; i < chs.length; i++) {
						for (int j = 0; j < chs.length; j++)
							System.out.print(chs[i][j]);
						System.out.println();
					}
				}
			}
			if (line.equals("draw ans")) {
				if (maze == null)
					System.out.println("no maze");
				else {
					char[][] chs = new char[maze.w * 5][maze.w * 5];
					for (int i = 0; i < maze.w * 5; i++)
						Arrays.fill(chs[i], '?');
					for (int i = 0; i < maze.w; i++)
						for (int j = 0; j < maze.w; j++) {
							try {
								fillAns(chs, maze, i, j);
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("err at " + i + "," + j);
							}

						}
					for (int i = 0; i < chs.length; i++) {
						for (int j = 0; j < chs.length; j++)
							System.out.print(chs[i][j]);
						System.out.println();
					}
				}
			}

		}
		synchronized (deb) {
			deb.notify();
		}
		sc.close();
		System.out.println("[console] end");
	}

}