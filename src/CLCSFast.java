import java.util.*;
import java.awt.Point;


public class CLCSFast {
	static int[][] arr;
	static char[] A, B;
	static Map<Integer, Point[]> p;
	static int m, n;

	static int CLCS() {
		LCS();


		// System.err.println("Original array");
		//  Set<Point> s = new HashSet<Point>();
		//  for (int row : p.keySet()){
		//  	for (Point p1 : p.get(row)){
		//  		for (int x=p1[0]; x<=p1[1]; x++){
		//  			s.add(new Point(row, x));
		//  		}
		//  		row++;
		//  	}
		//  }
		//  printArr(s);	


		FindShortestPaths(0, A.length);
		int max = 0;
		Point[] maxPath = null;
		for (int n : p.keySet()) {
			int pathSize = pathSize(p.get(n), max);
			if (pathSize > max){
				max = pathSize;
				maxPath = p.get(n);	
			} 
		}
		if (maxPath == null) return 0;
		for (int i=0; i<maxPath.length; i++){
			// System.err.print(maxPath[i] + ", ");
		}
		// System.err.println("\n\n\n\n\n\n\n\n");
		return max;
	}

    static int pathSize(Point[] path, int maxLen){
    	int size = 0;
    	int pathLen = path.length;
        for (int i=1; i<pathLen; i++){
        	if (path[i][0] > path[i-1][1]) size++;
        	if (pathLen - i + size < maxLen - 1) break;
        }
        return size;
    }

	static void FindShortestPaths(int l, int u) {
		if (u - l <= 1) return;
		int mid = (l + u) / 2;


		Point[] midList = SingleShortestPath(mid, p.get(l), p.get(u), l, u);
		p.put(mid, midList);

		// System.err.println("Calculating shortest path on " + l + ", " + u);
		//  Set<Point> s = new HashSet<Point>();
		//  for (int row : p.keySet()){
		//  	for (Point p1 : p.get(row)){
		//  		for (int x=p1[0]; x<=p1[1]; x++){
		//  			s.add(new Point(row, x));
		//  		}
		//  		row++;
		//  	}
		//  }
		//  printArr(s);
		//  for(int i = 0; i < midList.length; i++) {
		//  	System.err.print(midList[i] + "  ");
		//  }
		//  System.err.println();
		//  try{Thread.sleep(1000);}catch(Exception e){}



		FindShortestPaths(l, mid);
		FindShortestPaths(mid, u);
	}

	private static boolean inRange(Point[] lower, Point[] upper, int lowerIndex, int upperIndex, int row, int col){
		if (row < 0) return false;
		if (col < 0) return false;
		// System.err.println("row: " + row + " col: " + col + " li: " + lowerIndex + " ui: " + upperIndex);
		if (upperIndex <= row && col < upper[row - upperIndex][0]) return false;
		if (row < lowerIndex + lower.length && col > lower[row - lowerIndex][1]) return false;
        return true;
	}

