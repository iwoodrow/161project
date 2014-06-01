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
			int pathSize = pathSize(p.get(n));
			if (pathSize > max) max = pathSize;
		}
		return max;
	}

    static int pathSize(ArrayList<Point> path){
    	int size = 0;
        for (int i=0; i<path.size(); i++){
        	if (i == 0) continue;
        	if (path.get(i).x > path.get(i-1).y) size++;
        }
        return size;
    }

	static void FindShortestPaths(int l, int u) {
		if (u - l <= 1) return;
		int mid = (l + u) / 2;
		p.put(mid, SingleShortestPath(mid, p.get(l), p.get(u), l, u));
		FindShortestPaths(l, mid);
		FindShortestPaths(mid, u);
	}

	private static int inPath(ArrayList<Point> path, int row, int index){
		if (row < 0) return 1;
		if (index < 0) return -1;
		if (index >= path.get(row).x && index <= path.get(row).y) return 0;
        if (index < path.get(row).x) return -1;
        if (index > path.get(row).y) return 1;
        return 0;
	}

	private static ArrayList<Point> SingleShortestPath(int mid,
			ArrayList<Point> lower, ArrayList<Point> upper, int lowerIndex, int upperIndex) {
		int m = A.length, n = B.length;
		int i, j;
        mid++;
        lowerIndex++	;
        upperIndex++;
		//Initialize previous row (before first point of lower path) to 0
		for (j = 0; j <= n; j++) arr[mid - 1][j] = 0;


		int start = 1;
		int end;
		for (i = mid; i < upperIndex; i++) {
			//start = upper.get(i - mid).x;
			end = lower.get(i - lowerIndex).y;
			for (j = start; j <= end; j++) {
				int left = arr[i][j-1];
				int up = (inPath(lower, i - lowerIndex - 1, j) != 1) ? arr[i-1][j] : 0;
				arr[i][j] = Math.max(left, up);
				if (j!= 0 && A[(i-1) % A.length] == B[j - 1] && inPath(lower, i - lowerIndex - 1, j - 1) != 1) {
					arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
				}
			}
		}
		for (i = upperIndex; i < lowerIndex + m; i++){
			start = upper.get(i - upperIndex).x;
			end = lower.get(i - lowerIndex).y;
            for (j = start; j <= end; j++){
                int left = (inPath(upper, i - upperIndex, j - 1) != -1) ? arr[i][j - 1] : 0;
                int up =  (inPath(lower, i - lowerIndex - 1, j) != 1) ? arr[i - 1][j] : 0;
                arr[i][j] = Math.max(left, up);
                // System.err.println("i-1 % A.length: " + ((i-1) % A.length) + " a.length: " + A.length + " j-1: " + (j-1) + " B.length: " + B.length); 
                if (j!= 0 && A[(i-1) % A.length] == B[j-1] &&
                          (inPath(lower, i - lowerIndex - 1, j - 1) != 1) &&
                           inPath(upper, i - upperIndex - 1, j - 1) != -1){
                    arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
                }
            }
		}
        for (i = lowerIndex + m; i < mid + m; i++){
            start = upper.get(i - upperIndex).x;
            end = n;
            for (j = start; j <= end; j++){
                int left = (inPath(upper, i - upperIndex, j - 1) != -1) ? arr[i][j - 1] : 0;
                int up = arr[i - 1][j];
                arr[i][j] = Math.max(left, up);
                if (j!= 0 && A[(i-1) % A.length] == B[j-1] && inPath(upper, i - upperIndex - 1, j - 1) != -1) {
					arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
				}
            }
        }

		ArrayList<Point> points = new ArrayList<Point>();
		for (int f=0; f<m; f++){
			points.add(new Point(0,0));
		}
		int row = mid + m - 1;
		int col = n;
		for (i = m - 1; i >= 0; i--) {
			Point curr = new Point(col, col);
			while (col != 0 && A[(row-1) % A.length] != B[col-1] && arr[row][col - 1] >= arr[row - 1][col]) {
				//move left
				col--;
				//move leftmost left
				curr.x--;
			}
			if (col != 0 && row != 0 && A[(row-1) % A.length] == B[col-1]) {
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
		for (int f=0; f<m; f++){
			points.add(new Point(0,0));
		}
		int row = m;
		int col = n;
		for (i = m - 1; i >= 0; i--) {
			Point curr = new Point(col, col);
			while (col != 0 && A[row - 1] != B[col - 1] && arr[row][col - 1] > arr[row - 1][col]) {

				//move left
				col--;
				//move leftmost left
				curr.x--;
			}
			if (col != 0 &&	A[row - 1] == B[col - 1]) {
				col--;
				row--;
			} else {
				row--;
			}
			points.set(i, curr);
		}
        System.err.println("Points: " + points);
        p.put(0, points);
        p.put(m, points);

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