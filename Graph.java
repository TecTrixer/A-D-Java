// Implementation of most algorithms, functions and data structures needed for graphs


// Main class used for testing, the class owning the data and implementing the graph algorithms is 'GraphClass'
public class Graph {
	public static void main(String[] args) {
		// Example graph from the A&D graph script (Part 7 Iterative Approach) 
		LinkedList[] edges = new LinkedList[8];
		// Node 0 A
		LinkedList node0 = new LinkedList();
		node0.addEdge(1, 1);
		node0.addEdge(2, 1);
		edges[0] = node0;
		// Node 1 B
		LinkedList node1 = new LinkedList();
		node1.addEdge(0, 1);
		node1.addEdge(2, 1);
		node1.addEdge(3, 1);
		node1.addEdge(5, 1);
		edges[1] = node1;
		// Node 2 C
		LinkedList node2 = new LinkedList();
		node2.addEdge(1, 1);
		node2.addEdge(0, 1);
		edges[2] = node2;
		// Node 3 D
		LinkedList node3 = new LinkedList();
		node3.addEdge(1, 1);
		node3.addEdge(4, 1);
		edges[3] = node3;
		// Node 4 E
		LinkedList node4 = new LinkedList();
		node4.addEdge(3, 1);
		node4.addEdge(5, 1);
		node4.addEdge(6, 1);
		node4.addEdge(7, 1);
		edges[4] = node4;
		// Node 5 F
		LinkedList node5 = new LinkedList();
		node5.addEdge(1, 1);
		node5.addEdge(4, 1);
		edges[5] = node5;
		// Node 6 G
		LinkedList node6 = new LinkedList();
		node6.addEdge(4, 1);
		node6.addEdge(7, 1);
		edges[6] = node6;
		// Node 7 H
		LinkedList node7 = new LinkedList();
		node7.addEdge(4, 2);
		node7.addEdge(6, 1);
		edges[7] = node7;
		GraphClass gc = new GraphClass(8, edges);

		// Here you can use the functions
		// this is just an example which prints the results of the Floyd-Warshall Algorithm
		printArrArr(gc.floydWarshall());
	}

	// Helper utility to print 1D Integer Arrays
	public static void printArr(int[] arr) {
		String res = "[";
		for (int x : arr) {
			res += x + ", ";
		}
		res = res.substring(0, res.length() - 2) + "]";
		System.out.println(res);
	}

	// Helper utility to print 1D Integer Arrays with padding
	public static void printArrWithSize(int[] arr, int size) {
		String res = "[";
		for (int x : arr) {
			String val = Integer.toString(x);
			String padding = " ".repeat(size - val.length());
			res += padding + val + ", ";
		}
		res = res.substring(0, res.length() - 2) + "]";
		System.out.println(res);
	}

	// Helper utility to print 2D Integer Arrays with padding
	public static void printArrArr(int[][] arrArr) {
		int max = 0;
		int min = 0;
		for (int i = 0; i < arrArr.length; i++) {
			for (int j = 0; j < arrArr[i].length; j++) {
				if (arrArr[i][j] > max)
					max = arrArr[i][j];
				if (arrArr[i][j] < min)
					min = arrArr[i][j];
			}
		}
		String maxStr = Integer.toString(max);
		String minStr = Integer.toString(min);
		int size = maxStr.length() > minStr.length() ? maxStr.length() : minStr.length();
		for (int i = 0; i < arrArr.length; i++) {
			printArrWithSize(arrArr[i], size);
		}
	}
}

// Class which contains all data of a graph and implements most graph algorithms
class GraphClass {
	// number of vertices
	int n;
	// adjacency list of edges for each vertex (default graph representation of this class)
	LinkedList[] edges;
	// number of outgoing edges for each vertex
	int[] degree;
	// helper variables for some algorithms
	int[] Z;
	int zIdx;
	boolean[] seen;
	// matrix representation of graph
	int[][] matrix;
	// edge representation of the graph with all graphs in one array
	Edge[] allEdges;

