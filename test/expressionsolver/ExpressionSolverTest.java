package expressionsolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import jadd.ADD;
import jadd.JADD;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import expressionsolver.ExpressionSolver;

public class ExpressionSolverTest {
    private JADD jadd;
    private ExpressionSolver solver;

    @Before
    public void setUp() throws Exception {
        jadd = new JADD();
        solver = new ExpressionSolver(jadd);
    }

    @Test
    /**
     * This is an example of real use of the API.
     */
    public void testExpressionWithPresenceConditions() {
        ADD presenceCondition = solver.encodeFormula("(sqlite && !memory) || (!sqlite && memory)");

        HashMap<String, ADD> interpretations = new HashMap<String, ADD>();
        interpretations.put("rSqlite",
                            presenceCondition.ifThenElse(jadd.makeConstant(0.5),
                                                         1));
        interpretations.put("rMemory",
                            jadd.makeConstant(0.2));

        ADD result = solver.solveExpression("0.99*rSqlite - 0.5*rMemory",
                                            interpretations);

        ADD expected = jadd.makeConstant(0.99).operator(
                            presenceCondition.ifThenElse(jadd.makeConstant(0.5),
                                                         1), ADD.TIMES)
                        .operator(jadd.makeConstant(0.5).operator(jadd.makeConstant(0.2), ADD.TIMES), ADD.MINUS);

        assertEquals("Expression evaluation must honor provided interpretations",
                expected, result);

//        jadd.dumpDot("FM", presenceCondition, "out.dot");
        jadd.dumpDot(new String[]{"Result", "PresenceCondition"},
                     new ADD[]{result, presenceCondition},
                     "out.dot");
    }

    @Test
    public void testSum() {
        ADD result = solver.solveExpression("0.42 + 0.53");
        assertEquals("Sum of constants should yield an ADD for the result",
                jadd.makeConstant(0.95), result);
    }

    @Test
    public void testProduct() {
        ADD result = solver.solveExpression("0.42 * 0.5");
        assertEquals("Product of constants should yield an ADD for the result",
                jadd.makeConstant(0.21), result);
    }

    @Test
    public void testSubtraction() {
        ADD result = solver.solveExpression("0.4 - 0.8");
        assertEquals("Subtraction of constants should yield an ADD for the result",
                jadd.makeConstant(-0.4), result);
    }

    @Test
    public void testDivision() {
        ADD result = solver.solveExpression("0.4 / 0.2");
        assertEquals("Division of constants should yield an ADD for the result",
                jadd.makeConstant(2), result);
    }

    @Test
    public void testUnaryMinus() {
        ADD result = solver.solveExpression("-0.4");
        assertEquals("Negated constants should yield an ADD for the result",
                jadd.makeConstant(-0.4), result);
    }

    @Test
    public void testConjunction() {
        ADD result = solver.encodeFormula("0 && 1");
        assertEquals("Conjunction of constants should yield an ADD for the result",
                jadd.makeConstant(0), result);

        result = solver.encodeFormula("1 && 1");
        assertEquals("Conjunction of constants should yield an ADD for the result",
                jadd.makeConstant(1), result);
    }

    @Test
    public void testDisjunction() {
        ADD result = solver.encodeFormula("0 || 0");
        assertEquals("Disjunction of constants should yield an ADD for the result",
                jadd.makeConstant(0), result);

        result = solver.encodeFormula("1 || 0");
        assertEquals("Disjunction of constants should yield an ADD for the result",
                jadd.makeConstant(1), result);
    }

    @Test
    public void testComplement() {
        ADD result = solver.encodeFormula("!1");
        assertEquals("Complement of constants should yield an ADD for the result",
                jadd.makeConstant(0), result);

        result = solver.encodeFormula("!0");
        assertEquals("Complement of constants should yield an ADD for the result",
                jadd.makeConstant(1), result);
    }

    /**
     * We defined boolean constants "true" and "false" for better reading.
     */
    @Test
    public void testBooleanConstants() {
        ADD result = solver.encodeFormula("true && false");
        assertEquals("Aliased constants should yield an ADD for the result",
                jadd.makeConstant(0), result);

        result = solver.encodeFormula("true && !false");
        assertEquals("Complement of aliased constants should yield an ADD for the result",
                jadd.makeConstant(1), result);

        result = solver.encodeFormula("True && !False");
        assertEquals("Capitalized aliased constants can also be used",
                jadd.makeConstant(1), result);
    }

    @Test
    public void testFormula() {
        ADD encoded = solver.encodeFormula("(sqlite && !memory) || (!sqlite && memory)");

        ADD sqlite = jadd.getVariable("sqlite");
        ADD memory = jadd.getVariable("memory");
        ADD expected = sqlite.operator(memory.complement(), ADD.TIMES)
                    .operator(sqlite.complement().operator(memory, ADD.TIMES), ADD.LOGICAL_OR);

        assertEquals("Sum of constants should yield an ADD for the result",
                expected, encoded);
    }

    @Test
    public void testExpressionWithVariablesWithoutInterpretation() {
        ADD result = solver.solveExpression("0.99*rSqlite - 0.5*rMemory");

        assertNull("Expressions with variables must have interpretations for them",
                    result);
    }

    @Test
    public void testExpressionWithVariables() {
        HashMap<String, ADD> interpretations = new HashMap<String, ADD>();
        interpretations.put("rSqlite", jadd.makeConstant(0.5));
        interpretations.put("rMemory", jadd.makeConstant(0.2));
        ADD result = solver.solveExpression("0.99*rSqlite - 0.5*rMemory",
                                            interpretations);

        assertEquals("Expression evaluation must honor provided interpretations",
                jadd.makeConstant(0.395), result);
    }

    @Test
    public void testExpressionWithVariablesAndMultipleValues() {
        ADD dummyVar = jadd.getVariable("dummy");

        HashMap<String, ADD> interpretations = new HashMap<String, ADD>();
        interpretations.put("rSqlite", dummyVar.operator(jadd.makeConstant(0.5), ADD.TIMES));
        interpretations.put("rMemory", jadd.makeConstant(0.2));

        ADD result = solver.solveExpression("0.99*rSqlite - 0.5*rMemory",
                                            interpretations);

        ADD expected = jadd.makeConstant(0.99).operator(dummyVar.operator(jadd.makeConstant(0.5), ADD.TIMES), ADD.TIMES)
                        .operator(jadd.makeConstant(0.5).operator(jadd.makeConstant(0.2), ADD.TIMES), ADD.MINUS);

        assertEquals("Expression evaluation must honor provided interpretations",
                expected, result);
    }

    @Test
    public void testVariablesQuerying() {
        ADD formula = solver.encodeFormula("(sqlite && !memory) || (!sqlite && memory)");
        assertEquals(new HashSet<String>(Arrays.asList("sqlite", "memory")),
                     formula.getVariables());

        ADD dummyVar = jadd.getVariable("dummy");
        ADD dummy = dummyVar.operator(jadd.makeConstant(0.9), ADD.TIMES);
        assertEquals(new HashSet<String>(Arrays.asList("dummy")),
                     dummy.getVariables());

        ADD product = formula.operator(dummy, ADD.TIMES);
        assertEquals(new HashSet<String>(Arrays.asList("sqlite", "memory", "dummy")),
                     product.getVariables());
    }

}
