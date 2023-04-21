import java.util.ArrayList;

public class King extends Piece {

    private boolean inCheck;
    private boolean castle = true;
    private boolean blockAvailable;
    private Piece checkedBy;
    protected ArrayList<Piece> castlePiece = new ArrayList<>();

    public King(String name, String imageName, boolean isWhite, int pieceSize) {
        super(name, imageName, isWhite, pieceSize);
    }

    public boolean isChecked() {
        return inCheck;
    }

    public boolean isCastle() {
        return castle;
    }

    public boolean isBlockAvailable() {
        return blockAvailable;
    }

    public void setCheckedBy(Piece checker) {
        checkedBy = checker;
    }

    public void setCheck(boolean checked) {
        this.inCheck = checked;
    }

    public void setCastle(boolean castle) {
        this.castle = castle;
    }

    public void setBlockAvailable(boolean blockAvailable) {
        this.blockAvailable = blockAvailable;
    }

    public Piece getCheckedBy() {
        return checkedBy;
    }

    @Override
    public int[][] moveCheck(int xPosition, int yPosition, Board board) {
        //printInfo("King move check", "King", "23", this);
        reset();
        up(xPosition, yPosition, 1, board);
        down(xPosition, yPosition, 1, board);
        right(xPosition, yPosition, 1, board, true);
        left(xPosition, yPosition, 1, board, true);
        diagonalUpRight(xPosition, yPosition, 1, board);
        diagonalUpLeft(xPosition, yPosition, 1, board);
        diagonalDownRight(xPosition, yPosition, 1, board);
        diagonalDownLeft(xPosition, yPosition, 1, board);
        if(castle && firstMove) {
            isCastleAvailable(xPosition, yPosition, board);
        }
        return this.validMoves;
    }

    protected boolean isMoveAvailable() {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if((this.validMoves[x][y] == -1) || (this.validMoves[x][y] == 2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void isCastleAvailable(int xPosition, int yPosition, Board board) {
        Piece temp;
        temp = right(xPosition, yPosition, 5, board, false);
        printInfo("checking", "castle available - King", "57", temp);
        if(temp != null) {
            this.castlePiece.add(temp);
        }
        temp = left(xPosition, yPosition, 5, board, false);
        printInfo("checking", "castle available - King", "62", temp);
        if(temp != null) {
            this.castlePiece.add(temp);
        }
    }
}
