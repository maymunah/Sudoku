import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;



public class Solver{

	public static long end;

	private long[] finalOutput;
	private int n;
	private int p;
	private int q;
	private int assignments;

	private int grid[][];
	private boolean fixedCell[][];
	private int startDomainIndex[][];
	private ArrayList<Integer> cellDomain;
	private ArrayList<ArrayList<ArrayList<Integer>>> domains;
	private int nextAvailCell[];


	private  long totalsolverstart;

	public Solver(long t){
		totalsolverstart = t;
		n = 9;
		p = 3;
		q = 3;
		assignments = 0;

		grid = new int[9][9];                                       // initialized with 0
		fixedCell = new boolean[9][9];                              // initialized with false
		startDomainIndex = new int[9][9];                           // initialized with 0


		cellDomain = new ArrayList<Integer>();
		for (int i = 1; i < 10; i++){
			cellDomain.add(i);                                      // cellDomain = [1,2,3,4,5,6,7,8,9]
		}

		domains = new ArrayList<ArrayList<ArrayList<Integer>>>();   // stores cellDomains for all cells
		for (int i = 0; i < 9; i++){
			domains.add(new ArrayList<ArrayList<Integer>>());
			for (int j = 0; j < 9; j++){
				domains.get(i).add(new ArrayList<Integer>());
				domains.get(i).get(j).addAll(cellDomain);//add(cellDomain);
			}



		}
		finalOutput = new long[5];
		// 0: Time
		// 1: #assignments
		// 2: Solution (0: no, 1:yes)
		// 3: Timeout  (0: no, 1:yes)
		// 4: search time
		nextAvailCell = new int[2];                                // stores i and j of next cell


	}


