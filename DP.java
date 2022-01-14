public class DP {
	public static void main(String[] args) {
		Helper.printArr(Fibonacci.sequenceUntilN(9));
	}
}

// Calculating Fibonacci numbers
class Fibonacci {
	static int nthFibNumber(int n) {
		int x = 0;
		int y = 1;
		for (int i = 0; i < n; i++) {
			int temp = y;
			y += x;
			x = temp;
		}
		return x;
	}

	static int[] sequenceUntilN(int n) {
		int[] res = new int[n];
		res[0] = 0;
		res[1] = 1;
		for (int i = 2; i < n; i++) {
			res[i] = res[i - 1] + res[i - 2];
		}
		return res;
	}
}

// Longest increasing subsequence
class LAT {
	static int length(int[] A) {
		int[] lastElement = new int[A.length + 1];
		for (int i = 1; i <= A.length; i++) {
			lastElement[i] = Integer.MAX_VALUE;
		}
		lastElement[0] = Integer.MIN_VALUE;
		for (int i = 0; i < A.length; i++) {
			int elem = A[i];
			int idx = Helper.binarySearch(lastElement, elem);
			lastElement[idx + 1] = elem;
		}
		int res = 0;
		for (int i = 0; i < lastElement.length; i++) {
			int elem = lastElement[i];
			if (elem != Integer.MAX_VALUE) {
				res = i;
			}
		}
		return res;
	}

	static int[] sequence(int[] A) {
		int[] lastElement = new int[A.length + 1];
		int[] predecessor = new int[A.length];
		for (int i = 1; i <= A.length; i++) {
			lastElement[i] = Integer.MAX_VALUE;
			predecessor[i - 1] = Integer.MIN_VALUE;
		}
		lastElement[0] = Integer.MIN_VALUE;
		for (int i = 0; i < A.length; i++) {
			int elem = A[i];
			int idx = Helper.binarySearch(lastElement, elem);
			lastElement[idx + 1] = elem;
			predecessor[idx] = lastElement[idx];
		}
		int res = 0;
		int last = Integer.MIN_VALUE;
		for (int i = 0; i < lastElement.length; i++) {
			int elem = lastElement[i];
			if (elem != Integer.MAX_VALUE) {
				res = i;
				last = elem;
			}
		}
		int[] sequence = new int[res];
		for (int i = 1; i <= res; i++) {
			sequence[i - 1] = predecessor[i];
		}
		sequence[res - 1] = last;
		return sequence;
	}
}

// Longest common subsequence of strings
class LGT {
	static int length(String a, String b) {
		int[][] dp = new int[a.length() + 1][b.length() + 1];
		for (int i = 0; i <= a.length(); i++) {
			dp[i][0] = 0;
		}
		for (int i = 0; i <= b.length(); i++) {
			dp[0][i] = 0;
		}
		for (int i = 1; i <= a.length(); i++) {
			for (int j = 1; j <= b.length(); j++) {
				if (a.charAt(i - 1) == b.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					dp[i][j] = Helper.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}
		return dp[a.length()][b.length()];
	}

	static String sequence(String a, String b) {
		int[][] dp = new int[a.length() + 1][b.length() + 1];
		for (int i = 0; i <= a.length(); i++) {
			dp[i][0] = 0;
		}
		for (int i = 0; i <= b.length(); i++) {
			dp[0][i] = 0;
		}
		for (int i = 1; i <= a.length(); i++) {
			for (int j = 1; j <= b.length(); j++) {
				if (a.charAt(i - 1) == b.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					dp[i][j] = Helper.max(dp[i - 1][j], dp[i][j - 1]);
				}
			}
		}
		String res = "";
		int i = a.length();
		int j = b.length();
		while (i > 0 && j > 0) {
			if (dp[i - 1][j] == dp[i][j]) {
				i--;
			} else if (dp[i][j - 1] == dp[i][j]) {
				j--;
			} else {
				res += a.charAt(i - 1);
				j--;
				i--;
			}
		}
		return Helper.reverseString(res);
	}
}

// Minimal editing distance
class MED {
	static int length(String a, String b) {
		int[][] dp = new int[a.length() + 1][b.length() + 1];
		for (int i = 0; i <= a.length(); i++) {
			dp[i][0] = i;
		}
		for (int i = 0; i <= b.length(); i++) {
			dp[0][i] = i;
		}
		for (int i = 1; i <= a.length(); i++) {
			for (int j = 1; j <= b.length(); j++) {
				int d = 0;
				if (a.charAt(i - 1) == b.charAt(j - 1)) {
					d = 1;
				}
				dp[i][j] = Helper.min(Helper.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1] - d) + 1;
			}
		}
		Helper.print2DArr(dp);
		return dp[a.length()][b.length()];
	}
}

// Subset sum problem
class SSP {
	static boolean possible(int[] A) {
		int sum = 0;
		for (int elem : A)
			sum += elem;
		if (sum % 2 == 1)
			return false;
		sum /= 2;
		boolean[][] dp = new boolean[A.length + 1][sum + 1];
		for (int i = 0; i <= A.length; i++) {
			dp[i][0] = true;
		}
		for (int j = 1; j <= sum; j++) {
			for (int i = 1; i <= A.length; i++) {
				if (j - A[i - 1] >= 0) {
					dp[i][j] = dp[i - 1][j] || dp[i - 1][j - A[i - 1]];
				} else {
					dp[i][j] = dp[i - 1][j];
				}
			}
		}
		return dp[A.length][sum];
	}

