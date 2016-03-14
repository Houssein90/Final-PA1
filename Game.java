import java.util.Random;
import java.util.Scanner;

// ve4e pi6a mnogo dobre na java, napisah vsi4ko za 2 4asa i trugna ot raz, yahuuaoaoaoaaauuuuu
public class Game {
	private static final String[] wordForGuesing = { "computer", "programmer",
			"software", "debugger", "compiler", "developer", "algorithm",
			"array", "method", "variable" };

	private String guesWord;
	private StringBuffer dashedWord;
	private FileReadWriter filerw;

	public Game(boolean autoStart) {
		guesWord = getRandWord();
		dashedWord = getW(guesWord);
		filerw = new FileReadWriter();
		if(autoStart) {
			displayMenu();
		}
	}


	private String getRandWord(){
		Random rand = new Random();
		String randWord = wordForGuesing[rand.nextInt(9)];
		return randWord;
	}
	public void displayMenu(){
		System.out.println("Welcome to �Hangman� game. Please, try to guess my secret word.\n"
						+ "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
						+ "'HELP' to cheat and 'EXIT' to quit the game.");

		findLetterAndPrintIt();
	}
	/**
	 * Take in a parameter boolean and put it's value to TRUE. This is because he has now used 
	 * the "help" command.  Iterates through the dashBuffer, the first 
	 * unrevealed char, (dash)  reveal it by getting the char from the gussWord.
	 * once done, add 1 to the variable J. This will make us exit the while loop.
	 * @param helpUsed
	 * @param dashB
	 */
	public void helpCommand(boolean helpUsed, StringBuffer dashB){
		helpUsed = true;
		int i = 0, j = 0;
		while (j < 1){
			if (dashB.charAt(i) == '_'){
				dashB.setCharAt(i, guesWord.charAt(i));
				++j;
			}
			++i;
		}
		System.out.println("The secret word is: " + printDashes(dashB));
	}
	/**
	 * checks if the letter typed is infact a letter in the guessword. 
	 * If it is,  then plus on the int variable , counter. 
	 * After  iterating through the guessWord, check if Counter is indeed 0 or not.
	 * If it is, it means he typed the wrong letter, and thus add 1 to the mistake counter(int)
	 * returns the mistakes   
	 * @param letter
	 * @param dashB
	 * @param mistakeCnt
	 * @return  int
	 */
	public int checkLetter(String letter, StringBuffer dashB, int mistakeCnt ){
		int counter = 0;
		for (int i = 0; i < guesWord.length(); i++) {
			String currentLetter = Character.toString(guesWord.charAt(i));
			if (letter.equals(currentLetter)) {
									
				++counter;
				dashB.setCharAt(i, letter.charAt(0));
			}
		}
		
		if (counter == 0) {
			++mistakeCnt;
			System.out.printf("Sorry! There are no unrevealed letters \'%s\'. \n", letter);
			
		} else {
			System.out.printf("Good job! You revealed %d letter(s).\n", counter);
		}
		return mistakeCnt;
	}
	
	/**
	 * If the player won without using the help command, ask for his name and write his 
	 * name with his score to file
	 * @param mistake
	 * @param dashB
	 */
	public void helpFalse(int mistake, StringBuffer dashB){
		System.out.println("You won with " + mistake + " mistake(s).");
		System.out.println("The secret word is: " + printDashes(dashB));

		System.out.println("Please enter your name for the top scoreboard:");
		Scanner input = new Scanner(System.in);
		String playerName = input.next();

		filerw.openFileToWite();
		filerw.addRecords(mistake, playerName);
		filerw.closeFileFromWriting();
		filerw.openFiletoRead();
		filerw.readRecords();
		filerw.closeFileFromReading();
		filerw.printAndSortScoreBoard();
		
	}
	private void findLetterAndPrintIt() {
		boolean isHelpUsed = false;
		String letter = "";
		StringBuffer dashBuff = new StringBuffer(dashedWord);
		int mistakes = 0;

		do {
			System.out.println("The secret word is: " + printDashes(dashBuff));
			System.out.println("DEBUG " + guesWord);
			do {
				System.out.println("Enter your gues(1 letter alowed): ");
				Scanner input = new Scanner(System.in);
				letter = input.next();

				if (letter.equals(Command.help.toString())) {
					
					helpCommand(isHelpUsed, dashBuff);
				}// end if
				menu(letter);

			} while (!letter.matches("[a-z]"));

			mistakes = checkLetter(letter, dashBuff, mistakes);

		} while (!dashBuff.toString().equals(guesWord));

		if (isHelpUsed == false){
			helpFalse(mistakes, dashBuff);
			
		} else {
			System.out.println("You won with "+ mistakes + " mistake(s). but you have cheated."
					+ "You are not allowed to enter into the scoreboard.");
			System.out.println("The secret word is: " + printDashes(dashBuff));
		}
		
		// restart the game
		new Game(true);
		
	}// end method
	private void menu(String letter) {
		if (letter.equals(Command.restart.toString())){
			new Game(true);
		} else if (letter.equals(Command.top.toString())) {
				filerw.openFiletoRead();
				filerw.readRecords();
				filerw.closeFileFromReading();
				filerw.printAndSortScoreBoard();
				new Game(true);
		} else if (letter.equals(Command.exit.toString())) {
					System.exit(1);
				
			
		}
	}

	private StringBuffer getW(String word) {
		StringBuffer dashes = new StringBuffer("");
		for (int i = 0; i < word.length(); i++) {
			dashes.append("_");
		}
		return dashes;
	}
	private String printDashes(StringBuffer word) {
		String toDashes = "";
		
		for (int i = 0; i < word.length(); i++) {
			
			toDashes += (" " + word.charAt(i));
		}
		return toDashes;

	}
}
