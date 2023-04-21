import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

abstract public class Piece {

    public int pieceSize;
    private String name;
    private int pieceType;
    private int xPosition;
    private int yPosition;
    private Image image[] = new Image[3];
    protected boolean firstMove = true;
    protected String imageName;
    protected int validMoves[][] = new int[8][8];
    protected boolean isWhite;
    protected LinkedList<Piece> availableKills = new LinkedList<>();

    public Piece(String name, String imageName, boolean isWhite, int pieceSize) {
        reset();
        this.imageName = imageName;
        this.pieceSize = pieceSize;
        this.name = name;
        this.isWhite = isWhite;
        try {
            this.image[0] = ImageIO.read(new File(imageName));
            image[0] = image[0].getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        findType(name);
    }

    public void setPosX(int x) {
        this.xPosition = x;
    }

    public void setPosY(int y) {
        this.yPosition = y;
    }

    public void setFirstFalse() {
        this.firstMove = false;
    }

    public int getPosX() {
        return this.xPosition;
    }

    public int getPosY() {
        return this.yPosition;
    }

    public int getPieceType() {
        return this.pieceType;
    }

    public Image[] getImage() {
        return this.image;
    }

    abstract public int[][] moveCheck(int xPosition, int yPosition, Board board);

    protected void reset() {
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                this.validMoves[x][y] = 0;
            }
        }
        availableKills.clear();
    }

    protected void printInfo(String what_is_it_doing, String in_what_class, String what_line, Piece the_piece) {
        System.out.print(what_is_it_doing);
        System.out.print(" - ");
        System.out.print(in_what_class);
        System.out.print("(line : ");
        System.out.print(what_line);
        System.out.print(")");
        if(the_piece != null) {
            System.out.print(" - Piece Type(");
            System.out.print(the_piece.getPieceType());
            System.out.print(") - Piece Loc (");
            System.out.print(the_piece.getPosX());
            System.out.print(" , ");
            System.out.print(the_piece.getPosY());
            System.out.println(")");
        } else {
            System.out.println();
        }
    }

    protected void up(int xPosition, int yPosition, int limit, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        if(limit != 0) {
            if(yPos-1 >= 0) {
                if(board.getPiece(xPos, yPos-1) == null) {
                    yPos -= 1;
                    this.validMoves[xPos][yPos] = -1;
                    limit--;
                    up(xPos, yPos, limit, board);
                } else if(board.getPiece(xPos, yPos-1).isWhite != this.isWhite) {
                    if(this.pieceType != 1) {
                        yPos -= 1;
                        this.validMoves[xPos][yPos] = 2;
                        availableKills.add(board.getPiece(xPos, yPos));
                    }
                }
            }
        }
    }

    protected void down(int xPosition, int yPosition, int limit, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        if(limit != 0) {
            if(yPos+1 < 8) {
                if(board.getPiece(xPos, yPos+1) == null) {
                    yPos += 1;
                    this.validMoves[xPos][yPos] = -1;
                    limit--;;
                    down(xPos, yPos, limit, board);
                } else if(board.getPiece(xPos, yPos+1).isWhite != this.isWhite) {
                    if(this.pieceType != 1) {
                        yPos += 1;
                        this.validMoves[xPos][yPos] = 2;
                        availableKills.add(board.getPiece(xPos, yPos));
                    }
                }
            }
        }
    }

    protected Piece right(int xPosition, int yPosition, int limit, Board board, Boolean add) {
        int xPos = xPosition;
        int yPos = yPosition;
        if(limit != 0) {
            if(xPos+1 < 8) { 
                if(board.getPiece(xPos+1, yPos) == null) {
                    xPos += 1;
                    if(add) {
                        this.validMoves[xPos][yPos] = -1;
                    }
                    limit--;
                    right(xPos, yPos, limit, board, add);
                } else if(board.getPiece(xPos+1, yPos).isWhite != isWhite) {
                    xPos += 1;
                    if(add) {
                        this.validMoves[xPos][yPos] = 2;
                    }
                    availableKills.add(board.getPiece(xPos, yPos));
                } else if((board.getPiece(xPos+1, yPos).getPieceType() == 2) && (this.getPieceType() == 5)) {
                    //((King)this).setCastle(true);
                    if(limit > 1) {
                        xPos += 1;
                        if(board.getPiece(xPos, yPos).firstMove) {
                            this.validMoves[xPos][yPos] = -1;
                        }
                        return board.getPiece(xPos, yPos);
                    }
                }
            }
        }
        return null;
    }

    protected Piece left(int xPosition, int yPosition, int limit, Board board, Boolean add) {
        int xPos = xPosition;
        int yPos = yPosition;
        if(limit != 0) {
            if(xPos-1 >= 0) {
                if(board.getPiece(xPos-1, yPos) == null) {
                    xPos -= 1;
                    if(add) {
                        this.validMoves[xPos][yPos] = -1;
                    }
                    limit--;
                    left(xPos, yPos, limit, board, add);
                } else if(board.getPiece(xPos-1, yPos).isWhite != isWhite) {
                    xPos -= 1;
                    if(add) {
                        this.validMoves[xPos][yPos] = 2;
                    }
                    availableKills.add(board.getPiece(xPos, yPos));
                } else if((board.getPiece(xPos-1, yPos).getPieceType() == 2) && (this.getPieceType() == 5)) {
                    //((King)this).setCastle(true);
                    if(limit > 1) {
                        xPos -= 1;
                        if(board.getPiece(xPos, yPos).firstMove) {
                            this.validMoves[xPos][yPos] = -1;
                        }
                        return board.getPiece(xPos, yPos);
                    }
                }
            }
        }
        return null;
    }

    protected void diagonalUpRight(int xPosition, int yPosition, int limit, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        if(limit != 0) {
            if((yPos-1 >= 0) && (xPos+1 < 8)) {
                if(board.getPiece(xPos+1, yPos-1) == null) {
                    if(this.pieceType != 1) {
                        yPos -= 1;
                        xPos += 1;
                        this.validMoves[xPos][yPos] = -1;
                        limit--;
                        diagonalUpRight(xPos, yPos, limit, board);
                    }
                } else if(board.getPiece(xPos+1, yPos-1).isWhite != isWhite) {
                    yPos -= 1;
                    xPos += 1;
                    this.validMoves[xPos][yPos] = 2;
                    availableKills.add(board.getPiece(xPos, yPos));
                }
            }
        }
    }

    protected void diagonalUpLeft(int xPosition, int yPosition, int limit, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        if(limit != 0) {
            if((yPos-1 >= 0) && (xPos-1 >= 0)) {
                if(board.getPiece(xPos-1, yPos-1) == null) {
                    if(this.pieceType != 1) {
                        yPos -= 1;
                        xPos -= 1;
                        this.validMoves[xPos][yPos] = -1;
                        limit--;
                        diagonalUpLeft(xPos, yPos, limit, board);
                    }
                } else if(board.getPiece(xPos-1, yPos-1).isWhite != isWhite) {
                    yPos -= 1;
                    xPos -= 1;
                    this.validMoves[xPos][yPos] = 2;
                    availableKills.add(board.getPiece(xPos, yPos));
                }
            }
        }
    }

    protected void diagonalDownRight(int xPosition, int yPosition, int limit, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        if(limit != 0) {
            if((yPos+1 < 8) && (xPos+1 < 8)) {
                if(board.getPiece(xPos+1, yPos+1) == null) {
                    if(this.pieceType != 1) {
                        yPos += 1;
                        xPos += 1;
                        this.validMoves[xPos][yPos] = -1;
                        limit--;
                        diagonalDownRight(xPos, yPos, limit, board);
                    }
                } else if(board.getPiece(xPos+1, yPos+1).isWhite != isWhite) {
                    yPos += 1;
                    xPos += 1;
                    this.validMoves[xPos][yPos] = 2;
                    availableKills.add(board.getPiece(xPos, yPos));
                }
            }
        }
    }

    protected void diagonalDownLeft(int xPosition, int yPosition, int limit, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        if(limit != 0) {
            if((yPos+1 < 8) && (xPos-1 >= 0)) {
                if(board.getPiece(xPos-1, yPos+1) == null) {
                    if(this.pieceType != 1) {
                        yPos += 1;
                        xPos -= 1;
                        this.validMoves[xPos][yPos] = -1;
                        limit--;
                        diagonalDownLeft(xPos, yPos, limit, board);
                    }
                } else if(board.getPiece(xPos-1, yPos+1).isWhite != isWhite) {
                    yPos += 1;
                    xPos -= 1;
                    this.validMoves[xPos][yPos] = 2;
                    availableKills.add(board.getPiece(xPos, yPos));
                }
            }
        }
    }

    protected void upL(int xPosition, int yPosition, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        xPos += 1;
        yPos -= 2;
        if((xPos < 8) && (yPos >= 0)) {
            if(board.getPiece(xPos, yPos) == null) {
                this.validMoves[xPos][yPos] = -1;
            } else if(board.getPiece(xPos, yPos).isWhite != isWhite) {
                this.validMoves[xPos][yPos] = 2;
                availableKills.add(board.getPiece(xPos, yPos));
            }
        }
        xPos = xPosition;
        yPos = yPosition;
        xPos -= 1;
        yPos -= 2;
        if((xPos >= 0) && (yPos >= 0)) {
            if( board.getPiece(xPos, yPos) == null) {
                this.validMoves[xPos][yPos] = -1;
            } else if(board.getPiece(xPos, yPos).isWhite != isWhite) {
                this.validMoves[xPos][yPos] = 2;
                availableKills.add(board.getPiece(xPos, yPos));
            }
        }
    }

    protected void downL(int xPosition, int yPosition, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        xPos += 1;
        yPos += 2;
        if((xPos < 8) && (yPos < 8)) {
            if(board.getPiece(xPos, yPos) == null) {
                this.validMoves[xPos][yPos] = -1;
            } else if(board.getPiece(xPos, yPos).isWhite != isWhite) {
                this.validMoves[xPos][yPos] = 2;
                availableKills.add(board.getPiece(xPos, yPos));
            }
        }
        xPos = xPosition;
        yPos = yPosition;
        xPos -= 1;
        yPos += 2;
        if((xPos >= 0) && (yPos < 8)) {
            if( board.getPiece(xPos, yPos) == null) {
                this.validMoves[xPos][yPos] = -1;
            } else if(board.getPiece(xPos, yPos).isWhite != isWhite) {
                this.validMoves[xPos][yPos] = 2;
                availableKills.add(board.getPiece(xPos, yPos));
            }
        }
    }

    protected void rightL(int xPosition, int yPosition, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        xPos += 2;
        yPos += 1;
        if((xPos < 8) && (yPos < 8)) {
            if(board.getPiece(xPos, yPos) == null) {
                this.validMoves[xPos][yPos] = -1;
            } else if(board.getPiece(xPos, yPos).isWhite != isWhite) {
                this.validMoves[xPos][yPos] = 2;
                availableKills.add(board.getPiece(xPos, yPos));
            }
        }
        xPos = xPosition;
        yPos = yPosition;
        xPos += 2;
        yPos -= 1;
        if((xPos < 8) && (yPos >= 0)) {
            if( board.getPiece(xPos, yPos) == null) {
                this.validMoves[xPos][yPos] = -1;
            } else if(board.getPiece(xPos, yPos).isWhite != isWhite) {
                this.validMoves[xPos][yPos] = 2;
                availableKills.add(board.getPiece(xPos, yPos));
            }
        }
    }

    protected void leftL(int xPosition, int yPosition, Board board) {
        int xPos = xPosition;
        int yPos = yPosition;
        xPos -= 2;
        yPos += 1;
        if((xPos >= 0) && (yPos < 8)) {
            if(board.getPiece(xPos, yPos) == null) {
                this.validMoves[xPos][yPos] = -1;
            } else if(board.getPiece(xPos, yPos).isWhite != isWhite) {
                this.validMoves[xPos][yPos] = 2;
                availableKills.add(board.getPiece(xPos, yPos));
            }
        }
        xPos = xPosition;
        yPos = yPosition;
        xPos -= 2;
        yPos -= 1;
        if((xPos >= 0) && (yPos >= 0)) {
            if( board.getPiece(xPos, yPos) == null) {
                this.validMoves[xPos][yPos] = -1;
            } else if(board.getPiece(xPos, yPos).isWhite != isWhite) {
                this.validMoves[xPos][yPos] = 2;
                availableKills.add(board.getPiece(xPos, yPos));
            }
        }
    }

    private void findType(String name) {
        switch (this.name) {
            case "pawn" :
                this.pieceType = 1;
                return;
            case "rook" :
                this.pieceType = 2;
                return;
            case "bishop" :
                this.pieceType = 3;
                return;
            case "knight" :
                this.pieceType = 4;
                return;
            case "king" :
                this.pieceType = 5;
                return;
            case "queen" :
                this.pieceType = 6;
                return;
            default :
                return;
        }
    }
}
