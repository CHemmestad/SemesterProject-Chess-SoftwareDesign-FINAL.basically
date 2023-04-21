import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class ChessGame {
    public static Piece selectedPiece;
    public static int boardSelection = 0;
    public static Image[] gameStatus = new Image[2];

    public static void main(String[] Args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int borderDecoration = ((int)screenSize.getHeight()-(int)screenSize.getHeight()%10)/15;
        int boardHeight = (int)screenSize.getHeight()-(int)screenSize.getHeight()%10;
        int boardWidth = (int)screenSize.getWidth()-(int)screenSize.getWidth()%10;
        int sideX = boardHeight-boardHeight/4;
        int sideY = sideX;
        int pieceSize = sideX/8;
        int boardSize = sideX+borderDecoration*2;

        try {
            gameStatus[0] = ImageIO.read(new File("Images/WhiteWins.png")).getScaledInstance(sideX, sideY, Image.SCALE_SMOOTH);
            gameStatus[1] = ImageIO.read(new File("Images/BlackWins.png")).getScaledInstance(sideX, sideY, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }


        JFrame frame = new JFrame();
        frame.setBounds(boardWidth/2-(boardSize)/2, boardHeight/2-(boardSize)/2, boardSize, boardSize);
        frame.setUndecorated(true);

        Board chessBoard = new Board(pieceSize, boardSize);
        //White pieces
        for(int x = 0; x < 8; x++) {
            chessBoard.addPiece(new Pawn("pawn", "Images/PawnWnew.png", true, pieceSize), x, 6, true);
        }
        chessBoard.addPiece(new Rook("rook", "Images/RookWnew.png", true, pieceSize), 0, 7, true);
        chessBoard.addPiece(new Rook("rook", "Images/RookWnew.png", true, pieceSize), 7, 7, true);
        chessBoard.addPiece(new Knight("knight", "Images/KnightWnew.png", true, pieceSize), 1, 7, true);
        chessBoard.addPiece(new Knight("knight", "Images/KnightWnew.png", true, pieceSize), 6, 7, true);
        chessBoard.addPiece(new Bishop("bishop", "Images/BishopWnew.png", true, pieceSize), 2, 7, true);
        chessBoard.addPiece(new Bishop("bishop", "Images/BishopWnew.png", true, pieceSize), 5, 7, true);
        chessBoard.addPiece(new King("king", "Images/KingWnew.png", true, pieceSize), 3, 7, true);
        chessBoard.addPiece(new Queen("queen", "Images/QueenWnew.png", true, pieceSize), 4, 7, true);

        //Black pieces
        for(int x = 0; x < 8; x++) {
            chessBoard.addPiece(new Pawn("pawn", "Images/PawnBnew.png", false, pieceSize), x, 1, true);
        }
        chessBoard.addPiece(new Rook("rook", "Images/RookBnew.png", false, pieceSize), 0, 0, true);
        chessBoard.addPiece(new Rook("rook", "Images/RookBnew.png", false, pieceSize), 7, 0, true);
        chessBoard.addPiece(new Knight("knight", "Images/KnightBnew.png", false, pieceSize), 1, 0, true);
        chessBoard.addPiece(new Knight("knight", "Images/KnightBnew.png", false, pieceSize), 6, 0, true);
        chessBoard.addPiece(new Bishop("bishop", "Images/BishopBnew.png", false, pieceSize), 2, 0, true);
        chessBoard.addPiece(new Bishop("bishop", "Images/BishopBnew.png", false, pieceSize), 5, 0, true);
        chessBoard.addPiece(new King("king", "Images/KingBnew.png", false, pieceSize), 3, 0, true);
        chessBoard.addPiece(new Queen("queen", "Images/QueenBnew.png", false, pieceSize), 4, 0, true);

        
        JPanel panel = new JPanel()
        {
            @Override
            public void paint(Graphics g) {
                boolean white = true;
                g.drawImage(chessBoard.chessBoards[boardSelection], 0, 0, this);
                //chessBoard.checkGameStatus();
                for ( int y = 0 ; y < 8 ; y++ ){
                    for ( int x = 0 ; x < 8 ; x++ ){
                        Piece current = chessBoard.getPiece(x, y);
                        if (white) {
                            //HSB COLOR PEN https://codepen.io/HunorMarton/full/eWvewo
                            g.drawImage(chessBoard.moveColors[4], borderDecoration+x*pieceSize, borderDecoration+y*pieceSize, this);
                            //g.setColor(Color.white);
                            //g.setColor(new Color(x, y, x));
                        } else {
                            g.drawImage(chessBoard.moveColors[3], borderDecoration+x*pieceSize, borderDecoration+y*pieceSize, this);
                            //g.setColor(Color.black);
                        }
                        //g.fillRect(borderDecoration+x*pieceSize, borderDecoration+y*pieceSize, pieceSize, pieceSize);
                        white =! white;
                        if(selectedPiece != null) {
                            if(selectedPiece.validMoves[x][y] == -1) {
                                g.drawImage(chessBoard.moveColors[0], borderDecoration+x*pieceSize, borderDecoration+y*pieceSize, this);
                            }
                            if(selectedPiece.validMoves[x][y] == 2) {
                                g.drawImage(chessBoard.moveColors[1], borderDecoration+x*pieceSize, borderDecoration+y*pieceSize, this);
                            }
                        }
                        if(current != null) {
                            if(chessBoard.isPlayerChecked()) {
                                if(current.getPieceType() == 5) {
                                    if(((King)current).isChecked()) {
                                        //g.drawImage(chessBoard.moveColors[1], borderDecoration+x*pieceSize, borderDecoration+y*pieceSize, this);
                                        g.drawImage(current.getImage()[0], borderDecoration+current.getPosX()*pieceSize, borderDecoration+current.getPosY()*pieceSize, this);
                                        g.drawImage(chessBoard.moveColors[2], borderDecoration+(x*pieceSize), borderDecoration+(y*pieceSize), this);
                                    } else {
                                        g.drawImage(current.getImage()[0], borderDecoration+current.getPosX()*pieceSize, borderDecoration+current.getPosY()*pieceSize, this);
                                    }
                                } else {
                                    if(chessBoard.blockingPieces.contains(current)) {
                                        g.drawImage(chessBoard.moveColors[5], borderDecoration+(x*pieceSize), borderDecoration+(y*pieceSize), this);
                                    }
                                    g.drawImage(current.getImage()[0], borderDecoration+current.getPosX()*pieceSize, borderDecoration+current.getPosY()*pieceSize, this);
                                }
                            } else {
                                g.drawImage(current.getImage()[0], borderDecoration+current.getPosX()*pieceSize, borderDecoration+current.getPosY()*pieceSize, this);
                            }
                        }
                    }
                    white =! white; 
                }
                if(chessBoard.isGameOver()) {
                    if(chessBoard.getWinner() == "white") {
                        g.drawImage(gameStatus[0], borderDecoration, borderDecoration, this);
                    } else {
                        g.drawImage(gameStatus[1], borderDecoration, borderDecoration, this);
                    }
                }
            }
        };
        frame.add(panel);

        frame.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!chessBoard.isGameOver()) {
                    frame.repaint();
                    int xPos = (e.getX()-(borderDecoration))/pieceSize;
                    int yPos = (e.getY()-(borderDecoration))/pieceSize;
                    if(selectedPiece != null) {
                        if(selectedPiece.validMoves[xPos][yPos] == -1) {
                            chessBoard.movePiece(selectedPiece, xPos, yPos);
                        } else if(selectedPiece.validMoves[xPos][yPos] == 2) {
                            chessBoard.killPiece(selectedPiece, xPos, yPos);
                        }
                    } else if(chessBoard.getPiece(xPos, yPos) == null) {
                        chessBoard.addPiece(new Queen("queen", "Images/QueenWnew.png", true, pieceSize), xPos, yPos, true);
                    }
                    if(chessBoard.getPiece(xPos, yPos) != null) {
                        if(chessBoard.getPiece(xPos, yPos).isWhite == chessBoard.getTurn()) {
                            if(selectedPiece != chessBoard.getPiece(xPos, yPos)) {
                                System.out.println("getting piece");
                                selectedPiece = chessBoard.getPiece(xPos, yPos);
                                selectedPiece.moveCheck(xPos, yPos, chessBoard);
                            } else {
                                selectedPiece = null;
                            }
                        } else {
                            selectedPiece = null;
                        }
                    } else {
                        selectedPiece = null;
                    }
                }
                chessBoard.checkGameStatus();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("mouse pressed ------------------------------------------------------------------------------------------------------------------------------------");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("mouse released");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("mouse entered");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(boardSelection < 7) {
                    boardSelection++;
                } else {
                    boardSelection = 0;
                }
                frame.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
            
        });
        frame.setDefaultCloseOperation(0);
        frame.setVisible(true);
    }
}