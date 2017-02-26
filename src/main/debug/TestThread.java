package debug;

public class TestThread implements Runnable {

	private Test test;
	boolean odd;

	public TestThread(Test test, boolean odd) {
		this.test = test;
		this.odd = odd;
	}

	@Override
	public void run() {
		for(int i = 0; i < 200; i++) {
			test.write(i);
		}
	}
}
