package org.hyperion.rs2.model.content.misc;

import org.hyperion.Configuration;
import org.hyperion.engine.task.Task;
import org.hyperion.rs2.commands.Command;
import org.hyperion.rs2.commands.CommandHandler;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Rank;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.content.Lock;
import org.hyperion.util.Misc;
import org.hyperion.util.Time;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jack Daniels.
 */
public class TriviaBot {

	/**
	 * Prefix for all messages done by TriviaBot.
	 */
	private static final String TITLE = "[@whi@TriviaBot@bla@] ";

	/**
	 * Max amount of characters a question can be.
	 */
	private final static int QUESTION_MAX_LENGTH = 45;

	/**
	 * LinkedLists to hold data for the TriviaBot. The names are self-explanatory.
	 */
	private static List<String> currentAnswers = new LinkedList<>();
	private static List<String> attemptedAnswers = new LinkedList<>();
	private static List<Question> questions = new ArrayList<>();

	/**
	 * The current Question.
	 */
	private static String currentQuestion;

	/**
	 * The speed counter.
	 */
	private static int speedCounter = 0;

	/**
	 * The ID of the last question.
	 */
	private static int lastQuestionID = 0;

	/**
	 * All answers shouldn't contain any of the Strings below to be considered as valid.
	 */
	private static final String[] NOT_ALLOWED_WORDS = {
			"@", "arsen", "cock", "faggot", "fuck", "suck", "dick", "vagina", "dildo", "nigger", "black",
			"pooper", "penis", "nigga", "shit", "c0ck", "nigga", "ass", "boobs",
	};

	/**
	 * The event that updates the question every <code>CYCLETIME</code>.
	 */
	private final static Task TRIVIA_EVENT = new Task(Time.ONE_MINUTE,"triva2") {
		@Override
		public void execute() {
			updateQuestion();
		}
	};

	/**
	 * Initialized the TriviaBot.
	 */
	public static void init() {
		loadQuestions();
		updateQuestion();
		World.submit(TRIVIA_EVENT);
	}

	/**
	 * Sets the speed counter.
	 *
	 * @param counter
	 */
	public static void setSpeedCounter(int counter) {
		speedCounter = counter;
	}

	/**
	 * Use to get the amount of questions in the memory.
	 *
	 * @returns the amount of questions.
	 */
	public static int getQuestionsAmount() {
		return questions.size();
	}

	/**
	 * Use to get the amount of trivia players.
	 *
	 * @returns the amount of trivia players.
	 */
	public static int getPlayersAmount() {
		int counter = 0;
		for(Player p : World.getPlayers()) {
			if(Lock.isEnabled(p, Lock.TRIVIA))
				counter++;
		}
		return counter;
	}

	/**
	 * Use to get the current question.
	 *
	 * @returns the current question
	 */
	public static String getQuestion() {
		return currentQuestion;
	}

	/**
	 * Resets the answers.
	 */
	public static void resetAnswers() {
		currentAnswers.clear();
		attemptedAnswers.clear();
	}

	/**
	 * This method makes the player answer on a question with the specified <code>answer</code>
	 *
	 * @param p
	 * @param answer
	 */
	public static void sayAnswer(Player p, String answer) {
		if(! p.getTrivia().canAnswer()) {
			p.getActionSender().sendMessage("You have already answered a few seconds ago.");
			return;
		}
		for(String s : NOT_ALLOWED_WORDS) {
			if(answer.toLowerCase().contains(s)) {
				p.getActionSender().sendMessage("Your answer contains unacceptable language.");
				return;
			}
		}
		if(currentQuestion.equals("")) {
			p.getActionSender().sendMessage("There is currently no question.");
			return;
		}
		for(String a : currentAnswers) {
			if(answer.equalsIgnoreCase(a)) {
				rightAnswer(p);
				return;
			}
		}
		attemptedAnswers.add(answer);
		p.getActionSender().sendMessage("You haven't answered the question correctly.");
		if(Math.random() > 0.96) {
			yellMessage("There are currently " + getPlayersAmount() + " people playing Trivia.");
		}
		p.getTrivia().updateTimer();
	}

