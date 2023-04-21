import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

public class Board {

    private Boolean turn = true;
    private Piece piece[][] = new Piece[8][8];
    private String winner;
    private boolean gameOver = false;
    private boolean playerInCheck = false;
    public Image[] moveColors = new Image[6];
    public Image[] chessBoards = new Image[8];
    protected LinkedList<Piece> allPieces = new LinkedList<>();
    protected LinkedList<Piece> blockingPieces = new LinkedList<>();
    protected LinkedList<Piece> tempKings = new LinkedList<>();

    public Board(int pieceSize, int boardSize) {
        //initialize();
        try {
            this.chessBoards[0] = ImageIO.read(new File("Images/WoodChessBoard.png")).getScaledInstance(boardSize, boardSize, Image.SCALE_SMOOTH);
            this.chessBoards[1] = ImageIO.read(new File("Images/BlackChessBoard.png")).getScaledInstance(boardSize, boardSize, Image.SCALE_SMOOTH);
            this.chessBoards[2] = ImageIO.read(new File("Images/BrushedMetalChessBoard.png")).getScaledInstance(boardSize, boardSize, Image.SCALE_SMOOTH);
            this.chessBoards[3] = ImageIO.read(new File("Images/MarbleChessBoard.png")).getScaledInstance(boardSize, boardSize, Image.SCALE_SMOOTH);
            this.chessBoards[4] = ImageIO.read(new File("Images/SpaceChessBoard.png")).getScaledInstance(boardSize, boardSize, Image.SCALE_SMOOTH);
            this.chessBoards[5] = ImageIO.read(new File("Images/AIChessBoard.png")).getScaledInstance(boardSize, boardSize, Image.SCALE_SMOOTH);
            this.chessBoards[6] = ImageIO.read(new File("Images/CodeChessBoard.png")).getScaledInstance(boardSize, boardSize, Image.SCALE_SMOOTH);
            this.chessBoards[7] = ImageIO.read(new File("Images/FavoriteMovieChessBoard.png")).getScaledInstance(boardSize, boardSize, Image.SCALE_SMOOTH);
            this.moveColors[0] = ImageIO.read(new File("Images/HighlightedEmpty.png")).getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
            this.moveColors[1] = ImageIO.read(new File("Images/HighlightedKill.png")).getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
            this.moveColors[2] = ImageIO.read(new File("Images/Check.png")).getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
            this.moveColors[3] = ImageIO.read(new File("Images/BlackTileSpace.png")).getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
            this.moveColors[4] = ImageIO.read(new File("Images/WhiteTileSpace.png")).getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
            this.moveColors[5] = ImageIO.read(new File("Images/BlockablePieces.png")).getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean getTurn() {
        return turn;
    }

    public Piece getPiece(int xPosition, int yPosition) {
        return piece[xPosition][yPosition];
    }

    public String getWinner() {
        return winner;
    }

    public void changeTurn() {
        this.turn =! turn;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isPlayerChecked() {
        return playerInCheck;
    }

    public void movePiece(Piece piece, int x, int y) {
        printInfo("Moving piece", "Board", "65", piece);
        piece.setFirstFalse();
        if(piece.getPieceType() == 5) {
            if(getPiece(x, y) != null) {
                if((getPiece(x, y).getPieceType() == 2) && (getPiece(x, y).isWhite == piece.isWhite)) {
                    printInfo("contains check", "movePiece func", "70", getPiece(x, y));
                    castleMove(piece, getPiece(x, y));
                } else {
                    stupid(piece, x, y);
                }
            } else {
                stupid(piece, x, y);
            }
        } else {
            stupid(piece, x, y);
        }
    }

    private void stupid(Piece piece, int x, int y) {
        removePiece(piece.getPosX(), piece.getPosY());
        piece.setPosX(x);
        piece.setPosY(y);
        this.blockingPieces.clear();
        this.piece[x][y] = piece;
        changeTurn();
    }

    public void addPiece(Piece piece, int x, int y, boolean yes) {
        piece.setPosX(x);
        piece.setPosY(y);
        this.piece[x][y] = piece;
        if(yes) {
            allPieces.add(piece);
        }
    }

    public void removePiece(int x, int y) {
        this.piece[x][y] = null;
    }

    public void killPiece(Piece piece, int x, int y) {
        allPieces.remove(getPiece(x, y));
        movePiece(piece, x, y);
    }

    public void checkGameStatus() {
        System.out.println("Checking game status - Board(line : 141)");
        Piece Wking = findWKing();
        Piece Bking = findBKing();
        if((Wking == null) || (Bking == null)) {
            gameOver();
            return;
        }
        switchPawn();
        //printArray(Bking.validMoves);
        addKings(Bking);
        addKings(Wking);
        //printArray(Bking.validMoves);
        moveCheckAll();
        //printArray(Bking.validMoves);
        kingChecked(null);
        printInfo("Checking info", "checkGameStatus func", "152", Wking);
        printInfo("Checking info", "checkGameStatus func", "153", Bking);
        //printArray(Bking.validMoves);
        if(((King)Wking).isChecked()) {
            checkForBlock((King)Wking);
        }
        //printArray(Bking.validMoves);
        if(((King)Bking).isChecked()) {
            checkForBlock((King)Bking);
        }
        printArray(Bking.validMoves);
        if((((King)Wking).isChecked()) && (!((King)Wking).isMoveAvailable()) && (!((King)Wking).isBlockAvailable())) {
            this.winner = "black";
            gameOver();
            return;
        }
        if((((King)Bking).isChecked()) && (!((King)Bking).isMoveAvailable()) && (!((King)Bking).isBlockAvailable())) {
            this.winner = "white";
            gameOver();
            return;
        }
    }

    private void switchPawn() {
        for(Piece current : allPieces) {
            if(current.getPieceType() == 1) {
                if((current.isWhite) && (current.getPosY() == 0)) {
                    allPieces.remove(current);
                    Piece newPiece = new Queen("queen", "Images/QueenWnew.png", true, current.pieceSize);
                    addPiece(newPiece, current.getPosX(), current.getPosY(), true);
                    newPiece.moveCheck(newPiece.getPosX(), newPiece.getPosY(), this);
                    kingChecked(null);
                } else if((!current.isWhite) && (current.getPosY() == 7)) {
                    allPieces.remove(current);
                    Piece newPiece = new Queen("queen", "Images/QueenBnew.png", false, current.pieceSize);
                    addPiece(newPiece, current.getPosX(), current.getPosY(), true);
                    newPiece.moveCheck(newPiece.getPosX(), newPiece.getPosY(), this);
                    kingChecked(null);
                }
            }
        }
    }

    private void checkForBlock(King king) {
        printArray(king.validMoves);
        Boolean blockFound = false;
        Piece checkedByTemp = king.getCheckedBy();
        int saveX = checkedByTemp.getPosX();
        int saveY = checkedByTemp.getPosY();
        for(Piece current : allPieces) {
            if(current.isWhite == king.isWhite) {
                for(int y = 0; y < 8; y++) {
                    for(int x = 0; x < 8; x++) {
                        if((current.validMoves[x][y] != 0) && (current != king)) {
                            Piece temp = new Pawn("temp", "Images/PawnWnew.png", king.isWhite, king.pieceSize);
                            Piece savePiece = getPiece(x, y);
                            printInfo("checking piece", "checkForBlock()", "148", savePiece);
                            removePiece(x, y);
                            addPiece(temp, x, y, false);
                            checkedByTemp.moveCheck(saveX, saveY, this);
                            kingChecked(null);
                            if(!king.isChecked()) {
                                blockingPieces.add(current);
                                current.validMoves[x][y] = current.validMoves[x][y];
                                blockFound = true;
                                king.setCheck(true);
                            } else {
                                current.validMoves[x][y] = 0;
                            }
                            removePiece(x, y);
                            printInfo("checking piece", "checkForBlock()", "160", savePiece);
                            if(savePiece != null) {
                                addPiece(savePiece, x, y, false);
                            }
                            temp = null;
                        }
                    }
                }
            }
        }
        if(blockFound) {
            king.setBlockAvailable(blockFound);
        } else {
            king.setBlockAvailable(blockFound);
        }
        checkedByTemp.moveCheck(saveX, saveY, this);
        kingChecked(null);
    }

    public Piece addKings(Piece king) {
        printInfo("Adding temp kings", "Board", "105", null);
        king.moveCheck(king.getPosX(), king.getPosY(), this);
        kingChecked(king);
        int[][] tempArray = new int[8][8];
        System.arraycopy(king.validMoves, 0, tempArray, 0, king.validMoves.length);
        printArray(tempArray);
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(tempArray[x][y] != 0) {
                    Piece savePiece = getPiece(x, y);
                    System.out.println("adding temp king ****************");
                    Piece tempKing = new King("temp", "Images/HighlightedEmpty.png", king.isWhite, king.pieceSize);
                    addPiece(tempKing, x, y, false);
                    tempKings.add(tempKing);
                    moveCheckAll();
                    kingChecked(tempKing);
                    if(((King)tempKing).isChecked()) {
                        System.out.print(tempKing.getPosX());
                        System.out.print(" , ");
                        System.out.print(tempKing.getPosY());
                        System.out.println(" : temp king pos = 0");
                        king.validMoves[tempKing.getPosX()][tempKing.getPosY()] = 0;
                    }
                    System.out.println("deleting temp king ****************");
                    piece[x][y] = savePiece;
                }
            }
        }
        tempKings.clear();
        return king;
    }

    private void kingChecked(Piece king) {
        printInfo("Checking King", "Board", "138", king);
        boolean Wfound = false;
        boolean Bfound = false;
        Piece Wking = findWKing();
        Piece Bking = findBKing();
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Piece current = this.piece[x][y];
                if(current != null) {
                    if(current.availableKills.contains(Wking)) {
                        System.out.println("White King is in check");
                        Wfound = true;
                        playerInCheck = true;
                        ((King)Wking).setCheck(true);
                        ((King)Wking).setCheckedBy(current);
                    }
                    if(current.availableKills.contains(Bking)) {
                        System.out.println("Black King is in check");
                        Bfound = true;
                        playerInCheck = true;
                        ((King)Bking).setCheck(true);
                        ((King)Bking).setCheckedBy(current);
                    }
                    if(current.availableKills.contains(king)) {
                        System.out.println("temp King is in check");
                        ((King)king).setCheck(true);
                    }
                }
            }
        }
        if((!Wfound) && (Wking != null)) {
            ((King)Wking).setCheck(false);
            ((King)Wking).setCheckedBy(null);
        }
        if((!Bfound) && (Bking != null)) {
            ((King)Bking).setCheck(false);
            ((King)Bking).setCheckedBy(null);
        }
        if(!Bfound && !Wfound) {
            playerInCheck = false;
        }
    }

    private void castleMove(Piece king, Piece rook) {
        int distance;
        if(rook.getPosX() > king.getPosX()) {
            distance = rook.getPosX() - king.getPosX() - 1;
        } else {
            distance = king.getPosX() - rook.getPosX() - 1;
        }
        int kingMove = distance - 1;
        int rookMove = distance - kingMove;
        if(king.getPosX()-rook.getPosX() > 0) {
            kingMove = kingMove*(-1);
        } else {
            rookMove = rookMove*(-1);
        }
        King temp = new King("king", king.imageName, king.isWhite, king.pieceSize);
        temp.setCastle(false);
        temp.setPosX(rook.getPosX()+rookMove);
        temp.setPosY(rook.getPosY());
        Rook temp2 = new Rook("rook", rook.imageName, rook.isWhite, rook.pieceSize);
        temp2.setPosX(king.getPosX()+kingMove);
        temp2.setPosY(king.getPosY());
        this.piece[temp.getPosX()][temp.getPosY()] = temp;
        this.piece[temp2.getPosX()][temp2.getPosY()] = temp2;
        this.piece[king.getPosX()][king.getPosY()] = null;
        this.piece[rook.getPosX()][rook.getPosY()] =  null;
        allPieces.remove(king);
        allPieces.remove(rook);
        allPieces.add(temp);
        allPieces.add(temp2);
        king = null;
        rook = null;
    }

    private void moveCheckAll() {
        printInfo("Move Checking everything", "Board", "178", null);
        for(Piece current : allPieces) {
            if(current.getPieceType() != 5) {
                current.moveCheck(current.getPosX(), current.getPosY(), this);
            }
        }
    }

    private Piece findWKing() {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Piece current = this.piece[x][y];
                if(current != null) {
                    if(current.getPieceType() == 5) {
                        if(current.isWhite) {
                            printInfo("White King Found", "Board", "195", current);
                            return current;
                        }
                    }
                }
            }
        }
        this.winner = "black";
        return null;
    }

    private Piece findBKing() {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                Piece current = this.piece[x][y];
                if(current != null) {
                    if(current.getPieceType() == 5) {
                        if(!current.isWhite) {
                            printInfo("Black King Found", "Board", "211", current);
                            return current;
                        }
                    }
                }
            }
        }
        this.winner = "white";
        return null;
    }

    private void gameOver() {
        this.gameOver = true;
    }

    public void printInfo(String what_is_it_doing, String in_what_class, String what_line, Piece the_piece) {
        System.out.print(what_is_it_doing);
        System.out.print(" - ");
        System.out.print(in_what_class);
        System.out.print("(line : ");
        System.out.print(what_line);
        System.out.print(")");
        if(the_piece != null) {
            System.out.print(" - Piece Type(");
            System.out.print(the_piece.getPieceType());
            System.out.print(") - Piece Loc(");
            System.out.print(the_piece.getPosX());
            System.out.print(" , ");
            System.out.print(the_piece.getPosY());
            System.out.print(")");
            if(the_piece.getPieceType() == 5) {
                System.out.print(" - Checked(");
                System.out.print(((King)the_piece).isChecked());
                System.out.print(")");
                System.out.print(" - Block Available(");
                System.out.print(((King)the_piece).isBlockAvailable());
                System.out.print(")");
            }
            System.out.println();
        } else {
            System.out.println();
        }
    }

    public void printArray(int[][] array) {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                System.out.print(array[x][y]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
