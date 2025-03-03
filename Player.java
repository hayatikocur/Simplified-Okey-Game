import java.util.Arrays;

public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    public Tile getAndRemoveTile(int index) {
        Tile givenTile = this.playerTiles[index];
        for(int i = index; i < playerTiles.length - 1; i++){
            playerTiles[i] = playerTiles[i+1];
        }
        playerTiles[numberOfTiles - 1] = null;
        numberOfTiles--;
        return givenTile;
    }

    public void addTile(Tile t) {
        if (this.numberOfTiles == 15) {
            System.out.println("You can't add that tile. You already have 15 tiles.");
        } else {
            this.playerTiles[this.numberOfTiles] = t;
            this.numberOfTiles++;
        }
    }

    public boolean isWinningHand() {
        int count = 0;
        int currentChain = 1;

        if (numberOfTiles < 12) return false;

        for(int i = 0; i < numberOfTiles - 1; i++){
            if (playerTiles[i].getValue() == playerTiles[i+1].getValue() && playerTiles[i].getColor() == playerTiles[i+1].getColor()) {
                continue;
            } else if ( playerTiles[i] != null && playerTiles[i+1] != null && playerTiles[i].getValue() == playerTiles[i+1].getValue() && playerTiles[i].getColor() != playerTiles[i+1].getColor()) {
                currentChain++;
            } else {
                currentChain = 1;
            }
            
            if (currentChain == 4) {
                count++;
                currentChain = 1;
            }    
        }

        if (count == 3) {
            System.out.println();
            System.out.println("Winner's hand: ");
            
            return true;
        } else {
            return false;
        }
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }

    public int getNumberOfTiles() {
        return this.numberOfTiles;
    }

    public void setNumberOfTiles(int numberOfTiles) {
        this.numberOfTiles = numberOfTiles;
    }

    public void sortTiles() {
        Arrays.sort(this.playerTiles, 0, this.numberOfTiles);
    }
}