	/**
	 * This method is called whenever a player has answered
	 * a question correctly.
	 *
	 * @param p
	 */
	private static void rightAnswer(Player p) {
		yellMessage("Player @dre@" + p.getSafeDisplayName() + "@bla@ has answered my question correctly.");
		if(currentAnswers.size() == 1) {
			yellMessage("The answer was: @dre@" + currentAnswers.get(0));
		} else {
			yellMessage("One of the answers was: @dre@" + currentAnswers.get(0));
		}
		yellMessage("He has been rewarded " + Configuration.getString(Configuration.ConfigurationObject.NAME) + " points. The question will soon be updated.");
		String wrongAnswers = "";
		for(String s : attemptedAnswers) {
			if(wrongAnswers.length() > 80)
				break;
			wrongAnswers += s + ", ";
		}
		try {
			wrongAnswers = wrongAnswers.substring(0, wrongAnswers.lastIndexOf(","));
		} catch(Exception e){}
		if(!wrongAnswers.isEmpty())
			yellMessage("Wrong answers were: @dre@" + wrongAnswers);
		currentQuestion = "";
		resetAnswers();
		addReward(p);
		if(speedCounter > 0) {
			World.submit(new Task(2000,"trivia") {
				public void execute() {
					updateQuestion();
					speedCounter--;
					this.stop();
				}
			});
		}
	}

	/**
	 * Updates the current question.
	 */
	public static void updateQuestion() {
		TriviaSettings.resetAllTimers();
		int r = Misc.random(questions.size() - 1);
		if(Math.random() > 0.5) {
			int r2 = Misc.random(questions.size() - 1);
			r = Math.max(r, r2);
		}
		while(r == lastQuestionID || questions.get(r).getQuestion().length() > QUESTION_MAX_LENGTH) {
			r = Misc.random(questions.size() - 1);
		}
		setQuestion(r);
	}

	/**
	 * Sets a new question.
	 *
	 * @param ID
	 */
	private static void setQuestion(int ID) {
		currentQuestion = questions.get(ID).getQuestion();
		lastQuestionID = ID;
		resetAnswers();
		for(int i = 0; i < questions.get(ID).getAnswers().length; i++) {
			currentAnswers.add(questions.get(ID).getAnswers()[i]);
		}
		if(currentQuestion.length() < 45)
			yellMessage("New question: @dre@" + currentQuestion);
		else {
			yellMessage("@dre@New question: ");
			yellMessage("@dre@" + currentQuestion);
		}
	}

	/**
	 * Rewards the player when answering a question correctly.
	 *
	 * @param player
	 */
	private static void addReward(Player player) {
		player.getPoints().increasePkPoints(Misc.random(getPlayersAmount() * 2) + 1);
	}

	/**
	 * Yells a message to all players with Trivia enabled.
	 *
	 * @param message
	 */
	private static void yellMessage(String message) {
		for(Player p : World.getPlayers()) {
			if(p != null && Lock.isEnabled(p, Lock.TRIVIA))
				p.getActionSender().sendMessage(TITLE + message);
		}
	}

	/**
	 * Loads all <code>Question</code> objects into the memory.
	 */
	public static void loadQuestions() {
		try {
			questions.clear();
			BufferedReader r = new BufferedReader(new FileReader("./data/questions.txt"));
			String s = "";
			LinkedList<String> answers = new LinkedList<String>();
			String question = "";
			while((s = r.readLine()) != null) {
				if(s.startsWith("<question>")) {
					if(answers.size() > 0) {
						questions.add(new Question(question, answers));
						answers.clear();
					}
					s = s.replace("<question>", "");
					question = s;
				} else if(s.startsWith("<answer>")) {
					s = s.replace("<answer>", "");
					answers.add(s.toLowerCase());
				}
			}
			if(answers.size() > 0) {
				questions.add(new Question(question, answers));
				answers.clear();
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


}