	public void readInput(InputStream input) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String inputLine = reader.readLine();
		StringTokenizer st = new StringTokenizer(inputLine," ");
		n = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());

		// copying data from file into grid
		for (int i = 0; i < 9; i++){
			inputLine = reader.readLine();
			st = new StringTokenizer(inputLine," ");
			for (int j = 0; j < 9; j++){
				grid[i][j] = Integer.parseInt(st.nextToken());
				if (grid[i][j] != 0){
					fixedCell[i][j] = true;                      // original given cells can't be changed
				}
			}
		}
		reader.close();
	}

	// input error checking
	public Boolean canProceed(){
		if ( n != 9 ){
			System.out.println("There's a problem: N != 9");
			return false;
		}

		else if ( p != 3 ){
			System.out.println("There's a problem: p != 3");
			return false;
		}

		else if ( q != 3 ){
			System.out.println("There's a problem: q != 3");
			return false;
		}

		return true;
	}

	// find the block given cell coordinates
	// 0 1 2
	// 3 4 5
	// 6 7 8
	private int findBlock(int i, int j){
		if ( i >= 0 && i < 3 ){
			if ( j >= 0 && j < 3 ){
				return 0;
			}
			else if ( j > 2 && j < 6 ){
				return 1;
			}
			else{
				return 2;
			}
		}
		else if ( i > 2 && i < 6){
			if ( j >= 0 && j <= 2){
				return 3;
			}
			else if ( j > 2 && j < 6 ){
				return 4;
			}
			else{
				return 5;
			}
		}
		else if ( i > 5 && i < 9){
			if ( j >= 0 && j <= 2){
				return 6;
			}
			else if ( j > 2 && j < 6 ){
				return 7;
			}
			else{
				return 8;
			}
		}
		return -1;
	}

	// check if the assigned token doesn't violate the row constraint
	private Boolean checkRow(int row, int value){
		for (int j = 0; j < 9; j++){
			if (grid[row][j] == value){
				return false;
			}
		}
		return true;
	}

	// check if the assigned token will not violate the column constraint
	private Boolean checkCol(int col, int value){
		for (int i = 0; i < 9; i++){
			if (grid[i][col] == value){
				return false;
			}
		}
		return true;
	}

	// check if the assigned token will not violate the block constraint
	private Boolean checkBlock(int block, int value){
		switch(block){
		case 0:
			for (int i = 0; i < 3; i++){
				for (int j = 0; j < 3; j++){
					if (grid[i][j] == value){
						return false;
					}
				}
			}
			return true;
		case 1:
			for (int i = 0; i < 3; i++){
				for (int j = 3; j < 6; j++){
					if (grid[i][j] == value){
						return false;
					}
				}
			}
			return true;
		case 2:
			for (int i = 0; i < 3; i++){
				for (int j = 6; j < 9; j++){
					if (grid[i][j] == value){
						return false;
					}
				}
			}
			return true;
		case 3:
			for (int i = 3; i < 6; i++){
				for (int j = 0; j < 3; j++){
					if (grid[i][j] == value){
						return false;
					}
				}
			}
			return true;
		case 4:
			for (int i = 3; i < 6; i++){
				for (int j = 3; j < 6; j++){
					if (grid[i][j] == value){
						return false;
					}
				}
			}
			return true;
		case 5:
			for (int i = 3; i < 6; i++){
				for (int j = 6; j < 9; j++){
					if (grid[i][j] == value){
						return false;
					}
				}
			}
			return true;
		case 6:
			for (int i = 6; i < 9; i++){
				for (int j = 0; j < 3; j++){
					if (grid[i][j] == value){
						return false;
					}
				}
			}
			return true;
		case 7:
			for (int i = 6; i < 9; i++){
				for (int j = 3; j < 6; j++){
					if (grid[i][j] == value){
						return false;
					}
				}
			}
			return true;
		case 8:
			for (int i = 6; i < 9; i++){
				for (int j = 6; j < 9; j++){
					if (grid[i][j] == value){
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public int getNumberOfAssignments(){
		return assignments;
	}

	//find next available cell:
	//if we got [0][0] that means we were done with the grid
	// nextCell(8,8) = (0,0)

	private int[] findNextCell(int row, int col){
		int[] nextCell = new int[2];

		if ( row <= 8 && col < 8){
			nextCell[0] = row;
			nextCell[1] = col+1;
		}

		if ( row < 8 && col == 8){
			nextCell[0] = row + 1;
			nextCell[1] = 0;
		}

		if (!(nextCell[0] == 0 && nextCell[1] == 0)
				&&	fixedCell[nextCell[0]][nextCell[1]]){
			return findNextCell(nextCell[0],nextCell[1]);
		}
		else if ( row == 8 && col == 8){
			nextCell[0] = 0;
			nextCell[1] = 0;

		}

		return nextCell;
	}



	private void printOutput(long[] a){
		System.out.println("Time: " + a[0]);
		System.out.println("Assignments: " + a[1]);
		System.out.println("Solution: " + (a[2]==0? "No" : "Yes"));
		System.out.println("Timeout: " + (a[3]==0? "No" : "Yes"));
		System.out.println("Search Time: " + a[0]);
	}

	public void solveBT(int i, int j){

		if ( System.nanoTime() >= (Sudoku.timeLimit + totalsolverstart)){

			//time out
			end = System.nanoTime();
			//prepare output
			finalOutput[0] = (long) ((end - totalsolverstart)/(1000000.0)); // in milliseconds
			finalOutput[1] =  getNumberOfAssignments();
			finalOutput[2] = 0; // no solution
			finalOutput[3] = 1; // timeout
			finalOutput[4] = (long) ((end - Sudoku.totalSearchStart)/(1000000.0));
			//print output
			printOutput(finalOutput);
			System.exit(0);
		}
		if (fixedCell[i][j]){
			nextAvailCell = findNextCell(i, j);
			if (nextAvailCell[0] == 0 && nextAvailCell[1] == 0){
				end = System.nanoTime();
				//prepare output
				finalOutput[0] = (long) ((end - totalsolverstart)/(1000000.0)); // in milliseconds
				finalOutput[1] =  getNumberOfAssignments();
				finalOutput[2] = 1; // solution
				finalOutput[3] = 0; // no timeout
				finalOutput[4] = (long) ((end - Sudoku.totalSearchStart)/(1000000.0));
				//print output
				printOutput(finalOutput);
				System.exit(0);
			}
			else{
				solveBT(nextAvailCell[0], nextAvailCell[1]);
			}
		}
		else{

			for (int val = startDomainIndex[i][j] +1; val < 10; val++){
				assignments++;

				if ( checkRow(i, val) && checkCol(j, val) && checkBlock(findBlock(i, j), val)){
					startDomainIndex[i][j] = val;
					grid[i][j] = val;
					nextAvailCell = findNextCell(i, j);
					// ..
					if (nextAvailCell[0] == 0 && nextAvailCell[1] == 0){
						end = System.nanoTime();

						//prepare output
						finalOutput[0] = (long) ((end - totalsolverstart)/(1000000.0)); // in milliseconds
						finalOutput[1] =  getNumberOfAssignments();
						finalOutput[2] = 1;
						finalOutput[3] = 0;
						finalOutput[4] = (long) ((end - Sudoku.totalSearchStart)/(1000000.0));
						//print output
						printOutput(finalOutput);
						System.exit(0);

					}
					else{
						solveBT(nextAvailCell[0], nextAvailCell[1]);
					}
					// ..
					//
					grid[i][j] = 0;
					startDomainIndex[i][j] = 0;
				}
			}

		}
		// backtracked all the way to 0
		if (i ==0 && j ==0 && startDomainIndex[0][0] == 8){
			end = System.nanoTime();
			//prepare output
			finalOutput[0] = (long) ((end - totalsolverstart)/(1000000.0)); // in milliseconds
			finalOutput[1] =  getNumberOfAssignments();
			finalOutput[2] = 0;
			finalOutput[3] = 0;
			finalOutput[4] = (long) ((end - Sudoku.totalSearchStart)/(1000000.0));
			//print output
			printOutput(finalOutput);
			System.exit(0);
		}
	}

	public void solveBTandFC(int i, int j){

		if ( System.nanoTime() >= (Sudoku.timeLimit + totalsolverstart)){
			//time out
			end = System.nanoTime();
			//prepare output
			finalOutput[0] = (long) ((end - totalsolverstart)/(1000000.0)); // in milliseconds
			finalOutput[1] =  getNumberOfAssignments();
			finalOutput[2] = 0; // no solution
			finalOutput[3] = 1; // timeout
			finalOutput[4] = (long) ((end - Sudoku.totalSearchStart)/(1000000.0));
			//print output
			printOutput(finalOutput);
			System.exit(0);
		}
		if (fixedCell[i][j]){
			nextAvailCell = findNextCell(i, j);
			if (nextAvailCell[0] == 0 && nextAvailCell[1] == 0){

				end = System.nanoTime();
				//prepare output
				finalOutput[0] = (long) ((end - totalsolverstart)/(1000000.0)); // in milliseconds
				finalOutput[1] =  getNumberOfAssignments();
				finalOutput[2] = 1; //  solution
				finalOutput[3] = 0; // no timeout
				finalOutput[4] = (long) ((end - Sudoku.totalSearchStart)/(1000000.0));
				//print output
				printOutput(finalOutput);
				System.exit(0);
			}
			else{
				solveBTandFC(nextAvailCell[0], nextAvailCell[1]);
			}
		}
		else{

			for (int ind = startDomainIndex[i][j]; ind < 9; ind++){

				int val = domains.get(i).get(j).get(ind);

				if (val!=0){
					assignments++;
				}

				if ( val != 0 && checkRow(i, val) && checkCol(j, val) && checkBlock(findBlock(i, j), val)){

					startDomainIndex[i][j] = val - 1;
					grid[i][j] = val;
					nextAvailCell = findNextCell(i, j);
					// ..
					removeFromRow(i, j, val);
					removeFromCol(i, j, val);
					removeFromBlock(i, j, val);

					if (nextAvailCell[0] == 0 && nextAvailCell[1] == 0){
						end = System.nanoTime();
						//prepare output
						finalOutput[0] = (long) ((end - totalsolverstart)/(1000000.0)); // in milliseconds
						finalOutput[1] =  getNumberOfAssignments();
						finalOutput[2] = 0; // no solution
						finalOutput[3] = 1; // timeout
						finalOutput[4] = (long) ((end - Sudoku.totalSearchStart)/(1000000.0));
						//print output
						printOutput(finalOutput);
						System.exit(0);
					}
					else{
						solveBTandFC(nextAvailCell[0], nextAvailCell[1]);

					}
					// ..
					//
					grid[i][j] = 0;
					startDomainIndex[i][j] = 0;
					restoreRow(i, j, val);
					restoreCol(i, j, val);
					restoreBlock(i, j, val);
				}
			}

		}
		// backtracked all the way to 0
		if (i ==0 && j ==0 && startDomainIndex[0][0] == 8){
			end = System.nanoTime();
			//prepare output
			finalOutput[0] = (long) ((end - totalsolverstart)/(1000000.0)); // in milliseconds
			finalOutput[1] =  getNumberOfAssignments();
			finalOutput[2] = 0; // no solution
			finalOutput[3] = 0; // no timeout
			finalOutput[4] = (long) ((end - Sudoku.totalSearchStart)/(1000000.0));
			//print output
			printOutput(finalOutput);
			System.exit(0);
		}
	}






	private void removeFromRow(int row, int col, int value){

		for (int j = 0; j < 9; j++ ){

			if (col !=j)
				domains.get(row).get(j).set(value-1, 0);

		}
		/*for (int j = 0; j < col; j++ ){
			domains.get(row).get(j).set(value-1, 0);

		}
		for (int j = col+1; j < 9; j++ ){

			domains.get(row).get(j).set(value-1,0);
		}*/

	}

	private void removeFromCol(int row, int col, int value){
		for (int i = 0; i < 9; i++ ){

			if (row !=i)
				domains.get(i).get(col).set(value-1, 0);

		}
		/*for (int i = 0; i < row; i++ ){

			domains.get(i).get(col).set(value-1,0);

		}
		for (int i = row; i < 9; i++ ){

			domains.get(i).get(col).set(value-1,0);
		}*/
	}

	private void removeFromBlock(int row, int col, int value){
		int block = findBlock(row, col);
		switch(block){
		case 0:
			for (int i = 0; i < 3; i++){
				for (int j = 0; j < 3; j++){
					if ( !( row == i && col == j)){
						//System.out.println("before: ");
						//System.out.println(domains.get(i).get(j));
						domains.get(i).get(j).set(value-1,0);
						//System.out.println("after:");
						//System.out.println(domains.get(i).get(j));
					}
				}
			}
			break;

		case 1:
			for (int i = 0; i < 3; i++){
				for (int j = 3; j < 6; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,0);
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < 3; i++){
				for (int j = 6; j < 9; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,0);
					}
				}
			}
			break;

		case 3:
			for (int i = 3; i < 6; i++){
				for (int j = 0; j < 3; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,0);
					}
				}
			}
			break;

		case 4:
			for (int i = 3; i < 6; i++){
				for (int j = 3; j < 6; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,0);
					}
				}
			}
			break;

		case 5:
			for (int i = 3; i < 6; i++){
				for (int j = 6; j < 9; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,0);
					}
				}
			}
			break;

		case 6:
			for (int i = 6; i < 9; i++){
				for (int j = 0; j < 3; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,0);
					}
				}
			}
			break;

		case 7:
			for (int i = 6; i < 9; i++){
				for (int j = 3; j < 6; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,0);
					}
				}
			}
			break;

		case 8:
			for (int i = 6; i < 9; i++){
				for (int j = 6; j < 9; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,0);
					}
				}
			}

		}

	}

	private void restoreRow(int row, int col, int value){
		for (int j = 0; j < col; j++ ){
			domains.get(row).get(j).set(value-1, value);

		}
		for (int j = col+1; j < 9; j++ ){

			domains.get(row).get(j).set(value-1,value);
		}

	}

	private void restoreCol(int row, int col, int value){

		for (int i = 0; i < row; i++ ){

			domains.get(i).get(col).set(value-1,value);

		}
		for (int i = row; i < 9; i++ ){

			domains.get(i).get(col).set(value-1,value);
		}
	}

	private void restoreBlock(int row, int col, int value){
		int block = findBlock(row, col);
		switch(block){
		case 0:
			for (int i = 0; i < 3; i++){
				for (int j = 0; j < 3; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,value);
					}
				}
			}
			break;

		case 1:
			for (int i = 0; i < 3; i++){
				for (int j = 3; j < 6; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,value);
					}
				}
			}
			break;

		case 2:
			for (int i = 0; i < 3; i++){
				for (int j = 6; j < 9; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,value);
					}
				}
			}
			break;

		case 3:
			for (int i = 3; i < 6; i++){
				for (int j = 0; j < 3; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,value);
					}
				}
			}
			break;

		case 4:
			for (int i = 3; i < 6; i++){
				for (int j = 3; j < 6; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,value);
					}
				}
			}
			break;

		case 5:
			for (int i = 3; i < 6; i++){
				for (int j = 6; j < 9; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,value);
					}
				}
			}
			break;

		case 6:
			for (int i = 6; i < 9; i++){
				for (int j = 0; j < 3; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,value);
					}
				}
			}
			break;

		case 7:
			for (int i = 6; i < 9; i++){
				for (int j = 3; j < 6; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,value);
					}
				}
			}
			break;

		case 8:
			for (int i = 6; i < 9; i++){
				for (int j = 6; j < 9; j++){
					if ( !( row == i && col == j)){
						domains.get(i).get(j).set(value-1,value);
					}
				}
			}

		}

	}


	// output grid
	public void printGrid(){
		for (int i = 0; i < 9; i++){
			for(int j = 0; j <9; j++){
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
	}
}
