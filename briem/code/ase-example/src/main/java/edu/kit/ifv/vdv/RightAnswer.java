package edu.kit.ifv.vdv;

public class RightAnswer implements Answer {

	private static final String positiveReward = "1";
	private static final String negativeReward = "-1";
	private final String answer;
	private final Id id;

	public RightAnswer(String answer, Id id) {
		super();
		this.answer = answer;
		this.id = id;
	}
	

	@Override
	public String toFeedback() {
		return "Feedback: " + id.toString();
	}

	@Override
	public String toCondition() {
		return "Condition checked reward: " + positiveReward + " unchecked reward: " + negativeReward;
	}

	@Override
	public String toLabel() {
		return "Response " + id.id() + ": " + answer;
	}

	@Override
	public String toString() {
		return "[ X ]" + answer;
	}

}
