import java.util.Random;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    //this is for keeping track of stack of tiles so we dont modify array every single time when a tile gets picked.
    int tileIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
        for(int i=0; i<players.length; i++){
            for(int j=0; j<14; j++){
                players[i].addTile(tiles[tileIndex++]);
            }
            if(i==0){
                players[0].addTile(tiles[tileIndex++]);
            }
        }
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        players[currentPlayerIndex].addTile(lastDiscardedTile);
        return lastDiscardedTile.toString();
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public Tile getTopTile() {
        if(tileIndex >= tiles.length)
        {
            System.out.println("Tiles ended. The game ends.");
            return null;
        }
        
        players[currentPlayerIndex].addTile(tiles[tileIndex]);
        Tile tempTile = tiles[tileIndex];
        tiles[tileIndex] = null;
        tileIndex++;
        return tempTile;
    }

    public boolean isTie() {
        if (tileIndex >= tiles.length) {
            return true;
        }
        return false;
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {

        Random  random = new Random();
        for(int i = tiles.length - 1; i > 0; i--)
        {
            int randomIndex = random.nextInt(i + 1);
            Tile tempTile = tiles[randomIndex];

            tiles[randomIndex] = tiles[i];
            tiles[i] = tempTile;

        }

    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game, use isWinningHand() method of Player to decide
     */
    public boolean didGameFinish() {
        return players[currentPlayerIndex].isWinningHand();
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {

        Player currentPlayer =players[currentPlayerIndex];
        if(lastDiscardedTile == null)
        {
            Tile tileFromStack = getTopTile();
            System.out.print(currentPlayer.getName() + " pick the top tile from stack");
            currentPlayer.addTile(tileFromStack);
            return;
        }
        int lastDiscardedTileValue = lastDiscardedTile.getValue();
        char lastDiscardedTileColor = lastDiscardedTile.getColor();
        

        boolean getLastDiscardedTile = false;
        boolean sameTileExist = false;
        int sameValDiffColCounter = 0;

        // check for same tile existence
        for (Tile t : currentPlayer.getTiles()) {
            if (t != null && t.getValue() == lastDiscardedTileValue) {
                if (t.getColor() != lastDiscardedTileColor) {
                    sameValDiffColCounter++;
                } else {
                    sameTileExist = true;
                }
            } 
        }
        
        // sameValDiffColCounter >= 2 means 3 or 4 length chain can be made with the last discarded tile
        if (sameValDiffColCounter >= 2 && !sameTileExist) {
            getLastDiscardedTile = true;
        }

        if (getLastDiscardedTile) {
            getLastDiscardedTile();
            System.out.print(currentPlayer.getName() + " picks the last discarded tile.");
        } else {
            getTopTile();
            System.out.print(currentPlayer.getName() + " picks the top tile from the stack.");
        }
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * this method should print what tile is discarded since it should be
     * known by other players. You may first discard duplicates and then
     * the single tiles and tiles that contribute to the smallest chains.
     */
    public void discardTileForComputer() {
        Player currentPlayer = players[currentPlayerIndex];
        Tile[] tiles = currentPlayer.getTiles();

        int discardedTileIndex = 0;
        int tempSameTileNum = 0, maxSameTileNum = 0;

        // check for duplicates
        for (int i = 0; i < currentPlayer.numberOfTiles - 1; i++) {
            if (tiles[i].toString().equals(tiles[i + 1].toString())) {
                tempSameTileNum++;
                if (tempSameTileNum > maxSameTileNum) {
                    maxSameTileNum = tempSameTileNum;
                    discardedTileIndex = i;
                }
            } else {
                tempSameTileNum = 0;
            }
        }

        // check for the single tiles and tiles that contribute to the smallest chains
        if (maxSameTileNum == 0) {
            // minSameValNum equals to 4 because there are 4 different colors so tempSameValNum can be at most 3
            int tempSameValNum = 0, minSameValNum = 4;

            for (int i = 0; i < currentPlayer.numberOfTiles - 1; i++) {
                if (tiles[i].getValue() == tiles[i + 1].getValue()) {
                    tempSameValNum++;
                } else {
                    if (tempSameValNum < minSameValNum) {
                        minSameValNum = tempSameValNum;
                        tempSameValNum = 0;
                        discardedTileIndex = i;
                    }
                }
            }
        } 

        discardTile(discardedTileIndex);
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        Tile[] playerTiles = players[currentPlayerIndex].getTiles();
        if (playerTiles[tileIndex] == null) {
            System.out.println("You don't have any tile with that index!");
        } else {
            this.lastDiscardedTile = playerTiles[tileIndex];
            for (int i = tileIndex; i < playerTiles.length - 1; i++) {
                playerTiles[i] = playerTiles[i + 1];
            }
            players[currentPlayerIndex].setNumberOfTiles(players[currentPlayerIndex].getNumberOfTiles() - 1);
            System.out.println(players[currentPlayerIndex].getName() + " just discarded " + this.lastDiscardedTile.toString() + " from your tiles.");
        }
        
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].sortTiles();
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
