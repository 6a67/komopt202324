# binary knapsack problem solver using zimpl

# parse number of elements n
param n := read file as "1n" use 1 comment "#";
do print n;

# prase max weight W
param W := read file as "1n" skip n+1 use 1 comment "#";
do print W;

# parse tuples
set indices := {1..n};
param c[<i> in indices] := read file as "1n" skip 1 use n comment "#";
param w[<i> in indices] := read file as "2n" skip 1 use n comment "#";
do forall <i> in indices do print <i,c[i],w[i]>;

# decision variables if item i is selected
var x[indices] binary;

# objective function
maximize values: sum <i> in indices: c[i] * x[i];

# constraints
subto maxweight: sum <i> in indices: w[i] * x[i] <= W;

