package edu.kit.ifv.vdv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.Document;

public class ProcessWord {

	private static final String taxonomy = "taxonomy";
	private static final String poolPrefix = "pool";
	private static final String questionPrefix = "questions";

	private final IdSequence idSequence;
	private final File baseFolder;

	public ProcessWord() {
		super();
		idSequence = new IdSequence();
		baseFolder = new File("output");
	}

	public static void main(String[] args) throws Docx4JException, IOException {
		File old = new File("dummy-data");
		new ProcessWord().parse(old);
	}

	private void parse(File folder) throws IOException {
		List<Exercise> exercises = Arrays.stream(folder.listFiles()).map(f -> load(f)).map(b -> parse(b))
				.collect(Collectors.toList());
		exercises.stream().forEach(System.out::println);
		printAsSpecialFormat(exercises);
	}

	private void printAsSpecialFormat(List<Exercise> exercises) throws IOException {
		ensureFolderExists();
		generatePool(exercises);
		generateQuestions(exercises);
		generateTaxonomy(exercises);
	}

	private void generateTaxonomy(List<Exercise> exercises) throws IOException {
		File outFile = outputFile(taxonomy);
		try (BufferedWriter out = Files.newBufferedWriter(outFile.toPath(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING)) {
			String nodeId = "73";
			generateTaxonomyHeader(nodeId, out);
			generateTaxonomy(out, exercises, nodeId);
			generateTaxonomyFooter(out);
		}
	}

	private void generateTaxonomy(BufferedWriter toOut, List<Exercise> exercises, String nodeId) {
		exercises.stream().flatMap(exercise -> exercise.toTaxonomy(nodeId)).forEach(text -> write(text, toOut));
	}

	private void generateTaxonomyFooter(BufferedWriter out) throws IOException {
		write("Taxonomy Footer", out);
	}

	private void generateTaxonomyHeader(String nodeId, BufferedWriter out) throws IOException {
		write("Taxonomy Header", out);
	}

	private void ensureFolderExists() {
		baseFolder.mkdirs();
	}

	private void generateQuestions(List<Exercise> exercises) throws IOException {
		File questionFile = outputFile(questionPrefix);
		try (BufferedWriter out = createWriter(questionFile)) {
			printQuestionHeaderTo(out);
			printQuestions(exercises, out);
			printQuestionFooterTo(out);
		}
	}

	private BufferedWriter createWriter(File questionFile) throws IOException {
		return Files.newBufferedWriter(questionFile.toPath(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}

	private void printQuestions(List<Exercise> exercises, BufferedWriter toOut) {
		exercises.stream().flatMap(Exercise::toQuestion).forEach(t -> write(t, toOut));
	}

	private void write(String text, BufferedWriter toOut) {
		try {
			toOut.write(text);
			toOut.newLine();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private void printQuestionFooterTo(BufferedWriter out) throws IOException {
		write("Question Footer", out);
	}

	private void printQuestionHeaderTo(BufferedWriter out) throws IOException {
		write("Question Header", out);
	}

	private void generatePool(List<Exercise> exercises) throws IOException {
		File poolFile = outputFile(poolPrefix);
		try (BufferedWriter out = createWriter(poolFile)) {
			generatePoolHeader(out);
			printPageObjects(exercises, out);
			printSkills(exercises, out);
			generatePoolFooter(out);
		}
	}

	private File outputFile(String prefix) {
		return new File(baseFolder, prefix + ".txt");
	}

	private void printSkills(List<Exercise> exercises, BufferedWriter toOut) throws IOException {
		write("Skill Assignment Header", toOut);
		exercises.stream().flatMap(Exercise::toSkills).forEach(t -> write(t, toOut));
		write("Skill Assignment Footer", toOut);
	}

	private void printPageObjects(List<Exercise> exercises, BufferedWriter toOut) {
		exercises.stream().flatMap(Exercise::toPageObjects).forEach(text -> write(text, toOut));
	}

	private void generatePoolHeader(BufferedWriter out) throws IOException {
		write("Pool Header", out);
	}

	private void generatePoolFooter(BufferedWriter out) throws IOException {
		write("Pool Footer", out);
	}

	private Body load(File file) {
		try {
			WordprocessingMLPackage wordMLPackage = Docx4J.load(file);
			MainDocumentPart main = wordMLPackage.getMainDocumentPart();
			Document contents = main.getContents();
			return contents.getBody();
		} catch (Docx4JException e) {
			throw new RuntimeException(e);
		}
	}

	private Exercise parse(Body body) {
		DocumentParser parser = new DocumentParser(body, idSequence);
		return parser.parse();
	}

}
