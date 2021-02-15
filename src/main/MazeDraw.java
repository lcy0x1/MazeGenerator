package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import generator.MazeConfig;
import generator.MazeGen;
import generator.MazeGen.Debugger;
import objective.Registry;

public class MazeDraw {

	public static void drawPNG(MazeGen maze) throws IOException {
		File f = new File("./out.png");
		BufferedImage bimg = new BufferedImage(maze.w * 5, maze.w * 5, BufferedImage.TYPE_3BYTE_BGR);
		for (int i = 0; i < maze.w; i++)
			for (int j = 0; j < maze.w; j++)
				fillAns(bimg, maze, i, j);
		if (!f.exists())
			f.createNewFile();
		ImageIO.write(bimg, "PNG", f);
	}

	public static void main(String[] args) throws IOException {
		File fx = new File("./log.txt");
		if (!fx.exists())
			fx.createNewFile();
		PrintStream ps = new PrintStream(fx);
		// System.setErr(ps);
		// System.setOut(ps);
		try {
			MazeConfig config = readConfig();
			MazeGen maze = new MazeGen(25, new Random(), config, new Debugger());
			maze.gen();
			for (Registry.Entry<?> ent : Registry.LIST) {
				double ans = ent.execute(maze.ans, maze.r, maze.r);
				System.out.println(ent.name + ": " + ans);
			}
			// drawPNG(maze);
		} catch (Exception e) {
			e.printStackTrace(ps);
		}
		ps.close();
	}

	public static MazeConfig readConfig() throws IOException {
		File in = new File("./in.txt");
		List<String> list = Files.readAllLines(in.toPath());
		String[] l0 = list.get(0).split(":")[1].trim().split(" ");
		int[] i0 = new int[l0.length];
		for (int i = 0; i < l0.length; i++)
			i0[i] = Integer.parseInt(l0[i].trim());
		String[] l1 = list.get(1).split(":")[1].trim().split(" ");
		int[] i1 = new int[l1.length];
		for (int i = 0; i < l1.length; i++)
			i1[i] = Integer.parseInt(l1[i].trim());
		int i2 = Integer.parseInt(list.get(2).split(":")[1].trim());
		int i3 = Integer.parseInt(list.get(3).split(":")[1].trim());
		int i4 = Integer.parseInt(list.get(4).split(":")[1].trim());
		int i5 = Integer.parseInt(list.get(5).split(":")[1].trim());
		return new MazeConfig(i0, i1, i2 * 0.01, i3 * 0.01, i4 * 0.01, i5 * 0.01);
	}

	private static void fillAns(BufferedImage chs, MazeGen maze, int i, int j) {
		for (int a = 0; a < 5; a++)
			for (int b = 0; b < 5; b++)
				if (a == 0 || b == 0 || a == 4 || b == 4)
					chs.setRGB(i * 5 + a, j * 5 + b, 0x000000);
				else
					chs.setRGB(i * 5 + a, j * 5 + b, 0xffffff);
		int ans = maze.ans[i][j];
		for (int k = 1; k <= 3; k++) {
			if ((ans & 1) > 0)
				chs.setRGB(i * 5, j * 5 + k, 0xffffff);
			if ((ans & 2) > 0)
				chs.setRGB(i * 5 + 4, j * 5 + k, 0xffffff);
			if ((ans & 4) > 0)
				chs.setRGB(i * 5 + k, j * 5, 0xffffff);
			if ((ans & 8) > 0)
				chs.setRGB(i * 5 + k, j * 5 + 4, 0xffffff);
		}
	}

}