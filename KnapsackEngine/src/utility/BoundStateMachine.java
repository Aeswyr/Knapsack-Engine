package utility;

import java.util.ArrayList;

public class BoundStateMachine<T> {

	HashTable<String, Node> graph;
	Node activeState;

	public BoundStateMachine() {
		graph = new HashTable<String, Node>();
	}

	public void addState(String tag, int stateTime, T bind) {
		Node n = new Node();
		n.bound = bind;
		n.timeMax = stateTime;
		n.tag = tag;
		graph.add(tag, n);
	}

	public void removeState(String tag, int stateTime, T bind) {
		Node n = graph.get(tag);
		for (int i = 0; i < n.links.size(); i++) {
			graph.get(n.links.get(i).tag).backLink.remove(n);
		}
		for (int i = 0; i < n.backLink.size(); i++) {
			ArrayList<Link> l = n.backLink.get(i).links;
			for (int j = 0; j < l.size(); j++) {
				if (l.get(j).tag.equals(tag)) {
					l.remove(i);
					i--;
				}
			}
		}
		graph.remove(tag);
	}

	int timer = -1;

	public void update() {
		if (timer > 0) {
			timer--;
		} else if (timer == 0) {
			Node n = activeState;
			for (int i = 0; i < n.links.size(); i++) {
				if (n.links.get(i).state.check()) {
					activeState = graph.get(n.links.get(i).tag);
					timer = activeState.timeMax;
					break;
				}
			}
		}
	}

	public void forceState(String tag) {
		activeState = graph.get(tag);
		timer = activeState.timeMax;
	}
	
	public void forceStateCheck() {
		Node n = activeState;
		for (int i = 0; i < n.links.size(); i++) {
			if (n.links.get(i).state.check()) {
				activeState = graph.get(n.links.get(i).tag);
				timer = activeState.timeMax;
				break;
			}
		}

	}

	public void preserveState() {
		timer++;
	}

	public T get() {
		return activeState.bound;
	}

	public String getState() {
		return activeState.tag;
	}
	
	public void changeBind(String tag, T bind) {
		graph.get(tag).bound = bind;
	}

	public void addLink(String sourceTag, String targetTag, StateChange changeEvent) {
		graph.get(sourceTag).links.add(new Link(targetTag, changeEvent));
		graph.get(targetTag).backLink.add(graph.get(sourceTag));
	}

	private class Node {
		String tag;
		T bound;
		int timeMax;
		ArrayList<Link> links;
		ArrayList<Node> backLink;

		public Node() {
			links = new ArrayList<Link>();
			backLink = new ArrayList<Node>();
		}
	}

	private class Link {
		String tag;
		StateChange state;

		public Link(String tag, StateChange state) {
			this.tag = tag;
			this.state = state;
		}
	}

}
