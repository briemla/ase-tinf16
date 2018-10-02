package edu.kit.ifv.vdv;

public class FalseAnswer implements Answer {

	private static final String positiveReward = "1";
	private static final String negativeReward = "-1";
	private final String answer;
	private final Id id;

	public FalseAnswer(String answer, Id id) {
		super();
		this.id = id;
		this.answer = answer;
	}

	@Override
	public String toFeedback() {
		return "Feedback: " + id.toString();
	}

	@Override
	public String toCondition() {
		return "Condition checked reward: " + negativeReward + " unchecked reward: " + positiveReward;
	}

	@Override
	public String toLabel() {
		return "Response " + id.id() + ": " + answer;
	}

	@Override
	public String toString() {
		return "[   ]" + answer;
	}
}