	// Constructor of a graph with number of vertices and adjacency list
	GraphClass(int n, LinkedList[] edges) {
		this.n = n;
		this.edges = edges;
		// generating degrees
		calcDegrees();
		this.Z = null;
		this.zIdx = 0;
		this.seen = null;
		// generating matrix represetation
		genMatrix();
		// generating edge representation
		genAllEdges();
	}

	// Helper utility for generating edge representation
	void genAllEdges() {
		// count number of total edges
		int num = 0;
		for (int i = 0; i < n; i++) {
			num += degree[i];
		}
		// insert all edges to array 'allEdges'
		Edge[] allEdges = new Edge[num];
		int eIdx = 0;
		for (int i = 0; i < n; i++) {
			ListNode curr = edges[i].first;
			while (curr != null) {
				allEdges[eIdx] = new Edge(i, curr.node, curr.weight);
				eIdx++;
				curr = curr.next;
			}
		}
		this.allEdges = allEdges;
	}

	// Helper utility for generating matrix representation
	void genMatrix() {
		// creating matrix with each element being Integer.MAX_VALUE by default (unreachable)
		int[][] matrix = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = Integer.MAX_VALUE;
			}
		}
		// setting all entries of the matrix to the weight of the corresponding edge
		for (int i = 0; i < n; i++) {
			ListNode curr = edges[i].first;
			while (curr != null) {
				matrix[i][curr.node] = curr.weight;
				curr = curr.next;
			}
		}
		for (int i = 0; i < n; i++) {
			matrix[i][i] = 0;
		}
		this.matrix = matrix;
	}

	// Helper utility to print generic arrays
	<T> void printArr(T[] arr) {
		String res = "[";
		for (T x : arr) {
			res += x + ", ";
		}
		res = res.substring(0, res.length() - 2) + "]";
		System.out.println(res);
	}

	// Helper utility to calculate the number of outgoing edges for each vertex
	void calcDegrees() {
		int[] degree = new int[n];
		for (int i = 0; i < n; i++) {
			degree[i] = edges[i].length;
		}
		this.degree = degree;
	}

	// Implementation of the Bellman-Ford algorithm using the edge representation of the graph
	int[] bellmanFord(int start) {
		// create distance array with all vertices being unreachable (Integer.MAX_VALUE) besides start
		int[] dist = new int[n];
		for (int i = 0; i < n; i++) {
			dist[i] = Integer.MAX_VALUE;
		}
		dist[start] = 0;
		// iterate n - 1 times and update each dist
		for (int i = 1; i < n; i++) {
			for (Edge uv : allEdges) {
				if (dist[uv.src] + uv.weight < dist[uv.dest]) {
					dist[uv.dest] = dist[uv.src] + uv.weight;
				}
			}
		}
		return dist;
	}

	// Implementation of the Floyd-Warshall algorithm using the matrix representatio of the graph
	int[][] floydWarshall() {
		// copy matrix representation
		int[][] calc = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				calc[i][j] = matrix[i][j];
			}
		}
		// execution of Floyd-Warshall algorithm (updating the distances each iteration)
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (calc[i][j] > calc[i][k] + calc[k][j] && calc[i][k] != Integer.MAX_VALUE
							&& calc[k][j] != Integer.MAX_VALUE) {
						calc[i][j] = calc[i][k] + calc[k][j];
					}
				}
			}
		}
		return calc;
	}

	// Function to detect whether the graph has an Eulerian Walk
	boolean hasEulerianWalk() {
		int numOdd = 0;
		for (int d : degree) {
			if (d % 2 == 1) {
				numOdd++;
			}
		}
		return numOdd <= 2;
	}

	// Function to find Eulerian Walk of the graph if it exists
	int[] Euler() {
		// if it does not exist return null
		if (!hasEulerianWalk())
			return null;
		// if it has already been computed return the old result (potential error as old result might have been overwritten)
		if (this.Z != null)
			return this.Z;
		// calculate number of vertices in Eulerian Walk
		int sumEdges = 0;
		for (int deg : degree) {
			sumEdges += deg;
		}
		this.Z = new int[sumEdges / 2 + 1];
		EulerWalk(0);
		return this.Z;
	}

	// Helper function to recursively generate an Eulerian Walk
	void EulerWalk(int vertex) {
		ListNode curr = edges[vertex].first;
		while (curr != null) {
			if (!curr.marked) {
				curr.marked = true;
				// mark other same edge
				ListNode sec = edges[curr.node].first;
				while (sec.node != vertex) {
					sec = sec.next;
				}
				sec.marked = true;
				EulerWalk(curr.node);
			}
			curr = curr.next;
		}
		// append element to result (array Z)
		this.Z[zIdx] = vertex;
		zIdx++;
	}

	// Implementation of Depth-First-Search (returns elements in order of visit)
	int[] dfs(int startingVertex) {
		this.seen = new boolean[n];
		this.Z = new int[n];
		this.zIdx = 0;
		recDFS(startingVertex);
		return this.Z;
	}

	// Helper function for recursive Depth-First-Search
	void recDFS(int vertex) {
		seen[vertex] = true;
		ListNode curr = edges[vertex].first;
		while (curr != null) {
			if (!seen[curr.node]) {
				recDFS(curr.node);
			}
			curr = curr.next;
		}
		this.Z[zIdx] = vertex;
		zIdx++;
	}

	// Funtion to calculate the number of Zusammenhangskomponenten using DFS
	int numZHKs() {
		int num = 0;
		this.seen = new boolean[n];
		int start = 0;
		int oldZIdx = 0;
		// while not all vertices have been marked do DFS on an unmarked vertex
		while (true) {
			num++;
			this.zIdx = 0;
			this.Z = new int[n];
			recDFS(start);
			if (zIdx + oldZIdx >= n) {
				break;
			}
			oldZIdx += zIdx;
			for (int i = 0; i < n; i++) {
				if (!seen[i]) {
					start = i;
					break;
				}
			}
		}
		return num;
	}

	// Implementation of Breadth-First-Search using a Queue (class defined after the GraphClass)
	int[] bfs(int startingVertex) {
		Queue q = new Queue();
		Boolean[] visited = new Boolean[n];
		for (int i = 0; i < n; i++) {
			visited[i] = false;
		}
		this.Z = new int[n];
		zIdx = 0;
		q.push(startingVertex);
		visited[startingVertex] = true;
		while (q.first != null) {
			int u = q.pop();
			this.Z[zIdx] = u;
			zIdx++;
			ListNode curr = edges[u].first;
			while (curr != null) {
				if (!visited[curr.node]) {
					q.push(curr.node);
					visited[curr.node] = true;
				}
				curr = curr.next;
			}
			printArr(visited);
		}
		return this.Z;
	}

	// Implementation of Dijkstra's algorithm to find single to all shortest distance
	int[] dijkstra(int start) {
		int[] dist = new int[n];
		Boolean[] seen = new Boolean[n];
		for (int i = 0; i < n; i++) {
			dist[i] = Integer.MAX_VALUE;
			seen[i] = false;
		}
		dist[start] = 0;
		PriorityQueue pq = new PriorityQueue();
		pq.insert(start, 0);
		while (pq.length > 0) {
			int u = pq.extract();
			if (seen[u])
				continue;
			seen[u] = true;
			ListNode curr = edges[u].first;
			while (curr != null) {
				if (!seen[curr.node] && dist[curr.node] > dist[u] + curr.weight) {
					dist[curr.node] = dist[u] + curr.weight;
					pq.insert(curr.node, dist[curr.node]);
				}
				curr = curr.next;
			}
		}
		return dist;
	}
}

