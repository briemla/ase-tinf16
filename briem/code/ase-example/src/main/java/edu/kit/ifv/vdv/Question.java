package edu.kit.ifv.vdv;

import java.util.ArrayList;
import java.util.List;

public class Question {

	private final Id id;
	private final String question;
	private final List<Answer> answers;

	public Question(Id id, String question) {
		super();
		this.id = id;
		this.question = question;
		answers = new ArrayList<>();
	}

	public void add(Answer answer) {
		answers.add(answer);
	}

	public boolean isFilled() {
		return !question.isEmpty() && !answers.isEmpty();
	}

	public String toPageObject() {
		return "PageObject: " + id.toString();
	}

	public String toQuestionSkill() {
		return "Skills for question id: " + id.id();
	}

	public String toQuestion() {
		return new QuestionSerialiser(id, question, answers).build();
	}

	public String toTaxonomy(String nodeId) {
		return "Taxonomy for: " + id.id();
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(id);
		string.append(question);
		string.append(System.lineSeparator());
		for (Answer answer : answers) {
			string.append(answer);
			string.append(System.lineSeparator());
		}
		return string.toString();
	}

}
