import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

public class IsMoveSyntaxCorrect {

	private String move;

	private boolean actualAnswer;

	static boolean isMoveSyntaxCorrect(String move) {
		return true;
	}

	@Given("the move is ABCD")
	public void theMoveIsABCD() {
		move = "ABCD";
	}

	@When("I ask whether it's a correct move")
	public void iAskWhetherItSACorrectMove() {
		actualAnswer = isMoveSyntaxCorrect(move);
	}

	@Then("I should be told {string}")
	public void iShouldBeTold(String expectedAnswer) {
		assertEquals(expectedAnswer.equalsIgnoreCase("YES"), actualAnswer);
	}

	@Given("the move is Nf3")
	public void theMoveIsNf() {
		move = "Nf3";
	}

}
