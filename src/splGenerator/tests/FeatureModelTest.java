package splGenerator.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import splGenerator.Feature;
import splGenerator.FeatureModel;

public class FeatureModelTest {

	String fmName;   
	FeatureModel fm; 
	
	
	/**
	 * Setup method... Create the simplest feature model possible. 
	 */
	@Before
	public void setUp() {
		fmName  = "Feature model 1";
		fm = FeatureModel.createFeatureModel(fmName);
	}
	
	
	@Test
	/**
	 * Create a single feature model means the construction of the simplest
	 * Feature Model, i.e., a feature Model with a root feature. The Feature
	 * Model is created by calling a factory method. 
	 */
	public void testCreateSingleFeatureModel() {
		assertNotNull(fm);
		assertEquals("splSimulator.FeatureModel", fm.getClass().getName());
		assertEquals("Feature model 1", fm.getName());
		assertNotNull(fm.getRoot());
		assertEquals(0, fm.getCrossTreeConstraints().size(), 0 );
	}
	
	
	@Test
	/**
	 * Create a single mandatory feature, which parent feature is the root. 
	 */
	public void testCreateMandatoryFeature() {
		Feature root = fm.getRoot();
		Feature f = root.addChild("f1", Feature.LEAF, Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		
		assertEquals(1, root.getChildren().size(), 0);
		f = root.getChildren().iterator().next(); 
		assertNotNull(f);
		assertEquals("splSimulator.Feature", f.getClass().getName());
		assertEquals("f1", f.getName());
		assertEquals(Feature.LEAF, f.getType(), 0);
		assertTrue(f.isMandatory());
		assertTrue(!f.isAbstract());
		assertTrue(!f.isHidden());
	}
	
	
	/**
	 * Create an abstract feature, which parent is the root.
	 */
	@Test 
	public void testCreateAbstractFeature() {
		Feature root = fm.getRoot(); 
		Feature f = root.addChild("f1", Feature.LEAF, !Feature.MANDATORY, 
				Feature.ABSTRACT, !Feature.HIDDEN);
		
		assertEquals(1, root.getChildren().size(), 0);
		f = root.getChildren().iterator().next(); 
		assertNotNull(f);
		assertEquals("splSimulator.Feature", f.getClass().getName());
		assertEquals("f1", f.getName());
		assertEquals(Feature.LEAF, f.getType(), 0);
		assertTrue(!f.isMandatory());
		assertTrue(f.isAbstract());
		assertTrue(!f.isHidden());
	}
	
	
	@Test
	public void testCreateHiddenFeature() { 
		Feature root = fm.getRoot(); 
		Feature f = root.addChild("f1", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, Feature.HIDDEN);
		
		assertEquals(1, root.getChildren().size(), 0);
		f = root.getChildren().iterator().next(); 
		assertNotNull(f);
		assertEquals("splSimulator.Feature", f.getClass().getName());
		assertEquals("f1", f.getName());
		assertEquals(Feature.LEAF, f.getType(), 0);
		assertTrue(!f.isMandatory());
		assertTrue(!f.isAbstract());
		assertTrue(f.isHidden());
	}
	
	
	@Test 
	public void testCreateOrFeature() {
		Feature root = fm.getRoot(); 
		Feature f = root.addChild("f1", Feature.OR, Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature g1 = f.addChild("g1", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature g2 = f.addChild("g2", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		
		
		assertEquals(1, root.getChildren().size(), 0);
		f = root.getChildren().iterator().next(); 
		assertNotNull(f);
		assertEquals("splSimulator.Feature", f.getClass().getName());
		assertEquals("f1", f.getName());
		assertEquals(Feature.OR, f.getType(), 0);
		assertTrue(f.isMandatory());
		assertTrue(!f.isAbstract());
		assertTrue(!f.isHidden());
		
		g1 = f.getChildrenByName("g1");
		assertNotNull(g1);
		assertEquals("splSimulator.Feature", g1.getClass().getName());
		assertEquals("g1", g1.getName());
		assertEquals(Feature.LEAF, g1.getType(), 0);
		assertTrue(!g1.isMandatory());
		assertTrue(!g1.isAbstract());
		assertTrue(!g1.isHidden());
		
		g2 = f.getChildrenByName("g2");
		assertNotNull(g2);
		assertEquals("splSimulator.Feature", g2.getClass().getName());
		assertEquals("g2", g2.getName());
		assertEquals(Feature.LEAF, g2.getType(), 0);
		assertTrue(!g2.isMandatory());
		assertTrue(!g2.isAbstract());
		assertTrue(!g2.isHidden());
	}
	
	
	@Test
	public void testCreateAndFeature() {
		Feature root = fm.getRoot(); 
		Feature f = root.addChild("f1", Feature.AND, Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature g1 = f.addChild("g1", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature g2 = f.addChild("g2", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		
		
		assertEquals(1, root.getChildren().size(), 0);
		f = root.getChildren().iterator().next(); 
		assertNotNull(f);
		assertEquals("splSimulator.Feature", f.getClass().getName());
		assertEquals("f1", f.getName());
		assertEquals(Feature.AND, f.getType(), 0);
		assertTrue(f.isMandatory());
		assertTrue(!f.isAbstract());
		assertTrue(!f.isHidden());
		
		g1 = f.getChildrenByName("g1");
		assertNotNull(g1);
		assertEquals("splSimulator.Feature", g1.getClass().getName());
		assertEquals("g1", g1.getName());
		assertEquals(Feature.LEAF, g1.getType(), 0);
		assertTrue(!g1.isMandatory());
		assertTrue(!g1.isAbstract());
		assertTrue(!g1.isHidden());
		
		g2 = f.getChildrenByName("g2");
		assertNotNull(g2);
		assertEquals("splSimulator.Feature", g2.getClass().getName());
		assertEquals("g2", g2.getName());
		assertEquals(Feature.LEAF, g2.getType(), 0);
		assertTrue(!g2.isMandatory());
		assertTrue(!g2.isAbstract());
		assertTrue(!g2.isHidden());
	}
	
	
	@Test
	public void testCreateAlternativeFeature() {
		Feature root = fm.getRoot(); 
		Feature f = root.addChild("f1", Feature.ALTERNATIVE, Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature g1 = f.addChild("g1", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature g2 = f.addChild("g2", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		
		
		assertEquals(1, root.getChildren().size(), 0);
		f = root.getChildren().iterator().next(); 
		assertNotNull(f);
		assertEquals("splSimulator.Feature", f.getClass().getName());
		assertEquals("f1", f.getName());
		assertEquals(Feature.ALTERNATIVE, f.getType(), 0);
		assertTrue(f.isMandatory());
		assertTrue(!f.isAbstract());
		assertTrue(!f.isHidden());
		
		g1 = f.getChildrenByName("g1");
		assertNotNull(g1);
		assertEquals("splSimulator.Feature", g1.getClass().getName());
		assertEquals("g1", g1.getName());
		assertEquals(Feature.LEAF, g1.getType(), 0);
		assertTrue(!g1.isMandatory());
		assertTrue(!g1.isAbstract());
		assertTrue(!g1.isHidden());
		
		g2 = f.getChildrenByName("g2");
		assertNotNull(g2);
		assertEquals("splSimulator.Feature", g2.getClass().getName());
		assertEquals("g2", g2.getName());
		assertEquals(Feature.LEAF, g2.getType(), 0);
		assertTrue(!g2.isMandatory());
		assertTrue(!g2.isAbstract());
		assertTrue(!g2.isHidden());
	}
	
	
	@Test
	public void testCreateChildFeature() {
		Feature root = fm.getRoot(); 
		Feature f = root.addChild("f1", Feature.ALTERNATIVE, Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature g1 = f.addChild("g1", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature g2 = f.addChild("g2", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		
		Feature t; 
		assertEquals(1, root.getChildren().size(), 0);
		t = root.getChildrenByName("f1"); 
		assertNotNull(t);
		assertEquals("splSimulator.Feature", t.getClass().getName());
		assertEquals("f1", t.getName());
		assertEquals(2, t.getChildren().size(), 0);
		
		g1 = t.getChildrenByName("g1");
		assertNotNull(g1);
		assertEquals("splSimulator.Feature", g1.getClass().getName());
		assertEquals("g1", g1.getName());
		assertEquals(0, g1.getChildren().size(), 0);
		
		g2 = t.getChildrenByName("g2");
		assertNotNull(g2);
		assertEquals("splSimulator.Feature", g2.getClass().getName());
		assertEquals("g2", g2.getName());
		assertEquals(0, g2.getChildren().size(), 0);
		
	}
	
	
	@Test 
	public void testDeleteChildFeature() {
		Feature root = fm.getRoot(); 
		Feature f = root.addChild("f1", Feature.ALTERNATIVE, Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		
		boolean deleted = root.deleteFeature("f1");
		assertTrue(deleted);
		assertNull(root.getChildrenByName("f1"));
	}
	
	
	@Test 
	public void testCreateCrossTreeConstraint() {
		Feature root = fm.getRoot(); 
		Feature f = root.addChild("f", Feature.ALTERNATIVE, Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature g = f.addChild("g", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature h = f.addChild("h", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		Feature i = root.addChild("i", Feature.LEAF, !Feature.MANDATORY, 
				!Feature.ABSTRACT, !Feature.HIDDEN);
		
		boolean answer = fm.addCrossTreeConstraint("i -> !h");
		
		assertTrue(answer);
	}

}
