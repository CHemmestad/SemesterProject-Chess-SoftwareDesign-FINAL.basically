public class Pawn extends Piece {

    private int limit;

    public Pawn(String name, String imageName, boolean isWhite, int pieceSize) {
        super(name, imageName, isWhite, pieceSize);
    }

    @Override
    public int[][] moveCheck(int xPosition, int yPosition, Board board) {
        //printInfo("Pawn move check", "Pawn", "12", this);
        reset();
        if(this.firstMove) {
            this.limit = 2;
        } else {
            this.limit = 1;
        }
        if(this.isWhite) {
            diagonalUpLeft(xPosition, yPosition, 1, board);
            diagonalUpRight(xPosition, yPosition, 1, board);
            up(xPosition, yPosition, limit, board);
        } else {
            diagonalDownLeft(xPosition, yPosition, 1, board);
            diagonalDownRight(xPosition, yPosition, 1, board);
            down(xPosition, yPosition, limit, board);
        }
        return this.validMoves;
    }
}
