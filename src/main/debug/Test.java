package debug;

public class Test {

	private IntegerQueue queue = new IntegerQueue();
	private boolean writing = false;
	private int value = 0;

	public void write(int k) { // Just an example
		if(writing) {
			queue.add(k);
			return;
		}
		writing = true;
		for(int i = 0; i < IntegerQueue.MAX_SIZE; i++) {
			Integer queuedPacket = queue.get(i);
			if(queuedPacket == null) {
				queue.setCursor(0);
				break;
			}

			System.out.println(k + ":" + value);
			value++;
			queue.remove(i);
		}

		System.out.println(k + ":" + value);
		value++;
		writing = false;
	}

	public static void main(final String... args) {
		Test test = new Test();
		Thread t1 = new Thread(new TestThread(test, false));
		Thread t2 = new Thread(new TestThread(test, true));

		t1.start();
		t2.start();
	}


}