// Edge class used in the edge representation of a graph
class Edge {
	int src;
	int dest;
	int weight;

	// Constructor of edge with given source and destination vertex and weight
	Edge(int src, int dest, int weight) {
		this.src = src;
		this.dest = dest;
		this.weight = weight;
	}
}

// LinkedList class implementing addEdge function and exposing underlying data structure
class LinkedList {
	ListNode first;
	int length;

	// Constructor of LinkedList with aan existing LinkedList
	LinkedList(ListNode first, int length) {
		this.first = first;
		this.length = length;
	}
	
	// Constructor resulting in an empty LinkedList
	LinkedList() {
		this.first = null;
		this.length = 0;
	}

	// Function to add an edge to a given destination vertex and with a given weight
	void addEdge(int node, int weight) {
		ListNode curr = first;
		ListNode par = null;
		while (curr != null) {
			par = curr;
			curr = curr.next;
		}
		if (par == null)
			first = new ListNode(node, weight);
		else
			par.next = new ListNode(node, weight);
		length++;
	}
}

// Class which represents an element of the LinkedList. Can be used to get other elements in LinkedList with .next field
class ListNode {
	public int node;
	public int weight;
	public boolean marked;
	public ListNode next;

	ListNode(int node, int weight) {
		this.node = node;
		this.weight = weight;
		this.next = null;
		this.marked = false;
	}
}

