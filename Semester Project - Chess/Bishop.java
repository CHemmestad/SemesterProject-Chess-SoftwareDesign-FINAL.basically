public class Bishop extends Piece {

    public Bishop(String name, String imageName, boolean isWhite, int pieceSize) {
        super(name, imageName, isWhite, pieceSize);
    }

    @Override
    public int[][] moveCheck(int xPosition, int yPosition, Board board) {
        //printInfo("Bishop move check", "Bishop", "9", this);
        reset();
        diagonalUpRight(xPosition, yPosition, 10, board);
        diagonalUpLeft(xPosition, yPosition, 10, board);
        diagonalDownRight(xPosition, yPosition, 10, board);
        diagonalDownLeft(xPosition, yPosition, 10, board);
        return this.validMoves;
    }
}
