package edu.kit.ifv.vdv;

public class IdSequence {

	private int questionId;
	private int responseId;

	public IdSequence() {
		super();
		questionId = 0;
		resetResponse();
	}

	private void resetResponse() {
		responseId = 0;
	}

	public Id nextQuestion() {
		resetResponse();
		return new Id("question_", questionId++);
	}

	public Id nextAnswer() {
		return new Id("response_", responseId++);
	}

}
