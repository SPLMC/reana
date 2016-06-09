package splGenerator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

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

		// 2nd step: change the reliability values of all lifelines used by the
		// SPL's behavioral model.
		Iterator<Lifeline> itLifeline = lifelines.iterator();
		while (itLifeline.hasNext()) {
			Lifeline l = itLifeline.next();
			// double d = BigDecimal.valueOf(l.getReliability())
			// .setScale(getCurrentValue(), BigDecimal.ROUND_FLOOR)
			// .doubleValue();
			String s = new Formatter().format(Locale.ENGLISH,
					"%." + getCurrentValue() + "f", (float) l.getReliability())
					.toString();
			double d = Double.parseDouble(s);
			l.setReliability(d);
			System.out.println(getCurrentValue() + ": " + l.getReliability()
					+ "->" + l);
		}

		// 3rd step: change the reliability values of messages used by all the
		// sequence diagrams.
		// TODO we should refactor the code in order to allow the reuse of
		// lifelines' reliability values when setting the message's value
		// (instead of copying it).

		itActivity = spl.getActivityDiagram().getSetOfActivities().iterator();
		LinkedList<SequenceDiagram> sequenceDiagrams = new LinkedList<SequenceDiagram>();
		while (itActivity.hasNext()) {
			Activity a = itActivity.next();
			for (SequenceDiagram s : a.getSequenceDiagrams()) {
				sequenceDiagrams.addAll(getTransitiveSequenceDiagrams(s));
			}
			for (SequenceDiagram s : sequenceDiagrams) {
				transformMessageReliability(s);
			}
		}

		answer.add(spl);
		return answer;
	}

	/**
	 * Auxiliary method for transforming the message's reliability according to
	 * the number of decimal places used for its representation
	 * 
	 * @param s
	 *            sequence diagram whose message's reliabilities will be
	 *            transformed.
	 */
	private void transformMessageReliability(SequenceDiagram s) {
		LinkedList<SequenceDiagramElement> elements = s.getElements();
		for (SequenceDiagramElement e : elements) {
			if (e instanceof Message) {
				Message m = (Message) e;
				String probability = new Formatter().format(Locale.ENGLISH,
						"%." + getCurrentValue() + "f",
						(float) m.getProbability()).toString();
				m.setProbability(Double.parseDouble(probability));
			}
		}
	}

	/**
	 * Auxiliary method for getting all the sequence diagrams (by considering
	 * its transitive closure) given a sequence diagram as input.
	 * 
	 * @param s
	 *            sequence diagram from which all transitive sequence diagram
	 *            are going to be recovered.
	 * @return linked list of SequenceDiagram objects recovered from a sequence
	 *         diagram passed by parameter.
	 */
	private LinkedList<SequenceDiagram> getTransitiveSequenceDiagrams(
			SequenceDiagram s) {
		LinkedList<SequenceDiagram> answer = new LinkedList<SequenceDiagram>();

		HashSet<Fragment> frags = s.getFragments();
		for (Fragment f : frags) {
			for (SequenceDiagram seq : f.getSequenceDiagrams()) {
				answer.addAll(getTransitiveSequenceDiagrams(seq));
			}
		}
		answer.add(s);
		return answer;
	}
}
