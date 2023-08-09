param file := "../instances/fluss1.txt";

# read data from input file
param n := read file as "1n" skip 1 use 1;
set V := {1..n};
do print V;

# edges
set A := {<i,j> in V*V};
do print A;

# read upper bounds
param u[A] := read file as "n+" use n skip 3;
do forall <i,j> in A do print u[i,j];

# read costs
param c[A] := read file as "n+" use n skip 4+n;
do forall <i,j> in A do print c[i,j];

# read suppy/demand
param b[V] := read file as "n+" skip 5+2*n;
do forall <i> in V do print b[i];

# TODO: setup objective function and the constraints
