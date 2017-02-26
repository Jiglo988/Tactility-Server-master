package org.hyperion.rs2.model.content.checkers;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;

public class CheckersGame {
	int[] pieceSlot = new int[64], piecesLeft = new int[2], playerPiece = new int[2];

	String playerTurn;

	Player playerOne;
	Player playerTwo;

	boolean pieceTaken = false;

	public CheckersGame(Player p, Player o) {
		int p1Slot[] = {0, 2, 4, 6, 9, 11, 13, 15, 16, 18, 20, 22};
		int p2Slot[] = {41, 43, 45, 47, 48, 50, 52, 54, 57, 59, 61, 63};
		for(int i = 0; i < 64; i++) {
			pieceSlot[i] = - 1;
		}
		for(int i = 0; i < 64; i++) {
			for(int j : p1Slot) {
				if(i == j) {
					pieceSlot[i] = 7096;
				}
			}
			for(int k : p2Slot) {
				if(i == k) {
					pieceSlot[i] = 7097;
				}
			}
			p.getActionSender().sendUpdateItem(10494, pieceSlot[i], new Item(i, 1));
			o.getActionSender().sendUpdateItem(10494, pieceSlot[i], new Item(i, 1));
		}
		int id[] = {10499, 10498, 10492};
		for(int i : id) {
			p.getActionSender().sendString("", i);
			o.getActionSender().sendString("", i);
		}
		p.getActionSender().sendString(o.getName(), 10457);
		p.getActionSender().sendString(p.getName(), 10448);
		p.getActionSender().sendString(p.getName(), 10479);
		o.getActionSender().sendString(o.getName(), 10457);
		o.getActionSender().sendString(p.getName(), 10448);
		o.getActionSender().sendString(p.getName(), 10479);
		this.playerOne = p;
		this.playerTwo = o;
		this.playerTurn = p.getName();
		this.playerPiece[0] = 7096;
		this.playerPiece[1] = 7097;
		this.piecesLeft[0] = 16;
		this.piecesLeft[1] = 16;
	}

	public Player getPlayerOne() {
		return playerOne;
	}

	public Player getPlayerTwo() {
		return playerTwo;
	}

	public int[] getPlayerPiece() {
		return playerPiece;
	}

	public int getPlayerPiece(int idx) {
		return playerPiece[idx];
	}

	public void setPlayerPiece(int idx, int value) {
		this.playerPiece[idx] = value;
	}

	public int[] getPieces() {
		return pieceSlot;
	}

	public int[] getPiecesLeft() {
		return piecesLeft;
	}

	public int getPiecesLeft(int idx) {
		return piecesLeft[idx];
	}

	public void setPiecesLeft(int idx, int value) {
		this.piecesLeft[idx] = value;
	}

	public void setPieceSlot(int idx, int value) {
		pieceSlot[idx] = value;
	}

	public void subtractPiece(int idx) {
		piecesLeft[idx]--;
	}

	public void setPiece(int i, int j) {
		pieceSlot[j] = pieceSlot[i];
		pieceSlot[i] = - 1;
	}

	public int getPiece(int i) {
		return pieceSlot[i];
	}

	public boolean getPieceTaken() {
		return pieceTaken;
	}

	public void setPieceTaken(boolean pieceTaken) {
		this.pieceTaken = pieceTaken;
	}
}
