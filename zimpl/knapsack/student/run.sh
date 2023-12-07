#!/bin/bash
PROBLEM_PATH="../instances/"
ZIMPL_PATH="../../../../zimpl-3.3.1.linux.x86_64.gnu.static.opt"
KNAPSACK_PATH="knapsack.zpl"
LP_FILE="knapsack.lp"

VERBOSE=0

for file in $(find $PROBLEM_PATH -name "*.txt"); do
    echo "Solving $file"
    $ZIMPL_PATH $KNAPSACK_PATH -Dfile=$(realpath $file) > /dev/null
    output=$(scip -f $LP_FILE)
    if [ $VERBOSE -eq 1 ]; then
            parsed_output=$(echo "$output" | grep -A 100 "primal solution (original space):" | sed -n '/primal solution (original space):/,/Statistics/p' | grep -v "Statistics")
            echo -e "$parsed_output\n"
    fi
    if  [ $VERBOSE -eq 0 ]; then
        objective_value=$(echo "$output" | grep "objective value:")
        echo "$objective_value"
    fi
done