	private static Point[] SingleShortestPath(int mid,
			Point[] lower, Point[] upper, int lowerIndex, int upperIndex) {
		int i, j;
        mid++;
        lowerIndex++;
        upperIndex++;
		//Initialize previous row (before first point of lower path) to 0
		for (j = 0; j <= n; j++) arr[mid - 1][j] = 0;

		int start = 1;
		int end;
		for (i = mid; i < upperIndex; i++) {
			end = lower[i - lowerIndex + 1][1];
			for (j = start; j <= end; j++) {
				int left = arr[i][j-1];
				int up = (inRange(lower, upper, lowerIndex - 1, upperIndex - 1, i - 1, j)) ? arr[i-1][j] : 0;
				arr[i][j] = Math.max(left, up);
				if (j!= 0 && A[(i-1) % A.length] == B[j - 1] && inRange(lower, upper, lowerIndex - 1, upperIndex - 1, i - 1, j - 1)) {
					arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
				}
			}
		}
		for (i = upperIndex; i < lowerIndex + m; i++){
			start = upper[i - upperIndex + 1][0];
			end = lower[i - lowerIndex + 1][1];
            for (j = start; j <= end; j++){
                int left = (inRange(lower, upper, lowerIndex - 1, upperIndex - 1, i, j - 1)) ? arr[i][j - 1] : 0;
                int up =  (inRange(lower, upper, lowerIndex - 1, upperIndex - 1, i - 1, j)) ? arr[i - 1][j] : 0;
                arr[i][j] = Math.max(left, up);
                // System.err.println("i-1 % A.length: " + ((i-1) % A.length) + " a.length: " + A.length + " j-1: " + (j-1) + " B.length: " + B.length); 
                if (j!= 0 && A[(i-1) % A.length] == B[j-1] &&
                          (inRange(lower, upper, lowerIndex - 1, upperIndex - 1, i - 1, j - 1))){
                    arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
                }
            }
		}
		for (i = lowerIndex + m; i < mid + m; i++){
			start = upper[i - upperIndex + 1][0];
			end = n;
			for (j = start; j <= end; j++){
				int left = (inRange(lower, upper, lowerIndex - 1, upperIndex - 1, i, j - 1)) ? arr[i][j - 1] : 0;
				int up = arr[i - 1][j];
				arr[i][j] = Math.max(left, up);
				if (j!= 0 && A[(i-1) % A.length] == B[j-1] && inRange(lower, upper, lowerIndex - 1, upperIndex - 1, i - 1, j - 1)) {
					arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
				}
			}
		}
		return pointList(mid + m - 1, lower, upper, lowerIndex, upperIndex);
	}

	static void printArr(Set<Point> points){
		System.err.print("      ");
		for (int col=1; col<=B.length; col++){
		    System.err.print(" " + B[col - 1] +"  ");
		}
		System.err.println();
		System.err.print("  ");
		for (int row=0; row<=2*A.length; row++){
			if (row != 0) System.err.print(A[(row-1) % A.length] + " ");
		    for (int col=0; col<=B.length; col++){
		    	if (points.contains(new Point(row, col))){
		    	System.err.print("[" + arr[row][col] +"]");
		    	}else{
		    	System.err.print(" " + arr[row][col] +" ");
		    	}
		    	if (arr[row][col] < 10) System.err.print(" ");
			}
		    System.err.println();
		}		
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
		//System.err.println("Points: " + pointList(m));
		p.put(0, pointList(m, null, null, 0, 0));
		p.put(m, pointList(m, null, null, 0, 0));
	}

	private static Point[] pointList(int row, Point[] lower, Point[] upper, int lowerIndex, int upperIndex) {
		int m = A.length;
    	int n = B.length;
		Point[] points = new Point[m + 1];		
		points[0] = new Point(0,0);
		int col = n;
		// System.err.print("path: " + ((lowerIndex + upperIndex )/ 2));
		for (int i = m; i > 0; i--) {
			Point curr = new Point(col, col)	;
			boolean leftOOB = (lower == null) ? false : !inRange(lower, upper, lowerIndex - 1, upperIndex - 1, row, col - 1);
			boolean upOOB = (lower == null) ? false : !inRange(lower, upper, lowerIndex - 1, upperIndex - 1, row - 1, col);
			// System.err.println("upOOB " + upOOB);
			while (!leftOOB && col > 0 && A[(row - 1) % A.length] != B[col - 1] && (upOOB || (arr[row][col - 1] >= arr[row - 1][col]))) 	 {
				col--;
				curr[0]--;
				leftOOB = (lower == null) ? false : !inRange(lower, upper, lowerIndex - 1, upperIndex - 1, row, col - 1);
				if (upOOB) upOOB = (lower == null) ? false : !inRange(lower, upper, lowerIndex - 1, upperIndex - 1, row - 1, col);
				// System.err.println("upOOB2 " + upOOB);
			}
			if (col != 0 &&	row != 0 && A[(row - 1) % A.length] == B[col - 1]) {
				col--;
				row--;
			} else {
				row--;
			}
			points[i] = curr;
		}
		return points;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int T = s.nextInt();
		for (int tc = 0; tc < T; tc++) {
			A = s.next().toCharArray();
			B = s.next().toCharArray();
			m = A.length;
			n = B.length;
			arr = new int[4096][2048];
			p = new HashMap<Integer, Point[]>();
			System.err.println(tc);
			System.out.println(CLCS());
		}
	}
}
