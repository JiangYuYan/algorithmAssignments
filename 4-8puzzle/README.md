# A*Search for 8-Puzzle

## 8-Puzzle Problem

The goal of 8-Puzzle problem is to rearrange the tiles from initial boart(left) to goal board(right) using as few moves as possible.

![](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/4moves.png)



## A* Search Algorithm

### Search Node

- The ***current board***

- The number of ***moves*** made from initial board to the current board
- The ***previous*** (parent) search node

- The ***priority*** (***distances*** + ***moves***)

### Priority

- Distances
  - The ***Hamming distance*** between a board and the goal board is the number of tiles in the wrong position. 
  - The ***Manhattan distance*** between a board and the goal board is the sum of the Manhattan distances (sum of the vertical and horizontal distance) from the tiles to their goal positions.

- Priority 
  - The ***Hamming priority*** is the Hamming distance of a board plus the number of moves made so far to get to the search node.
  - The ***Manhattan priority*** is the Manhattan distance of a board plus the number of moves made so far to get to the search node.

![](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/hamming-manhattan.png)

### Algorithm

- Create a ***priority queue*** of search node.

- Insert the initial search node (the initial board, 0 moves, a null previous search node) into the priority queue.
- ***Delete parent***, the search node with the minimum priority.
- ***Insert children***, all feasible neighboring search nodes (can be reached in one move and different from the parent of the dequeued search node).
- Repeat step 3-4 until the search node with a goal board dequeued.

![](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/game-tree.png)

### Detecting unsolvable boards

 Boards can be divided into two equivalence classes with respect to reachability:

- Those that can lead to the goal board
- Those that can lead to the goal board if we modify the initial board by swapping any pair of tiles (the blank square is not a tile).

The ***Solution*** is to run the A* algorithm on ***two*** puzzle instances:

- one with the initial board
- one with the initial board modified by swapping a pair of tiles

