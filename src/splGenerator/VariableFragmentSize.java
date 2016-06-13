package splGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class VariableFragmentSize extends VariableBehavioralParameters {

	private static int messageId = 0;

	@Override
	protected LinkedList<SPL> employTransformation(SPL spl)
			throws CloneNotSupportedException {
		LinkedList<SPL> answer = new LinkedList<SPL>();

		SPL next = spl;
		// The transformation will be applied while the maximum number of
		// messages is not reached
		while (currentValue <= maxValue) {
			SPL temp = createSplDeepCopy(next);

			// 1st step: check if all sequence diagrams have the number of
			// messages
			// equals to the number of minimum messages defined by the attribute
			// minValue of this class's superclass.
			LinkedList<SequenceDiagram> seqDiags = new LinkedList<SequenceDiagram>();
			for (Activity a : temp.getActivityDiagram().getSetOfActivities()) {
				for (SequenceDiagram s : a.getSequenceDiagrams()) {
					try {
						seqDiags.addAll(getTransitiveSequenceDiagrams(s));
					} catch (InsuficientNumberOfMessagesException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			// 2nd step: in case it is possible to create random messages, for
			// each
			// sequence diagram a set of messages (given by the step value) is
			// created considering the set of lifelines initially defined for
			// the
			// "seed" sequence diagram
			for (SequenceDiagram s : seqDiags) {
				int size = s.getElements().size();
				int position = new Random().nextInt(size);
				LinkedList<SequenceDiagramElement> randomMessages = createRandomMessages(
						variationStep, s.getLifelines());
				s.getElements().addAll(position, randomMessages);
			}

			// 3rd step: for each transformation applied, we must create a SPL
			// object, add it to the set of SPLs created (represented by a
			// LinkedList) and then return the set of SPLs.
			answer.add(temp);
			next = temp;
			currentValue += variationStep; // increase the current value by the
											// step value
		}

		return answer;
	}

	/**
	 * This method's role is to create a SPL clone for a given SPL. As clone()
	 * method offered by Java implements a shallow copy of an object we wrote
	 * this method for creating a copy of all objects related to a SPL. To
	 * accomplish this task, this method persists the whole SPL at a temporary
	 * file and read it again in memory, when new and distinct objects are
	 * created.
	 * 
	 * @param spl
	 *            The software product line that will be cloned
	 * @return the cloned software product line
	 */
	private SPL createSplDeepCopy(SPL spl) {
		SPL answer = null;

		ActivityDiagram.reset();
		ActivityDiagramElement.reset();
		SequenceDiagram.reset();
		SequenceDiagramElement.reset();

		File f;
		try {
			f = File.createTempFile("spl", ".xml");
			f.deleteOnExit();
			FileOutputStream stream = new FileOutputStream(f);
			stream.write(spl.getXmlRepresentation().getBytes());
			stream.flush();
			SPL t = SPL.getSplFromXml(f.getAbsolutePath()); // its "deep copy"
															// is produced
			t.setName("model_" + currentValue);
			t.setFeatureModel(spl.getFeatureModel());
			answer = t;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
	}

	/**
	 * This method is responsible for creating a set of messages (whose size is
	 * given by the variationStep parameter), comprising the lifelines passed by
	 * the parameter lifelines. It returns a sequence of messages represented by
	 * a linked list.
	 * 
	 * @param variationStep
	 *            The number of messages to be created
	 * @param lifelines
	 *            The set of lifelines which will be used for creating the
	 *            random messages.
	 * @return the messages sequence represented by a linked list.
	 */
	private LinkedList<SequenceDiagramElement> createRandomMessages(
			int variationStep, HashSet<Lifeline> lifelines) {
		LinkedList<SequenceDiagramElement> answer = null;
		Message m = null;
		boolean consistentSetOfMessages = false;
		while (!consistentSetOfMessages) {
			answer = new LinkedList<SequenceDiagramElement>();
			int i = 0;
			while (i < variationStep) {
				SequenceDiagramElement e = SequenceDiagramElement
						.createElement(SequenceDiagramElement.MESSAGE,
								"added_message_" + messageId++);
				m = (Message) e;
				Lifeline source = randomLifeline(lifelines);
				Lifeline target = randomLifeline(lifelines);
				m.setSource(source);
				m.setTarget(target);
				m.setProbability(target.getReliability());
				m.setType(randomMessageType());
				answer.add(m);
				i++;
			}
			consistentSetOfMessages = isSetOfMessagesConsistent(answer);
		}

		return answer;
	}

	/**
	 * This method is responsible for ensure a sequence of messages randomly
	 * generated is consistent. A (piece of) sequence diagram is consistent if
	 * all synchronous messages have a reply message associated or, if it is
	 * formed by a single message, such message must be asynchronous.
	 * 
	 * @param messages
	 *            the sequence of messages that will be inspected
	 * @return true if the set of messages is consistente, otherwise false is
	 *         returned
	 */
	private boolean isSetOfMessagesConsistent(
			LinkedList<SequenceDiagramElement> messages) {
		boolean answer = false;
		if (messages.size() == 1) { // if it is a singleton set of messages, the
									// message must be asynchronous
			Message m = (Message) messages.get(0);
			if (m.getType() == Message.ASYNCHRONOUS)
				answer = true;
			else
				answer = false;
		} else { // if the set of new messages has more than one message, we
					// must ensure no synchronous message remains without a
					// reply message (it is not mandatory asynchronous messages
					// have reply messages associated)
			int pendingSyncMessages = 0;
			for (SequenceDiagramElement sde : messages) {
				Message m = (Message) sde;
				if (m.getType() == Message.SYNCHRONOUS) {
					pendingSyncMessages++;
				} else if (m.getType() == Message.REPLY) {
					pendingSyncMessages--;
				}
			}
			if (pendingSyncMessages == 0)
				answer = true;
			else
				answer = false;
		}
		return answer;
	}

	/**
	 * This message is responsible for choosing randomly if a message is
	 * synchronous, asynchronous or reply
	 * 
	 * @return an integer related to the constants Message.SYNCHRONOUS,
	 *         Message.ASYNCHRONOUS or Message.REPLY
	 */
	private int randomMessageType() {
		int answer = -1;
		LinkedList<Integer> values = new LinkedList<Integer>();
		values.add(Message.SYNCHRONOUS);
		values.add(Message.ASYNCHRONOUS);
		values.add(Message.REPLY);
		int index = new Random().nextInt(values.size());
		answer = values.get(index);
		return answer;
	}

	/**
	 * This method is responsible for randomly choosing a lifeline from a given
	 * set of lifelines
	 * 
	 * @param lifelines
	 *            the lifelines which will be subject to random choose.
	 * @return a lifeline randomly choosed.
	 */
	private Lifeline randomLifeline(HashSet<Lifeline> lifelines) {
		Lifeline answer = null;
		int i = new Random().nextInt(lifelines.size());
		Object[] vector = lifelines.toArray();
		answer = (Lifeline) vector[i];
		return answer;
	}

	/**
	 * This method returns all the sequence diagrams reachable from a given
	 * sequence diagram. It searches for sequence diagrams, in a recursive
	 * fashion, considering all the fragments represented at the input sequence
	 * diagram. As this method is used when the behavioral models of a given SPL
	 * are being evolved, in case a sequence diagram containing less messages
	 * than the minimum number of messages defined by the minValue attribute, an
	 * InsuficientNumberOfMessagesException will be thrown.
	 * 
	 * @param seqDiag
	 *            the input sequence diagram from the others sequence diagrams
	 *            will be searched.
	 * @return a linked list of sequence diagram objects.
	 * @throws InsuficientNumberOfMessagesException
	 *             in case
	 */
	private LinkedList<SequenceDiagram> getTransitiveSequenceDiagrams(
			SequenceDiagram seqDiag)
			throws InsuficientNumberOfMessagesException {
		LinkedList<SequenceDiagram> answer = new LinkedList<SequenceDiagram>();

		int numMessages = countMessages(seqDiag);
		if (numMessages < minValue) {
			throw new InsuficientNumberOfMessagesException("");
		} else {
			answer.add(seqDiag);
			for (Fragment f : seqDiag.getFragments()) {
				for (SequenceDiagram s : f.getSequenceDiagrams()) {
					answer.addAll(getTransitiveSequenceDiagrams(s));
				}
			}
		}
		return answer;
	}

	/**
	 * Auxiliary method for counting the number of messages of a given Sequence
	 * Diagram.
	 * 
	 * @param seqDiag
	 *            the given sequence diagram for which the messages will be
	 *            counted
	 * @return an integer representing the number of messages contained into the
	 *         sequence diagram
	 */
	private int countMessages(SequenceDiagram seqDiag) {
		int numMessages = 0;
		for (SequenceDiagramElement e : seqDiag.getElements()) {
			if (e instanceof splGenerator.Message) {
				numMessages++;
			}
		}
		return numMessages;
	}

}
