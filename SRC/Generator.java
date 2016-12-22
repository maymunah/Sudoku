import java.io.BufferedReader;
import java.io.EOFException;

import java.io.File;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;




public class Generator {

	private int n;
	private int p;
	private int q;
	private int m;

	private int[][] grid;

	private ArrayList<ArrayList<ArrayList<Integer>>> domains = new ArrayList<ArrayList<ArrayList<Integer>>>();

	public Generator(){
		n = 9;
		p = 3;
		q = 3;
		m = 0;
		grid = new int[9][9];

		// domain = [1,2,3,4,5,6,7,8,9]
		ArrayList<Integer> d = new ArrayList<>();
		for (int i = 1; i < 10; i++){
			d.add(i);
		}

		// initialize cell domains with {1,2,3,4,5,6,7,8,9}
		for (int i = 0; i < 9; i++){
			domains.add(i, new ArrayList<ArrayList<Integer>>());
			for (int j =0; j < 9; j++){
				domains.get(i).add(j, new ArrayList<Integer>());
				domains.get(i).get(j).addAll(d);

			}
		}
	}

	public void generate(InputStream input, OutputStream output) throws IOException{

		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String inputLine = reader.readLine();
		StringTokenizer st = new StringTokenizer(inputLine," ");
		n = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());


		if (canProceed()){    // after input error checking

			fillMcells();

		}

		// writing content to output file:
		// FileWriter writeOutput = new FileWriter(output);
		OutputStreamWriter writeOutput = new OutputStreamWriter(output);
		writeOutput.write(""+n+" "+p+" "+q+"\n");

		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				writeOutput.write("" +grid[i][j]+" ");
			}
			writeOutput.write("\n");
		}

		// after finishing with the files
		writeOutput.flush();
		writeOutput.close();

		reader.close();
	}


	private Boolean canProceed(){

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

		else if ( m < 0 || m > 81){
			System.out.println("There's a problem: M is not valid!");
			return false;
		}
		return true;
	}
	// find the block given cell coordinates
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

	// fill the grid with m tokens
	private void fillMcells(){
		int i = 0;
		Random gen = new Random();
		int randomCell;// = gen.nextInt(82);
		int randomToken;
		int radomDomainIndex;

		while ( i < m){


			randomCell = gen.nextInt(81);


			int iRandCell = ((randomCell / 9));//-1)>=0?randomCell / 9-1:randomCell / 9 ;
			int jRandCell = ((randomCell % 9));//-1)>=0?randomCell % 9-1:randomCell % 9 ;
			int bRandCell = findBlock(iRandCell,jRandCell);




			if ( grid[iRandCell][jRandCell] == 0 && !domains.get(iRandCell).get(jRandCell).isEmpty() ){
				radomDomainIndex = gen.nextInt(domains.get(iRandCell).get(jRandCell).size());
				randomToken = domains.get(iRandCell).get(jRandCell).get(radomDomainIndex);
				if (checkRow(iRandCell, randomToken)
						&& checkCol(jRandCell, randomToken)
						&& checkBlock(bRandCell, randomToken) ){

					grid[iRandCell][jRandCell] = randomToken;
					i++;
				}
				else{
					domains.get(iRandCell).get(jRandCell).remove(radomDomainIndex);
					//System.out.println(randomCell + " "+domains.get(iRandCell).get(jRandCell).remove(randomToken));
					if (domains.get(iRandCell).get(jRandCell).isEmpty()){
						//restart
						resetGrid();
						fillMcells();
					}
				}
			}
		}
	}

	private void resetGrid(){
		grid = new int[9][9];
	}
}
