package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

import generator.MazeConfig;
import generator.MazeGen;
import generator.MazeGen.Debugger;
import objective.Registry;

public class Statistics {

	public static final double[] EB = { /* 0.1, 0.15 , */ 0.2, 0.25/* , 0.3, 0.4, 0.5, 0.6 */ };
	public static final double[] EU = { 0.15, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8 };
	public static final double[] FU = { 0.01, 0.02, 0.04, 0.07, 0.1, 0.15, 0.2, 0.3 };
	public static final double[] SU = { 0.1, 0.15, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7 };
	public static final int[][] BW = { { 4, 1, 1 }, { 3, 2, 1 }, { 3, 1, 2 }, { 2, 3, 1 }, { 2, 2, 2 }, { 2, 1, 3 },
			{ 1, 4, 1 }, { 1, 3, 2 }, { 1, 2, 3 }, { 1, 1, 4 } };
	public static final int[][] SW = { { 4, 1 }, { 3, 2 }, { 2, 3 }, { 1, 4 } };
	public static final int TRIAL = 30;
	public static final int R = 25;
	public static final int SIZE = EB.length * EU.length * FU.length * SU.length * BW.length * SW.length;

	public static int r = R;
	public static MazeConfig config = new MazeConfig();
	private static int index = 0;
	private static PrintStream ps;

	public static void main(String[] args) throws IOException {
		File f = new File("51x51_1.csv");
		if (!f.exists())
			f.createNewFile();
		ps = new PrintStream(f);
		layer_0();
		ps.close();
	}

	public static void layer_0() {
		for (double eb : EB) {
			config.path_fac = eb;
			for (double eu : EU) {
				config.loop_fac = eu;
				for (double fu : FU) {
					config.conn_pri = fu;
					for (double su : SU) {
						config.conn_sec = su;
						for (int[] bw : BW) {
							config.path = bw;
							for (int[] sw : SW) {
								config.loop = sw;
								layer_5();
							}
						}
					}
				}
			}
		}
	}

	public static void layer_5() {
		int n = Registry.LIST.size();
		double[] sum = new double[n];
		double[] sumsq = new double[n];
		for (int trial = 0; trial < TRIAL; trial++) {
			MazeGen maze = new MazeGen(r, new Random(), config, new Debugger());
			maze.gen();
			for (int i = 0; i < n; i++) {
				double ans = Registry.LIST.get(i).execute(maze.ans, maze.r, maze.r);
				sum[i] += ans;
				sumsq[i] += ans * ans;
			}
		}
		ps.print(config.path_fac);
		ps.print(',');
		ps.print(config.loop_fac);
		ps.print(',');
		ps.print(config.conn_pri);
		ps.print(',');
		ps.print(config.conn_sec);
		ps.print(',');
		ps.print(config.path[0]);
		ps.print(',');
		ps.print(config.path[1]);
		ps.print(',');
		ps.print(config.loop[0]);
		ps.print(',');
		for (int i = 0; i < n; i++) {
			ps.print(sum[i]);
			ps.print(',');
			ps.print(sumsq[i]);
			ps.print(',');
		}
		ps.println();
		index++;
		if (index % 100 == 0)
			System.out.println(Math.round(index * 100000.0 / SIZE) / 1000.0 + "%");
	}

}