	static int[] subset(int[] A) {
		int sum = 0;
		for (int elem : A)
			sum += elem;
		if (sum % 2 == 1)
			return null;
		sum /= 2;
		boolean[][] dp = new boolean[A.length + 1][sum + 1];
		for (int i = 0; i <= A.length; i++) {
			dp[i][0] = true;
		}
		for (int j = 1; j <= sum; j++) {
			for (int i = 1; i <= A.length; i++) {
				if (j - A[i - 1] >= 0) {
					dp[i][j] = dp[i - 1][j] || dp[i - 1][j - A[i - 1]];
				} else {
					dp[i][j] = dp[i - 1][j];
				}
			}
		}
		if (!dp[A.length][sum]) return null;
		int[] subset = new int[A.length - 1];
		int idx = 0;
		int i = A.length;
		int j = sum;
		while (i > 0 && j > 0) {
			if (dp[i - 1][j]) {
				i--;
			} else {
				subset[idx++] = A[i - 1];
				j -= A[i-- - 1];
			}
		}
		int[] res = new int[idx];
		for (i = 0; i < idx; i++) {
			res[i] = subset[i];
		}
		return res;
	}
}

// Helper utility
class Helper {
	static int binarySearch(int[] A, int val) {
		int lower = 0;
		int upper = A.length;
		int mid = upper / 2;
		while (lower + 1 < upper) {
			if (A[mid] == val) {
				return mid;
			} else if (A[mid] < val) {
				lower = mid;
			} else {
				upper = mid;
			}
			mid = (upper + lower) / 2;
		}
		return mid;
	}

	static void printArr(int[] A) {
		if (A == null) {
			System.out.println("null");
			return;
		}
		String res = "[";
		for (int x : A) {
			res += x + ", ";
		}
		res = res.substring(0, res.length() - 2) + "]";
		System.out.println(res);
	}

	static String reverseString(String a) {
		String res = "";
		for (int i = a.length() - 1; i >= 0; i--) {
			res += a.charAt(i);
		}
		return res;
	}

	static void printArrWithPadding(int[] A, int size) {
		String res = "[";
		for (int x : A) {
			String val = Integer.toString(x);
			String padding = " ".repeat(size - val.length());
			res += padding + val + ", ";
		}
		res = res.substring(0, res.length() - 2) + "]";
		System.out.println(res);
	}

	static void print2DArr(int[][] A) {
		int maxWidth = 0;
		for (int[] row : A) {
			for (int x : row) {
				int length = Integer.toString(x).length();
				if (length > maxWidth)
					maxWidth = length;
			}
		}
		for (int[] row : A) {
			printArrWithPadding(row, maxWidth);
		}
	}

	static void printBoolArrWithPadding(boolean[] A, int size) {
		String res = "[";
		for (boolean x : A) {
			String val = Boolean.toString(x);
			String padding = " ".repeat(size - val.length());
			res += padding + val + ", ";
		}
		res = res.substring(0, res.length() - 2) + "]";
		System.out.println(res);
	}

	static void printBool2DArr(boolean[][] A) {
		int maxWidth = 0;
		for (boolean[] row : A) {
			for (boolean x : row) {
				int length = Boolean.toString(x).length();
				if (length > maxWidth)
					maxWidth = length;
			}
		}
		for (boolean[] row : A) {
			printBoolArrWithPadding(row, maxWidth);
		}
	}

	static int max(int a, int b) {
		if (a > b)
			return a;
		else
			return b;
	}

	static int min(int a, int b) {
		if (a < b)
			return a;
		else
			return b;
	}
}
