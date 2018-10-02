package edu.kit.ifv.vdv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.docx4j.wml.Body;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Tr;

public class DocumentParser {

	private final Body body;
	private final IdSequence idSequence;
	private final List<Question> questions;

	public DocumentParser(Body body, IdSequence idSequence) {
		super();
		this.body = body;
		this.idSequence = idSequence;
		questions = new ArrayList<>();
	}

	public Exercise parse() {
		Question current = null;
		List<Object> content = body.getContent();
		Iterator<Object> iterator = content.iterator();
		String element = null;
		while (iterator.hasNext() && !(element = iterator.next().toString()).startsWith("Übung")) {
		}
		String title = element;
		while (iterator.hasNext() && !(element = iterator.next().toString()).contains("folgender Weise")) {
		}
		iterator.next();
		// example end
		for (; iterator.hasNext();) {
			Object object = iterator.next();
			if (object instanceof P) {
				current = new Question(nextQuestionId(), object.toString());
				questions.add(current);
			}
			if (object instanceof JAXBElement) {
				parseAnswers(current, (JAXBElement<?>) object);
			}
		}
		return new Exercise(title, questions);
	}

	private Id nextQuestionId() {
		return idSequence.nextQuestion();
	}

	private void parseAnswers(Question current, JAXBElement<?> jaxbElement) {
		Tbl table = (Tbl) jaxbElement.getValue();
		List<Object> tableContent = table.getContent();
		for (Object object2 : tableContent) {
			if (object2 instanceof Tr) {
				if (2 == ((Tr) object2).getContent().size()) {
					Object rowContent = ((Tr) object2).getContent().get(1);
					if (rowContent instanceof JAXBElement) {
						Tc cell = (Tc) ((JAXBElement<?>) rowContent).getValue();
						Answer answer = isRightAnswer(cell);
						current.add(answer);
					}
				}
			}
		}
	}

	private Answer isRightAnswer(Tc cell) {
		P answerP = (P) cell.getContent().iterator().next();
		TcPr tcPr = cell.getTcPr();
		if (null != tcPr && null != tcPr.getShd() && "FFFF00".equals(tcPr.getShd().getFill())) {
			return new RightAnswer(answerP.toString(), nextAnswerId());
		}
		boolean isRight = answerP.getContent().stream().filter(o -> o instanceof R).map(o -> (R) o)
				.filter(r -> null != r.getRPr()).anyMatch(r -> null != r.getRPr().getHighlight());
		if (isRight) {
			return new RightAnswer(answerP.toString(), nextAnswerId());
		}
		return new FalseAnswer(answerP.toString(), nextAnswerId());
	}

	private Id nextAnswerId() {
		return idSequence.nextAnswer();
	}

}
