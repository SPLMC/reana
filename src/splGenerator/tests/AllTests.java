package splGenerator.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ActivityDiagramElementTest.class, ActivityDiagramTest.class,
	FeatureModelPersistenceTest.class, FeatureModelTest.class, 
	SequenceDiagramTest.class, SplBehavioralModelTest.class, 
	ActivityDiagramPersistenceTest.class, SequenceDiagramPersistenceTest.class, 
	ReadSplFromXmlRepresentationTest.class})

public class AllTests {

}
