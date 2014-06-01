import java.util.*;
import java.awt.Point;


public class CLCSFast {
	static int[][] arr = new int[4096][2048];
	static char[] A, B;
	static Map<Integer,
	ArrayList<Point>> p = new HashMap<Integer, ArrayList<Point>>();


	static int CLCS() {
		LCS();
		FindShortestPaths(0, A.length);
		int max = 0;
		for (int n : p.keySet()) {
			if (p.get(n).size() > max) max = p.get(n).size();
		}
		return max;
	}



	static void FindShortestPaths(int l, int u) {
		if (u - l <= 1) return;
		int mid = (l + u) / 2;
		p.put(mid, SingleShortestPath(mid, p.get(l), p.get(u), l, u));
		FindShortestPaths(l, mid);
		FindShortestPaths(mid, u);
	}

	private static boolean inPath(ArrayList<Point> path, int row, int index){
		return (index >= path.get(row).x && index <= path.get(row).y);
	}

	private static ArrayList<Point> SingleShortestPath(int mid,
			ArrayList<Point> lower, ArrayList<Point> upper, int lowerIndex, int upperIndex) {
		int m = A.length, n = B.length;
		int i, j;
		//Initialize previous row (before first point of lower path) to 0
		for (j = 0; j <= n; j++) arr[mid - 1][j] = 0;


		int start = 1;
		int end;
		for (i = mid; i < upperIndex; i++) {
			//start = upper.get(i - mid).x;
			end = lower.get(i - lowerIndex).y;
			for (j = start; j <= end; j++) {
				int left = arr[i][j-1];
				int up = 0;
				if (!inPath(lower, i - lowerIndex, j) || inPath(lower, i - lowerIndex - 1, j)){
					up = arr[i-1][j];
				}
				arr[i][j] = Math.max(left, up);
				if (A[i-1 % A.length] == B[j-1] && 
						(!inPath(lower, i - lowerIndex, j) || 
						  inPath(lower, i - lowerIndex - 1, j) || 
						  inPath(lower, i - lowerIndex - 1, j - 1))) {
					arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
				}
			}
		}
		for (i = upperIndex; i < lowerIndex + m; i++){
			start = upper.get(i - upperIndex).x;
			end = lower.get(i - lowerIndex).y;

		}

		ArrayList<Point> points = new ArrayList<Point>();
		//?????????
		int row = 1 + mid + m;
		int col = n;
		for (i = m - 1; i >= 0; i--) {
			Point curr = new Point(col, col);
			while (A[row] != B[col] && arr[row][col - 1] > arr[row - 1][col]) {
				//move left
				col--;
				//move leftmost left
				curr.x--;
			}
			if (A[row] == B[col]) {
				col--;
				row--;
			} else {
				row--;
			}
			points.set(i, curr);
		}


		return points;
	}



	static void LCS() {
		int m = A.length, n = B.length;
		int i, j;
		for (i = 0; i <= m; i++) arr[i][0] = 0;
		for (j = 0; j <= n; j++) arr[0][j] = 0;

		for (i = 1; i <= m; i++) {
			for (j = 1; j <= n; j++) {
				arr[i][j] = Math.max(arr[i-1][j], arr[i][j-1]);
				arr[i + m][j] = Math.max(arr[i-1][j], arr[i][j-1]);
				if (A[i-1] == B[j-1]) {
					arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
					arr[i + m][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
				}

			}
		}
		//path: ArrayList of Point(leftmost index, rightmost index) of a given row -- length m
		//one entry per row, path must span m rows


		ArrayList<Point> points = new ArrayList<Point>();		
		ArrayList<Point> pointsM = new ArrayList<Point>();
		int row = m;
		int col = n;
		for (i = m - 1; i >= 0; i--) {
			Point curr = new Point(col, col);
			while (A[row] != B[col] && arr[row][col - 1] > arr[row - 1][col]) {
				//move left
				col--;
				//move leftmost left
				curr.x--;
			}
			if (A[row] == B[col]) {
				col--;
				row--;
			} else {
				row--;
			}
			points.set(i, curr);
			points.set(i + m, curr);

		}


		//		ArrayList<Point> points = new ArrayList<Point>();
		//		int length = arr[m][n];
		//		int row = m;
		//		int col = n;
		//		while (length > 0) {
		//			
		//			if (A[row] == B[col]) {
		//				Point curr = new Point(row, col);
		//				points.add(curr);
		//				col--;
		//				row--;
		//				length--;
		//			} else {
		//				if (arr[row - 1][col] > arr[row][col - 1]) {
		//					row--;
		//				} else {
		//					col--;
		//				}
		//			}
		//			
		//		}
		//		ArrayList<Point> reversed = new ArrayList<Point>();
		//		ArrayList<Point> reversedM = new ArrayList<Point>();
		//		for (i = points.size() - 1; i >= 0; i--) {
		//			Point curr = new Point(points.get(i).x + m, points.get(i).y);
		//			reversed.add(points.get(i));
		//			reversedM.add(curr);
		//		}
		//		
		//		p.put(0, reversed);
		//		p.put(m, reversedM);
	}


	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int T = s.nextInt();
		for (int tc = 0; tc < T; tc++) {
			A = s.next().toCharArray();
			B = s.next().toCharArray();
			System.err.println(tc);
			System.out.println(CLCS());
		}
	}
}
