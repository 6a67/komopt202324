# instance file
param file := "../instances/facility1.txt";

# customers
param n := read file as "1n" skip 1 use 1;
set N := {1..n};
do print N;

# facilities
param m := read file as "2n" skip 1 use 1;
set S := {1..m};
do print S;

# customer demands
param b[N] := read file as "n+" skip 3 use 1;
do forall <j> in N do print b[j];

# production capacities
param K[S] := read file as "n+" skip 5 use 1;
do forall <i> in S do print K[i];

# fix costs
param F[S] := read file as "n+" skip 7 use 1;
do forall <i> in S do print F[i];

# cost matrix for delivery costs
param c[S*N] := read file as "n+" use m skip 9;
do forall <j> in N do forall <i> in S do print c[i,j];

# TODO: create variables, setup objective function and constraints

