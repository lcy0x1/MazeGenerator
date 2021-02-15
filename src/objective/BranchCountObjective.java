package objective;

public class BranchCountObjective extends MazeIterator.MazeCellData<BranchCountObjective> {

	/** branch count */
	public int count;

	@Override
	public void fillData(BranchCountObjective[] children) {
		if (children.length == 0)
			count = 1;
		else if (children.length == 1)
			count = children[0].count;
		else {
			count = 0;
			for (BranchCountObjective obj : children) {
				count += obj.count;
			}
		}
	}

	@Override
	public double getResult() {
		return count;
	}

}
