package edu.kit.ifv.vdv;

import static java.util.stream.Collectors.joining;

import java.util.List;

public class QuestionSerialiser {

	private final Id id;
	private final String question;
	private final List<Answer> answers;

	public QuestionSerialiser(Id id, String question, List<Answer> answers) {
		this.id = id;
		this.question = question;
		this.answers = answers;
	}

	public String build() {
		StringBuilder serialized = new StringBuilder();
		serialized.append(header());
		serialized.append(System.lineSeparator());
		serialized.append(presentation());
		serialized.append(resprocessing());
		serialized.append(feedback());
		serialized.append(footer());
		return serialized.toString();
	}

	private String header() {
		return "Start of question " + id.toString() + ": " + question;
	}

	private String presentation() {
		StringBuilder presentation = new StringBuilder();
		presentation.append("Presentation ");
		presentation.append(id.id());
		presentation.append(": ");
		presentation.append(question);
		presentation.append(System.lineSeparator());
		presentation.append(responses());
		presentation.append(System.lineSeparator());
		return presentation.toString();
	}

	private String responses() {
		return answers.stream().map(Answer::toLabel).collect(joining(System.lineSeparator()));
	}

	private String resprocessing() {
		StringBuilder responseProcessing = new StringBuilder();
		responseProcessing.append("Response processing: ");
		responseProcessing.append(System.lineSeparator());
		responseProcessing.append(responseConditions());
		responseProcessing.append(System.lineSeparator());
		return responseProcessing.toString();
	}

	private String responseConditions() {
		return answers.stream().map(Answer::toCondition).collect(joining(System.lineSeparator()));
	}

	private String feedback() {
		return answers.stream().map(Answer::toFeedback).collect(joining(System.lineSeparator()));
	}

	private String footer() {
		return "End of question: " + id.toString();
	}

}
