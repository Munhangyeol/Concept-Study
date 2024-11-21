package com.example.EntityPractice;

//package com.example.entityPractice;


public class Counter {
	private int count = 0;

	// Increment method with synchronized block
	//Increment가 사용중에는 해당 클래스에 접근을 막으라는 의미임
	public synchronized  void increment() {
			System.out.println("count: " + count);
			count++;
	}

	// Decrement method with synchronized block
	//decrement가 사용중에는 해당 클래스에 접근을 막으라는 의미임
	public void decrement() {
			System.out.println("count: " + count);
			count--;
	}

	// Get current count
	public int getCount() {
		synchronized (this) {
			System.out.println("count: "+count);
			return count;
		}
	}

	public static void main(String[] args) {
		Counter counter = new Counter();

		// Create threads that increment and decrement the counter
		Thread incrementThread = new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				counter.increment();
			}
		});

		Thread decrementThread = new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				counter.decrement();
			}
		});

		// Start the threads
		incrementThread.start();
		decrementThread.start();

		// Wait for threads to finish
		try {
			incrementThread.join();
			decrementThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Print the final count value
		System.out.println("Final count: " + counter.getCount());
	}
}

