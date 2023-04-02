# 25x25 Sudoku Solver

[Research Paper](https://www.overleaf.com/read/dvstfhsmkmzn)

To compile:
`javac *.java`

To run:
`java Tester [num_boards] [n] [difficulty]`
- num_boards: the number of sudoku puzzles you want to test
- n: the side length of each sudoku board (must be a perfect square, program only intended for up to n = 25)
- difficulty: a number between 0 to 1 that changes the difficulty. The number corresponds to the percentage of empty cells in the puzzle, i.e. a difficulty of 0 generates completely solved puzzles and a difficulty of 1 produces completely empty grids.
