import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;



public class Sudoku {

	public static long timeLimit = 60000000000l; // 1 minute

	public static long totalSearchStart = 0;
	public static void startGenerator() throws IOException{
		System.out.println("starting generator...");
		Generator gen = new Generator();
		InputStream input = new FileInputStream("input.txt");
		OutputStream output = new FileOutputStream("output.txt");

		gen.generate(input, output);
		System.out.println("done... check the output file to see the generated puzzle!");

	}

	public static void startSolver(long timeout, int h) throws IOException{
		System.out.println("starting solver...");

		Solver solver = new Solver(System.nanoTime());

		//uses a generated puzzle as input
		Generator gen = new Generator();
		InputStream input = new FileInputStream("input.txt");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		gen.generate(input, output);
		// convert output stream to input stream
		InputStream generated = new ByteArrayInputStream(output.toByteArray());

		solver.readInput(generated);

		if (solver.canProceed()){

		if (h == 1){
			totalSearchStart = System.nanoTime();
			solver.solveBT(0, 0);
		}
		else if (h == 2){
			totalSearchStart = System.nanoTime();
			solver.solveBTandFC(0, 0);
		}
		}
	}


	public static void main(String[] args) throws IOException {
		String str;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


		System.out.println("Enter '1' for Generator");
		System.out.println("Enter '2' for Solver");
		System.out.print("[1]Generator or [2]Solver: ");


		try {
			str = input.readLine();
			int x = Integer.parseInt(str);
			while (x != 1 && x != 2){
				System.out.println("Please Enter a Valid Number:");
				x = Integer.parseInt(input.readLine());
			}
			//Generator
			if (x==1){
				startGenerator();
			}
			//Solver
			if (x==2){

				System.out.print("Enter a time limit (or hit 'Enter' for default time limit[1 min.]): ");
				String userInput = input.readLine();
				if (!userInput.equals(""))
					//update timeLimit
					timeLimit = (Long.parseLong(userInput)) * 60 * 1000 * 1000 * 1000; //in nanoseconds



				System.out.println("Enter '1' for BT");
				System.out.println("Enter '2' for BT+FC");
				System.out.print("[1]BT or [2]BT+FC: ");

				try {
					str = input.readLine();
					int h = Integer.parseInt(str);
					while (x != 1 && x != 2){
						System.out.println("Please Enter a Valid Number:");
						h = Integer.parseInt(input.readLine());
					}

					startSolver(timeLimit,h);

				} catch (IOException e) {		}


			}
		} catch (IOException e) {		}
	}

}
