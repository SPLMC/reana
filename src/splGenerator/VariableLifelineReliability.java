package splGenerator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import splGenerator.Util.ValuesGenerator;

public class VariableLifelineReliability extends VariableBehavioralParameters {

	protected VariableLifelineReliability() {
		// TODO Auto-generated constructor stub
	}

	protected LinkedList<SPL> employTransformation(SPL spl)
			throws CloneNotSupportedException {
		LinkedList<SPL> answer = new LinkedList<SPL>();


		// 1st step: recover all lifelines of all sequence diagrams used by the
		// SPL's behavioral model.
		HashSet<Lifeline> lifelines = new HashSet<Lifeline>();
		Iterator<Activity> itActivity = spl.getActivityDiagram()
				.getSetOfActivities().iterator();
		while (itActivity.hasNext()) {
			Activity a = itActivity.next();
			lifelines.addAll(a.getTranstiveLifelines());
		}

		Iterator<Lifeline> itLifeline = lifelines.iterator();
		while (itLifeline.hasNext()) {
			Lifeline l = itLifeline.next();
//			System.out.println(l + ": " + l.getReliability());
			double d = BigDecimal.valueOf(l.getReliability()).setScale(getCurrentValue(), BigDecimal.ROUND_FLOOR).doubleValue();
			l.setReliability(d);
//			System.out.println(getCurrentValue() + ": " + l.getReliability());
		}
		answer.add(spl);
		return answer;
	}

}
