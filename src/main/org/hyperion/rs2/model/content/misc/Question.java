package org.hyperion.rs2.model.content.misc;

import java.util.LinkedList;

public class Question {

	private String question;
	private String[] answers;

	public Question(String question, LinkedList<String> answers) {
		//System.out.println("Question : " + question);
		//System.out.println("Answers : " + answers.toString());
		this.question = question;
		this.answers = new String[answers.size()];
		for(int i = 0; i < this.answers.length; i++) {
			this.answers[i] = answers.get(i);
		}
	}

	public String getQuestion() {
		return question;
	}

	public String[] getAnswers() {
		return answers;
	}

}
