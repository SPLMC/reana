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

			// 1st step: check if all sequence diagrams have the number of messages
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
			
			// 2nd step: in case it is possible to create random messages, for each
			// sequence diagram a set of messages (given by the step value) is
			// created considering the set of lifelines initially defined for the
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
			SPL t = SPL.getSplFromXml(f.getAbsolutePath()); //its "deep copy" is produced
			t.setName("model_" + currentValue);
			t.setFeatureModel(spl.getFeatureModel());
			answer = t; 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
	}

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

	private Lifeline randomLifeline(HashSet<Lifeline> lifelines) {
		Lifeline answer = null;
		int i = new Random().nextInt(lifelines.size());
		Object[] vector = lifelines.toArray();
		answer = (Lifeline) vector[i];
		return answer;
	}

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
