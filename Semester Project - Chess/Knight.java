public class Knight extends Piece {

    public Knight(String name, String imageName, boolean isWhite, int pieceSize) {
        super(name, imageName, isWhite, pieceSize);
    }

    @Override
    public int[][] moveCheck(int xPosition, int yPosition, Board board) {
        //printInfo("Knight move check", "Knight", "9", this);
        reset();
        upL(xPosition, yPosition, board);
        downL(xPosition, yPosition, board);
        rightL(xPosition, yPosition, board);
        leftL(xPosition, yPosition, board);
        return this.validMoves;
    }
}
