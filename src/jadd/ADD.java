package jadd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bridj.Pointer;

import bigcudd.BigcuddLibrary;
import bigcudd.BigcuddLibrary.Cudd_addApply_arg1_callback;
import bigcudd.BigcuddLibrary.DdGen;
import bigcudd.BigcuddLibrary.DdManager;
import bigcudd.DdNode;

/**
 * ADD - constant, variable or function alike.
 * @author thiago
 *
 */
public class ADD {
    private static double FLOATING_POINT_PRECISION = 1E-14;

    private Pointer<DdNode> function;
    private Pointer<DdManager> dd;
    private VariableStore variableStore;

    ADD(Pointer<DdManager> dd, Pointer<DdNode> function, VariableStore variableStore) {
        this.dd = dd;
        this.function = function;
        this.variableStore = variableStore;
        BigcuddLibrary.Cudd_Ref(this.function);
    }

    protected ADD(ADD other) {
        this.dd = other.dd;
        this.function = other.function;
        this.variableStore = other.variableStore;
        BigcuddLibrary.Cudd_Ref(this.function);
    }

    /**
     * Overriding finalize in order to free CUDD allocated memory.
     */
    @Override
    protected void finalize() throws Throwable {
        BigcuddLibrary.Cudd_RecursiveDeref(dd, function);
        super.finalize();
    }
    
    public ADD operator(ADD other, Cudd_addApply_arg1_callback operation) {
    	return apply(other, operation);
    }

    private ADD apply(ADD other, Cudd_addApply_arg1_callback operation) {
        Pointer<DdNode> result = BigcuddLibrary.Cudd_addApply(dd,
                                                              Pointer.getPointer(operation),
                                                              this.function,
                                                              other.function);
        return new ADD(dd, result, variableStore);
    }

    /**
     * @return negated form (corresponding to unary minus).
     */
    public ADD negate() {
        return new ADD(dd,
                       BigcuddLibrary.Cudd_addNegate(dd, this.function),
                       variableStore);
    }

    /**
     * @return complemented form (corresponding to logical not).
     */
    public ADD complement() {
        return new ADD(dd,
                       BigcuddLibrary.Cudd_addCmpl(dd, this.function),
                       variableStore);
    }

    /**
     * Implements if-then-else with the result of this boolean function
     * as the conditional.
     */
    public ADD ifThenElse(ADD ifTrue, ADD ifFalse) {
        Pointer<DdNode> result = BigcuddLibrary.Cudd_addIte(dd,
                                                            this.function,
                                                            ifTrue.function,
                                                            ifFalse.function);
        return new ADD(dd, result, variableStore);
    }

    /**
     * Overloading for constant fallbacks.
     */
    public ADD ifThenElse(ADD ifTrue, double ifFalse) {
        Pointer<DdNode> result = BigcuddLibrary.Cudd_addIte(dd,
                                                            this.function,
                                                            ifTrue.function,
                                                            BigcuddLibrary.Cudd_addConst(dd,
                                                                                         ifFalse));
        return new ADD(dd, result, variableStore);
    }

    public Set<String> getVariables() {
        Set<String> variables = new HashSet<String>();

        Pointer<Integer> variablesPtr = BigcuddLibrary.Cudd_SupportIndex(dd, this.function);
        int numVars = BigcuddLibrary.Cudd_ReadSize(dd);
        int[] variablesPresence = variablesPtr.getInts(numVars);
        for (short i = 0; i < numVars; i++) {
            if (variablesPresence[i] == 1) {
                variables.add(variableStore.getName(i));
            }
        }
        return variables;
    }

    public double eval(String[] variables) throws UnrecognizedVariableException {
        int[] presenceVector = variableStore.toPresenceVector(variables);
        Pointer<DdNode> terminal = BigcuddLibrary.Cudd_Eval(dd,
                                                            function,
                                                            Pointer.pointerToInts(presenceVector));
        DdNode terminalNode = terminal.get();
        return terminalNode.type().value();
    }

    /**
     * Returns all valid configurations and respective values.
     *
     * Variables which may or may not be present are represented parenthesized.
     * @return
     */
    public Map<List<String>, Double> getValidConfigurations() {
        Pointer<Integer> dummy = Pointer.allocateInt();
        // A pointer to a freshly allocated pointer to int.
        // As Cudd_FirstCube and Cudd_NextCube allocate the returned cubes,
        // allocating a whole int[] here makes no sense. Thus, we allocate
        // only the position where the address to the generated cubes are
        // to be stored.
        Pointer<Pointer<Integer>> cubePtr = Pointer.pointerToPointer(dummy);
        // A pointer to a freshly allocated double.
        Pointer<Double> valuePtr = Pointer.pointerToDouble(0);

        Map<List<String>, Double> configurationsAndValues = new HashMap<List<String>, Double>();
        // So let's start the iteration!
        Pointer<DdGen> generator = BigcuddLibrary.Cudd_FirstCube(dd,
                                                                 function,
                                                                 cubePtr,
                                                                 valuePtr);
        int numVars = BigcuddLibrary.Cudd_ReadSize(dd);
        while (BigcuddLibrary.Cudd_IsGenEmpty(generator) == 0) {
            Double value = valuePtr.get();
            Pointer<Integer> cube = cubePtr.getPointer(Integer.class);
            int[] presenceVector = cube.getInts(numVars);
            List<String> configuration = variableStore.fromPresenceVector(presenceVector);

            configurationsAndValues.put(configuration, value);

            BigcuddLibrary.Cudd_NextCube(generator,
                                         cubePtr,
                                         valuePtr);
        }
        BigcuddLibrary.Cudd_GenFree(generator);

        return configurationsAndValues;
    }

