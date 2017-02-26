package debug;

public class IntegerQueue {

	/**
	 * The maximum packets queue size.
	 */
	public static final int MAX_SIZE = 200;
	/**
	 * The packets.
	 */
	private Integer[] packets;
	/**
	 * The cursor.
	 */
	private int cursor = 0;

	/**
	 * Constructs a new Integer Queue.
	 */
	public IntegerQueue() {
		packets = new Integer[MAX_SIZE];
	}

	/**
	 * Adds a new packet to the packet queue.
	 *
	 * @param packet
	 */
	public void add(Integer packet) {
		if(cursor + 1 >= MAX_SIZE) {
			return;
		}
		packets[cursor++] = packet;
	}

	/**
	 * Sets the specified packet index to null.
	 *
	 * @param index
	 */
	public void remove(int index) {
		packets[index] = null;
	}

	/**
	 * Gets the packet with the specified index.
	 *
	 * @param index
	 * @return the packet with the given index.
	 */
	public Integer get(int index) {
		return packets[index];
	}

	/**
	 * Sets the cursor.
	 *
	 * @param counter
	 */
	public void setCursor(int counter) {
		this.cursor = counter;
	}
}