package objective;

public abstract class MazeCellData<T extends MazeCellData<T>> {

	public int access_direction;

	public abstract void fillData(T[] children);

	public abstract double getResult();

	@SuppressWarnings("unchecked")
	public T getThis() {
		return (T) this;
	}

	public T setAccessDire(int dire) {
		access_direction = dire;
		return getThis();
	}

}