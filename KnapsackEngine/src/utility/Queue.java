package utility;

/**
 * Lightweight queue object for use with other sections of code
 * 
 * @author Pascal
 *
 * @param <T>
 */
public class Queue<T> {

	/**
	 * Node for individual points along the queue
	 * 
	 * @author pmkgb
	 *
	 */
	private class Node {
		public Node next;
		public T data;

		public Node(T data) {
			this.data = data;
		}
	}

	Node head;
	Node tail;

	public Queue() {
		head = null;
		tail = null;
	}

	public void enqueue(T item) {
		if (head == null) {
			head = new Node(item);
			tail = head;
		} else {
			tail.next = new Node(item);
			tail = tail.next;
		}
	}

	/**
	 * removes and returns the top item from the queue
	 * @returns the top item of the queue
	 */
	public T dequeue() {
		if (head == null) {
			return null;
		}
		T data = head.data;
		head = head.next;
		return data;
	}

	/**
	 * @returns the top item of the queue without removing
	 */
	public T peek() {
		return head.data;
	}

	/**
	 * @returns true if the queue is empty, false otherwise
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * prints out the object data for each item in the queue
	 */
	public void print() {
		Node curr = head;
		while (curr != null) {
			System.out.println(curr.data);
			curr = curr.next;
		}
	}
}
