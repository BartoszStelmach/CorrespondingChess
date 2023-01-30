Feature: Is the syntax of a move correct?
  String describing a move should follow the chess algebraic notation

  Scenario: ABCD is not a correct move
    Given the move is ABCD
    When I ask whether it's a correct move
    Then I should be told "NO"

  Scenario: Nf3 is a correct move
    Given the move is Nf3
    When I ask whether it's a correct move
    Then I should be told "YES"