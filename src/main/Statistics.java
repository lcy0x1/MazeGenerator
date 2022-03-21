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

	//public static final double[] EB = { 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 1 };
	//public static final double[] EU = { 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1 };
	//public static final double[] FU = { 0.01, 0.02, 0.04, 0.07, 0.1, 0.15, 0.2, 0.3 };
	//public static final double[] SU = { 0.1, 0.15, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7 };
	//public static final int[][] BW = { { 40, 10, 10 }, { 30, 20, 10 }, { 30, 10, 20 }, { 20, 30, 10 }, { 20, 20, 20 }, { 20, 10, 30 }, { 10, 40, 10 }, { 10, 30, 20 }, { 10, 20, 30 }, { 10, 10, 40 } };
	//public static final int[][] SW = { { 50, 10 }, { 40, 20 }, { 30, 30 }, { 20, 40 }, { 10, 50 } };
	
	
	//public static final double[] EB = { 0.25, 0.35, 0.45, 0.55, 0.65, 0.75, 0.85, 0.95 };
	//public static final double[] EU = { 0.25, 0.35, 0.45, 0.55, 0.65, 0.75, 0.85, 0.95 };
	//public static final double[] FU = { 0.005, 0.015, 0.03, 0.05, 0.12, 0.25, 0.35, 0.45 };
	//public static final double[] SU = { 0.12, 0.18, 0.25, 0.35, 0.45, 0.55, 0.65, 0.75 };
	//public static final int[][] BW = { { 35, 10, 15 }, { 25, 20, 15 }, { 25, 10, 25 }, { 15, 30, 15 }, { 15, 20, 25 }, { 15, 10, 35 }, { 15, 40, 15 }, { 5, 30, 25 }, { 5, 20, 35 }, { 5, 10, 45 } };
	//public static final int[][] SW = { { 55, 5 }, { 45, 15 }, { 25, 35 }, { 15, 45 }, { 5, 55 } };
	
	//public static final int[][] BW = { { 35, 15, 10 }, { 25, 25, 10 }, { 25, 15, 20 }, { 15, 35, 10 }, { 15, 25, 20 }, { 15, 15, 30 }, { 15, 45, 10 }, { 5, 35, 20 }, { 5, 25, 30 }, { 5, 15, 40 } };
	
	public static final double[] EB = { 0.25, 0.28, 0.31, 0.34, 0.38, 0.42, 0.46, 0.5 };
	public static final double[] EU = { 0.45, 0.5, 0.55, 0.6, 0.65, 0.7, 0.8, 0.9 };
	public static final double[] FU = { 0.005, 0.01, 0.02, 0.03, 0.04, 0.06, 0.08, 0.1 };
	public static final double[] SU = { 0.7, 0.8, 0.85, 0.9, 0.925, 0.95, 0.975, 1 };
	public static final int[][] BW = { { 3, 3, 54 }, { 3, 6, 51 }, { 6, 3, 51 }, { 6, 6, 48 }, { 6, 9, 45 }, { 9, 6, 45 }, { 3, 9, 48 }, { 9, 3, 48 }, { 9, 9, 42 }};
	public static final int[][] SW = { { 1, 59 }, { 2, 58 }, { 3, 57 }, { 4, 56 }, { 5, 55 } };
	
	public static final int TRIAL = 50;
	public static final int R = 12;
	public static final int SIZE = EB.length * EU.length * FU.length * SU.length * BW.length * SW.length;

	public static int r = R;
	public static MazeConfig config = new MazeConfig();
	private static int index = 0;
	private static PrintStream ps;

	public static void main(String[] args) throws IOException {
		File f = new File("25x25_333333.csv");
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
		ps.print(config.path[0]/10.0);
		ps.print(',');
		ps.print(config.path[1]/10.0);
		ps.print(',');
		ps.print(config.loop[0]/10.0);
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