    /**
    * Returns the number of internal nodes in this ADD.
    * @return
    */
    public int getNodeCount() {
        return BigcuddLibrary.Cudd_DagSize(function);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        ADD other = (ADD) obj;
        return this.function.equals(other.function)
                || (BigcuddLibrary.Cudd_EqualSupNorm(dd,
                                                     this.function,
                                                     other.function,
                                                     ADD.FLOATING_POINT_PRECISION,
                                                     1) == 1);
    }
    
    public int getDeadNodesCount() {
    	return BigcuddLibrary.Cudd_ReadDead(dd);
    }
    
    public int getTerminalsDifferentThanZeroCount() {
    	return BigcuddLibrary.Cudd_CountLeaves(function) - 1;
    }
    
    public double getPathsToNonZeroTerminalsCount() {
    	return BigcuddLibrary.Cudd_CountPathsToNonZero(function);
    }
    
    public double getPathsToZeroTerminalCount() {
    	return BigcuddLibrary.Cudd_CountPath(function) - getPathsToNonZeroTerminalsCount();
    }
    
    public int getReorderingsCount() {
    	return BigcuddLibrary.Cudd_ReadReorderings(dd);
    }
    
    public int getGarbageCollectionsCount() {
    	return BigcuddLibrary.Cudd_ReadGarbageCollections(dd);
    }
    
    public long getAddSizeInBytes() {
    	return BigcuddLibrary.Cudd_ReadMemoryInUse(dd); 
    }

    @Override
    public int hashCode() {
        return this.function.hashCode();
    }

    Pointer<DdNode> getUnderlyingNode() {
        return this.function;
    }

    /**************************************************************
     *** Operators definitions
     *************************************************************/

    public static final BigcuddLibrary.Cudd_addApply_arg1_callback TIMES = new BigcuddLibrary.Cudd_addApply_arg1_callback() {
        @Override
        public Pointer<DdNode > apply(Pointer<BigcuddLibrary.DdManager > dd,
                                      Pointer<Pointer<DdNode > > node1,
                                      Pointer<Pointer<DdNode > > node2) {
            return BigcuddLibrary.Cudd_addTimes(dd, node1, node2);
        }
    };

    public static final BigcuddLibrary.Cudd_addApply_arg1_callback PLUS = new BigcuddLibrary.Cudd_addApply_arg1_callback() {
        @Override
        public Pointer<DdNode > apply(Pointer<BigcuddLibrary.DdManager > dd,
                                      Pointer<Pointer<DdNode > > node1,
                                      Pointer<Pointer<DdNode > > node2) {
            return BigcuddLibrary.Cudd_addPlus(dd, node1, node2);
        }
    };

    public static final BigcuddLibrary.Cudd_addApply_arg1_callback DIVIDE = new BigcuddLibrary.Cudd_addApply_arg1_callback() {
        @Override
        public Pointer<DdNode > apply(Pointer<BigcuddLibrary.DdManager > dd,
                                      Pointer<Pointer<DdNode > > node1,
                                      Pointer<Pointer<DdNode > > node2) {
            return BigcuddLibrary.Cudd_addDivide(dd, node1, node2);
        }
    };

    public static final BigcuddLibrary.Cudd_addApply_arg1_callback MINUS = new BigcuddLibrary.Cudd_addApply_arg1_callback() {
        @Override
        public Pointer<DdNode > apply(Pointer<BigcuddLibrary.DdManager > dd,
                                      Pointer<Pointer<DdNode > > node1,
                                      Pointer<Pointer<DdNode > > node2) {
            return BigcuddLibrary.Cudd_addMinus(dd, node1, node2);
        }
    };

    public static final BigcuddLibrary.Cudd_addApply_arg1_callback LOGICAL_OR = new BigcuddLibrary.Cudd_addApply_arg1_callback() {
        @Override
        public Pointer<DdNode > apply(Pointer<BigcuddLibrary.DdManager > dd,
                                      Pointer<Pointer<DdNode > > node1,
                                      Pointer<Pointer<DdNode > > node2) {
            return BigcuddLibrary.Cudd_addOr(dd, node1, node2);
        }
    };

}
