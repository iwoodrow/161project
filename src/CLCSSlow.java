/*  Contributors
 *  Mariano Sorgente *** 05827781 *** marianos 
 *  Whitney LaRow *** 05821502 *** wlarow
 *  Isabelle Woodrow *** 05785520 *** iwoodrow
 */

import java.util.*;


public class CLCSSlow {
  static int[][] arr = new int[2048][2048];
  static char[] A, B;

  static int CLCS(){
	  int max = 0;
	  char[] newA = new char[2*A.length];
	  for(int i = 0; i < newA.length; i++){
		  int index = i%A.length;
		  if(index < 0) index +=A.length;
		  newA[i] = A[index];
	  }
	  for(int i =0; i<A.length; i++){
		  int curr = LCS(A.length - i, newA);
		  if(curr > max){
			  max = curr;
		  }
	  }
	  return max;
  }
  
  static int LCS(int start, char[] newA) {
    int m = A.length, n = B.length;
    int i, j;
    for (i = 0; i <= m; i++) arr[i][0] = 0;
    for (j = 0; j <= n; j++) arr[0][j] = 0;
    
    for (i = start+1; i <= start + m; i++) {
      for (j = 1; j <= n; j++) {
        arr[i-start][j] = Math.max(arr[i-1-start][j], arr[i-start][j-1]);
        if (newA[i-1] == B[j-1]) arr[i-start][j] = Math.max(arr[i-start][j], arr[i-1-start][j-1]+1);
      }
    }
    
    return arr[m][n];
  }

  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    int T = s.nextInt();
    for (int tc = 0; tc < T; tc++) {
      A = s.next().toCharArray();
      B = s.next().toCharArray();
      // System.err.println(tc);
      System.out.println(CLCS());
    }
  }
}
