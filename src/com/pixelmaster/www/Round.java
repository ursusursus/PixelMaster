package com.pixelmaster.www;

public class Round {

	private String correctAnswer;
	public String getCorrectAnswer() {
		return correctAnswer;
	}

	private String[] answers;
	public String[] getAnswers() {
		return answers;
	}
	
	private int[] resourceIDs;
	public int[] getResourceIDs() {
		return resourceIDs;
	}

	public Round(String[] answers, int correctAnswerIndex, int[] resourceIDs) {
		
		this.answers = answers;
		this.correctAnswer = answers[correctAnswerIndex];
		this.resourceIDs = resourceIDs;
	}

}
