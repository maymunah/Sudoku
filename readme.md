## The steps needed to compile your Generator and Solver:
1. In Eclipse:
2. File -> New -> Java Project
3. Uncheck ‘Use Default Location’
4. Click ‘Browse’ and select my ‘AI’ folder, then click on ‘Ok’ then ‘Finish’

### Note: For the solver:
“input.txt” and “output.txt” are located inside AI . My solver uses the output file of the generator ( a random generated puzzle ) as the input file.
If you want to use your own input file for the solver:
- replace it with the original “output.txt”
- in Sudoku.java: comment out lines: 29, 31, 33
  - so instead of:
```java
//uses a generated puzzle as input
Generator gen = new Generator();
File output = new File("output.txt");
File input = new File("input.txt");
gen.generate(input, output);
solver.readInputFile(output);
```
we would have:
```java
//Generator gen = new Generator();
File output = new File("output.txt");
//File input = new File("input.txt");
//gen.generate(input, output);
solver.readInputFile(output);
```

## The steps needed to run the Generator and Solver:
1. Right click on AI in the ‘package explorer’ section
2. Run As -> Java Application
3. Answer the questions in Console to select which program to run.

### How to turn on and off the various heuristics and methods:
After choosing the solver (by answering the question in Console section):
answer the new question to select a heuristic
(you can enter a time limit or keep the default one)

## Architecture
I divided the problem into three classes: Generator, Solver, and Sudoku
1. Generator:
generates a random standard Sudoku. It takes two file parameters (matching the
given specifications)
2. Solver:
attempts solving a given standard Sudoku. It gets data from an input file.
It uses: Backtracking and Bactracking+ForwardChecking 
