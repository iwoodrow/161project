import java.util.*;
import java.awt.Point;


public class CLCSFast {
	static int[][] arr;
	static char[] A, B;
	static Map<Integer, Point[]> p;


	static int CLCS() {
		LCS();
		FindShortestPaths(0, A.length);
		int max = 0;
		Point[] maxPath = null;
		for (int n : p.keySet()) {
			int pathSize = pathSize(p.get(n));
			if (pathSize > max){
				max = pathSize;
				maxPath = p.get(n);	
			} 
		}
		for (int i=0; i<maxPath.length; i++){
			System.err.print(maxPath[i] + ", ");
		}
		System.err.println("\n\n\n\n\n\n\n\n");
		return max;
	}

    static int pathSize(Point[] path){
    	int size = 0;
        for (int i=0; i<path.length; i++){
        	if (i == 0) continue;
        	if (path[i].x > path[i-1].y) size++;
        }
        return size;
    }

	static void FindShortestPaths(int l, int u) {
		// System.err.println("Calculating shortest path on " + l + ", " + u);
		if (u - l <= 1) return;
		int mid = (l + u) / 2;

		p.put(mid, SingleShortestPath(mid, p.get(l), p.get(u), l, u));
		 Set<Point> s = new HashSet<Point>();
		 for (int row : p.keySet()){
		 	for (Point p1 : p.get(row)){
		 		for (int x=p1.x; x<=p1.y; x++){
		 			s.add(new Point(row, x));
		 		}
		 		row++;
		 	}
		 }
		 printArr(s);	
		 System.err.println(p.get(mid));	// System.err.prin
		 try{Thread.sleep(1000);}catch(Exception e){}

		FindShortestPaths(l, mid);
		FindShortestPaths(mid, u);
	}

	private static int inPath(Point[] path, int row, int index){
		if (row < 0) return 1;
		if (index < 0) return -1;
		if (index >= path[row].x && index <= path[row].y) return 0;
        if (index < path[row].x) return -1;
        if (index > path[row].y) return 1;
        return 0;
	}

	private static Point[] SingleShortestPath(int mid,
			Point[] lower, Point[] upper, int lowerIndex, int upperIndex) {
		int m = A.length, n = B.length;
		int i, j;
        mid++;
        lowerIndex++;
        upperIndex++;
		//Initialize previous row (before first point of lower path) to 0
		for (j = 0; j <= n; j++) arr[mid - 1][j] = 0;


		int start = 1;
		int end;
		for (i = mid; i < upperIndex; i++) {
			//start = upper.get(i - mid).x;
			end = lower[i - lowerIndex + 1].y;
			for (j = start; j <= end; j++) {
				int left = arr[i][j-1];
				int up = (inPath(lower, i - lowerIndex, j) != 1) ? arr[i-1][j] : 0;
				arr[i][j] = Math.max(left, up);
				if (j!= 0 && A[(i-1) % A.length] == B[j - 1] && inPath(lower, i - lowerIndex, j - 1) != 1) {
					arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
				}
			}
		}
		for (i = upperIndex; i < lowerIndex + m; i++){
			start = upper[i - upperIndex + 1].x;
			end = lower[i - lowerIndex + 1].y;
            for (j = start; j <= end; j++){
                int left = (inPath(upper, i - upperIndex + 1, j - 1) != -1) ? arr[i][j - 1] : 0;
                int up =  (inPath(lower, i - lowerIndex, j) != 1) ? arr[i - 1][j] : 0;
                arr[i][j] = Math.max(left, up);
                // System.err.println("i-1 % A.length: " + ((i-1) % A.length) + " a.length: " + A.length + " j-1: " + (j-1) + " B.length: " + B.length); 
                if (j!= 0 && A[(i-1) % A.length] == B[j-1] &&
                          (inPath(lower, i - lowerIndex, j - 1) != 1) &&
                           inPath(upper, i - upperIndex, j - 1) != -1){
                    arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
                }
            }
		}
		for (i = lowerIndex + m; i < mid + m; i++){
			start = upper[i - upperIndex + 1].x;
			end = n;
			for (j = start; j <= end; j++){
				int left = (inPath(upper, i - upperIndex + 1, j - 1) != -1) ? arr[i][j - 1] : 0;
				int up = arr[i - 1][j];
				arr[i][j] = Math.max(left, up);
				if (j!= 0 && A[(i-1) % A.length] == B[j-1] && inPath(upper, i - upperIndex, j - 1) != -1) {
					arr[i][j] = Math.max(arr[i][j], arr[i-1][j-1]+1);
				}
			}
		}
		return pointList(mid + m - 1);
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
		p.put(0, pointList(m));
		p.put(m, pointList(m));
	}

	private static Point[] pointList(int row) {
		int m = A.length;
    	int n = B.length;
		Point[] points = new Point[m + 1];		
		for (int f=0; f<=m; f++){
			points[f] = new Point(0,0);
		}
		int col = n;
		for (int i = m - 1; i >= 0; i--) {
			Point curr = new Point(col, col);
			while (col != 0 && A[(row - 1) % A.length] != B[col - 1] && arr[row][col - 1] >= arr[row - 1][col]) {
							try{
			// Thread.sleep(2000);
			}catch(Exception e){}	
			// printArr(row, col);//move left
				col--;
				//move leftmost left 
				curr.x--;

			}
			if (col != 0 &&	row != 0 && A[(row - 1) % A.length] == B[col - 1]) {
							try{
			// Thread.sleep(2000);
			}catch(Exception e){}	
			// printArr(row, col);
				col--;
				row--;
			} else {
							try{
			// Thread.sleep(2000);
			}catch(Exception e){}	
			// printArr(row, col);
				row--;
			}
			points[i + 1] = curr;

		}
		return points;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int T = s.nextInt();
		for (int tc = 0; tc < T; tc++) {
			A = s.next().toCharArray();
			B = s.next().toCharArray();
			arr = new int[4096][2048];
			p = new HashMap<Integer, Point[]>();
			System.err.println(tc);
			System.out.println(CLCS());
		}
	}
}
