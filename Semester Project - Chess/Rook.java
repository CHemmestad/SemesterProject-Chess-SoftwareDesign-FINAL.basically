public class Rook extends Piece {

    public Rook(String name, String imageName, boolean isWhite, int pieceSize) {
        super(name, imageName, isWhite, pieceSize);
    }

    @Override
    public int[][] moveCheck(int xPosition, int yPosition, Board board) {
        //printInfo("Rook move check", "Rook", "9", this);
        reset();
        up(xPosition, yPosition, 10, board);
        down(xPosition, yPosition, 10, board);
        right(xPosition, yPosition, 10, board, true);
        left(xPosition, yPosition, 10, board, true);
        return this.validMoves;
    }
}
