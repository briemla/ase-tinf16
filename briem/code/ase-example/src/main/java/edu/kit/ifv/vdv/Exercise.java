package edu.kit.ifv.vdv;
import java.util.List;
import java.util.stream.Stream;

public class Exercise {

	private final String title;
	private final List<Question> questions;

	public Exercise(String title, List<Question> questions) {
		super();
		this.title = title;
		this.questions = questions;
	}
	
	public Stream<String> toPageObjects() {
		return questions.stream().filter(Question::isFilled).map(Question::toPageObject);
	}
	
	public Stream<String> toSkills() {
		return questions.stream().filter(Question::isFilled).map(Question::toQuestionSkill);
	}
	
	public Stream<String> toQuestion() {
		return questions.stream().filter(Question::isFilled).map(Question::toQuestion);
	}
	
	public Stream<String> toTaxonomy(String nodeId) {
		return questions.stream().filter(Question::isFilled).map(question -> question.toTaxonomy(nodeId));
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(title);
		string.append(System.lineSeparator());
		questions.stream().filter(Question::isFilled).forEach(q -> string.append(q.toString()));
		return string.toString();
	}
}
