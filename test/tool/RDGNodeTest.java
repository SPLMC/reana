package tool;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fdtmc.FDTMC;

public class RDGNodeTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testDependenciesTransitiveClosureContainsOwnNode() throws CyclicRdgException {
        RDGNode sqlite = BSNNodes.getSQLiteRDGNode();
        List<RDGNode> dependencies = sqlite.getDependenciesTransitiveClosure();

        Assert.assertEquals(1, dependencies.size());
        Assert.assertTrue(dependencies.contains(sqlite));
    }

    @Test
    public void testGetDependenciesTransitiveClosure() throws CyclicRdgException {
        RDGNode situation = BSNNodes.getSituationRDGNode();
        RDGNode pulseRate = BSNNodes.getPulseRateRDGNode();
        RDGNode sqlite = BSNNodes.getSQLiteRDGNode();
        RDGNode memory = BSNNodes.getMemoryRDGNode();
        RDGNode file = BSNNodes.getFileRDGNode();

        assertSituationDependenciesIndex(sqlite, pulseRate);
        assertSituationDependenciesIndex(memory, pulseRate);
        assertSituationDependenciesIndex(file, pulseRate);

        assertSituationDependenciesIndex(sqlite, situation);
        assertSituationDependenciesIndex(memory, situation);
        assertSituationDependenciesIndex(file, situation);

        assertSituationDependenciesIndex(pulseRate, situation);
    }

    public void assertSituationDependenciesIndex(RDGNode nodeA, RDGNode nodeB) throws CyclicRdgException{
        RDGNode situation = BSNNodes.getSituationRDGNode();
        List<RDGNode> situationDependencies = situation.getDependenciesTransitiveClosure();
        Assert.assertTrue(situationDependencies.indexOf(nodeA) < situationDependencies.indexOf(nodeB));
    }

    @Test
    public void testGetNumberOfPaths() throws CyclicRdgException {
        RDGNode situation = BSNNodes.getSituationRDGNode();
        RDGNode pulseRate = BSNNodes.getPulseRateRDGNode();
        RDGNode sqlite = BSNNodes.getSQLiteRDGNode();
        RDGNode memory = BSNNodes.getMemoryRDGNode();
        RDGNode file = BSNNodes.getFileRDGNode();
        RDGNode oxygenation = BSNNodes.getOxygenationRDGNode();

        assertNumberOfPaths(2, sqlite);
        assertNumberOfPaths(2, memory);
        assertNumberOfPaths(2, file);
        assertNumberOfPaths(1, oxygenation);
        assertNumberOfPaths(1, pulseRate);
        assertNumberOfPaths(1, situation);
    }

    public void assertNumberOfPaths(int number, RDGNode node) throws CyclicRdgException{
        RDGNode situation = BSNNodes.getSituationRDGNode();
        Map<RDGNode, Integer> numberOfPaths = situation.getNumberOfPaths();
        Assert.assertEquals(number, numberOfPaths.get(node).intValue());
    }

    @Test
    public void testNodesWithSameFDTMC() {
        FDTMC sqliteFDTMC = FDTMCStub.createSqliteFDTMC();
        String presenceCondition = "SQLite";
        RDGNode firstSqlite = new RDGNode(RDGNode.getNextId(), presenceCondition, sqliteFDTMC);
        RDGNode secondSqlite = new RDGNode(RDGNode.getNextId(), presenceCondition, sqliteFDTMC);

        Assert.assertEquals("Nodes with same FDTMC and presence condition and no dependencies should be equal",
                firstSqlite, secondSqlite);
    }

    @Test
    public void testNodesWithSameFDTMCAndDifferentDependencies() {
        FDTMC pulseRateFDTMC = FDTMCStub.createPulseRateFDTMC();
        String presenceCondition = "SQLite";
        RDGNode first = new RDGNode(RDGNode.getNextId(), presenceCondition, pulseRateFDTMC);
        first.addDependency(BSNNodes.getMemoryRDGNode());
        RDGNode second = new RDGNode(RDGNode.getNextId(), presenceCondition, pulseRateFDTMC);
        second.addDependency(BSNNodes.getSQLiteRDGNode());

        Assert.assertNotEquals("Nodes with same FDTMC and presence condition and different dependencies should NOT be equal",
                first, second);

        second.addDependency(BSNNodes.getMemoryRDGNode());
        first.addDependency(BSNNodes.getSQLiteRDGNode());
        Assert.assertEquals("Nodes with same FDTMC, presence condition and dependencies should be equal",
                first, second);
    }

    @Test
    public void testSimilarNodes() {
        FDTMC pulseRateFDTMC = FDTMCStub.createPulseRateFDTMC();
        String presenceCondition = "SQLite";
        RDGNode first = new RDGNode(RDGNode.getNextId(), presenceCondition, pulseRateFDTMC);
        first.addDependency(BSNNodes.getMemoryRDGNode());
        RDGNode second = new RDGNode(RDGNode.getNextId(), presenceCondition, pulseRateFDTMC);
        second.addDependency(BSNNodes.getSQLiteRDGNode());

        RDGNode similarCandidate = RDGNode.getSimilarNode(second);
        Assert.assertNull("Nodes with same FDTMC and presence condition and different dependencies should NOT be equal",
                similarCandidate);

        second.addDependency(BSNNodes.getMemoryRDGNode());
        first.addDependency(BSNNodes.getSQLiteRDGNode());

        similarCandidate = RDGNode.getSimilarNode(second);
        Assert.assertEquals("Nodes with same FDTMC, presence condition and dependencies should be equal",
                first, similarCandidate);
    }

}