// Queue class used in BFS which implements push and pop functions
class Queue {
	QueueNode first;
	QueueNode last;

	// Constructor resulting in an empty queue
	Queue() {
		this.first = null;
		this.last = null;
	}

	// pushing element to back of the queue
	void push(int val) {
		if (first == null) {
			first = new QueueNode(val);
			last = first;
			return;
		}
		QueueNode newNode = new QueueNode(val);
		newNode.next = first;
		first.last = newNode;
		first = newNode;
	}

	// removing the first element in the queue and returning it
	int pop() {
		if (first == null)
			throw new java.util.NoSuchElementException();
		int res = last.val;
		if (last == first) {
			first = null;
			last = null;
			return res;
		}
		last.last.next = null;
		last = last.last;
		return res;
	}
}

// Class for elements of queue, can be used to navigate to other elements in the queue
class QueueNode {
	QueueNode next;
	QueueNode last;
	int val;

	// Constructor resulting in a QueueNode with given value
	QueueNode(int val) {
		this.val = val;
		this.next = null;
		this.last = null;
	}
}

// Implementation of a PriorityQueue used in Dijkstra's algorithm. This queue uses a MinHeap and can therefore not decrease the priority of an element. To work around this issue you can just insert the element with the new priority again.
class PriorityQueue {
	PrioNode[] data;
	int length;
	int capacity;
	
	// Constructor resulting in an empty PriorityQueue with a capacity of 5
	PriorityQueue() {
		this.data = new PrioNode[5];
		this.length = 0;
		this.capacity = 5;
	}

	// Function to insert an element into PriorityQueue (calls revHeapCond() to maintain HeapCondition)
	void insert(int node, int prio) {
		if (length >= capacity) {
			resize();
		}
		data[length] = new PrioNode(node, prio);
		revHeapCond(length);
		length++;
	}

	// helper function to maintain heap condition, goes from bottom of tree upwards and swaps elements if heap condition is not met
	void revHeapCond(int i) {
		while (i / 2 >= 0) {
			if (data[i / 2].prio <= data[i].prio)
				return;
			PrioNode temp = data[i];
			data[i] = data[i / 2];
			data[i / 2] = temp;
			i = i / 2;
		}
	}

	// function to extract the minimum value of the heap (calls heapCond() to maintain HeapCondition)
	int extract() {
		if (length <= 0)
			return -1;
		int res = data[0].node;
		data[0] = data[length - 1];
		length--;
		heapCond(0);
		return res;
	}

	// helper function to maintain heap condition (also known as restoreHeapCondition)
	void heapCond(int i) {
		while (i * 2 < length) {
			int j = i * 2;
			if (j + 1 < length && data[j + 1].prio < data[j].prio) {
				j += 1;
			}
			if (data[i].prio <= data[j].prio)
				return;
			PrioNode temp = data[i];
			data[i] = data[j];
			data[j] = temp;
			i = j;
		}
	}

	// helper function to double capacity if needed
	void resize() {
		PrioNode[] newData = new PrioNode[capacity * 2];
		for (int i = 0; i < length; i++) {
			newData[i] = data[i];
		}
		data = newData;
		capacity *= 2;
	}
}

// Class which represents an element in the PriorityQueue with a given node and a given priority
class PrioNode {
	int node;
	int prio;

	// Constructor resulting in a PriorityQueueNode with given node and priority
	PrioNode(int node, int prio) {
		this.node = node;
		this.prio = prio;
	}
}
