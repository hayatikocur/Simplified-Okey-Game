import java.util.Scanner;

/**
 * Main class of Simple Okey Game
 * @authors Hayati Kocur, Yiğit Kaan Önder, Burhan Bulut, Mustafa Mert Mumcu, Emir Akar 
 */
public class ApplicationMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OkeyGame game = new OkeyGame();

        System.out.println("Game mode selection: ");
        System.out.println("1. Standart Human and 3 Computer Player Mode");
        System.out.println("2. All 4 Computer Player Mode");
        int gameModeSelect = sc.nextInt();

        boolean isOnlyComputer = gameModeSelect == 2;

        if(!isOnlyComputer) 
        {
            System.out.print("Please enter your name: ");
            String playerName = sc.next();
            game.setPlayerName(0, playerName);
            game.setPlayerName(1, "John");
            game.setPlayerName(2, "Jane");
            game.setPlayerName(3, "Ted");

        }
        else 
        {
            game.setPlayerName(0, "Player1");
            game.setPlayerName(1, "Player2");
            game.setPlayerName(2, "Player3");
            game.setPlayerName(3, "Player4");
        }

        game.createTiles();
        game.shuffleTiles();
        game.distributeTilesToPlayers();

        // developer mode is used for seeing the computer players hands, to be used for debugging
        System.out.print("Play in developer's mode with other player's tiles visible? (Y/N): ");
        char devMode = sc.next().charAt(0);
        boolean devModeOn = devMode == 'Y';
        
        boolean firstTurn = true;
        boolean gameContinues = true;
        int playerChoice = -1;

        while(gameContinues) {
            
            int currentPlayer = game.getCurrentPlayerIndex();
            System.out.println(game.getCurrentPlayerName() + "'s turn.");
            
            if(!isOnlyComputer && currentPlayer == 0) {
                // this is the human player's turn
                game.displayCurrentPlayersTiles();
                game.displayDiscardInformation();

                System.out.println("What will you do?");

                if(!firstTurn) {
                    // after the first turn, player may pick from tile stack or last player's discard
                    System.out.println("1. Pick From Tiles");
                    System.out.println("2. Pick From Discard");
                }
                else{
                    // on first turn the starting player does not pick up new tile
                    System.out.println("1. Discard Tile");
                }

                System.out.print("Your choice: ");
                playerChoice = sc.nextInt();

                // after the first turn we can pick up
                if(!firstTurn) {
                    if (game.isTie()) {
                        System.out.println("Game ended in a tie.");
                        sc.close();
                        return;
                    }

                    if(playerChoice == 1) {
                        System.out.println("You picked up: " + game.getTopTile());
                        firstTurn = false;
                    }
                    else if(playerChoice == 2) {
                        System.out.println("You picked up: " + game.getLastDiscardedTile()); 
                    }

                    // display the hand after picking up new tile
                    game.displayCurrentPlayersTiles();
                }
                else{
                    // after first turn it is no longer the first turn
                    firstTurn = false;
                }

                gameContinues = !game.didGameFinish();

                if(gameContinues) {
                    // if game continues we need to discard a tile using the given index by the player
                    // (DONE)TODO: make sure the given index is correct, should be 0 <= index <= 14

                    boolean isCorrectChoiceDone = false;

                    while (!isCorrectChoiceDone) {
                        System.out.println("Which tile you will discard?");
                        System.out.print("Discard the tile in index: ");
                        playerChoice = sc.nextInt();

                        if (playerChoice >= 0 && playerChoice <= 14) {
                            isCorrectChoiceDone = true;
                        } else {
                            System.out.println("Please enter a valid index! (0 <= index <= 14)");
                        }
                    }

                    game.discardTile(playerChoice);
                    game.passTurnToNextPlayer();
                }
                else{
                    // if we finish the hand we win
                    System.out.println("Congratulations, you win!");
                }
            }
            else{
                // this is the computer player's turn
                if(devModeOn) {
                    game.displayCurrentPlayersTiles();
                }
                if(isOnlyComputer && firstTurn && currentPlayer == 0)
                {
                    System.out.println(game.getCurrentPlayerName() + "'s first turn of computer discard only");
                    game.discardTileForComputer();
                    firstTurn = false;
                    game.passTurnToNextPlayer();
                    continue;
                }

                // computer picks a tile from tile stack or other player's discard
                game.pickTileForComputer();

                gameContinues = !game.didGameFinish();

                if(gameContinues) {
                    // if game did not end computer should discard
                    if (game.isTie()) {
                        System.out.println("Game ended in a tie.");
                        sc.close();
                        return;
                    }
                    
                    game.discardTileForComputer();
                    game.passTurnToNextPlayer();
                }
                else{
                    // current computer character wins
                    game.displayCurrentPlayersTiles();
                    System.out.println(game.getCurrentPlayerName() + " wins.");
                }
            }
        }
        sc.close();
    }
}
