package de.samdev.absgdx.example.chessgame.piece;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.samdev.absgdx.example.chessgame.ChessLayer;
import de.samdev.absgdx.example.chessgame.ChessMoveType;
import de.samdev.absgdx.example.chessgame.Vector2i;
import de.samdev.absgdx.framework.entities.SimpleEntity;

public abstract class ChessPiece extends SimpleEntity {

	public final int player;
	
	private float moveSignumY;
	private float moveSignumX;

	protected int boardPosX;
	protected int boardPosY;
	
	public ChessPiece(TextureRegion tex, int player, int x, int y) {
		super(tex, 1, 2);
		moveSignumY = 0;
		moveSignumX = 0;
		
		boardPosX = x;
		boardPosY = y;
		
		this.player = player;
		
		this.setPosition(x+1, y+1);
		
		this.setZLayer(-boardPosY);
	}

	@Override
	public void beforeUpdate(float delta) {
		if (layer == null) return;

		float targetY = (boardPosY+1);
		float targetX = (boardPosX+1);
		
		if (getPositionY() != targetY) {
			if (Math.signum(targetY - getPositionY()) != moveSignumY) {
				setPositionY(targetY);
				moveSignumY = 0;
				speed.y = 0;
			} else {
				speed.y = Math.signum(targetY - getPositionY()) * 0.005f;				
			}
		} else {
			speed.y = 0;
		}
		
		if (getPositionX() != targetX) {
			if (Math.signum(targetX - getPositionX()) != moveSignumX) {
				setPositionX(targetX);
				moveSignumX = 0;
				speed.x = 0;
			} else {
				speed.x = Math.signum(targetX - getPositionX()) * 0.005f;				
			}
		} else {
			speed.x = 0;
		}
	}

	public void movePiece(int dx, int dy) {
		ChessLayer clayer = (ChessLayer)this.layer;
		
		clayer.board[boardPosX][boardPosY] = null;
		if (clayer.board[boardPosX+dx][boardPosY+dy] != null) clayer.board[boardPosX+dx][boardPosY+dy].alive = false;
		clayer.board[boardPosX+dx][boardPosY+dy] = this;
		
		boardPosX += dx;
		boardPosY += dy;

		moveSignumX = Math.signum((boardPosX+1) - getPositionX());
		moveSignumY = Math.signum((boardPosY+1) - getPositionY());

		this.setZLayer(-boardPosY);
	}
	
	public Vector2i getBoardPos() {
		return new Vector2i(boardPosX, boardPosY);
	}
	
	public boolean isValidMove(Vector2i pos, ChessMoveType mtype) {
		ChessLayer clayer = (ChessLayer)this.layer;
		
		Vector2i newpos = getBoardPos().add(pos);
		
		if (isValidPosition(newpos)) {
			ChessPiece cp = clayer.board[newpos.x][newpos.y];
			
			if ((mtype == ChessMoveType.ANY || mtype == ChessMoveType.MOVE) && cp == null) return true;
			if (mtype == ChessMoveType.KILL && cp == null) return false;
			
			if ((mtype == ChessMoveType.ANY || mtype == ChessMoveType.KILL) && cp.player != this.player && !(cp instanceof ChessKing)) return true;
		}
		
		return false;
	}
	
	public boolean isValidPosition(Vector2i pos) {
		return pos.x >= 0 && pos.y >= 0 && pos.x < 8 && pos.y < 8;
	}
	
	public abstract List<Vector2i> getMoves();
	
	@Override
	public void onPointerUp() {
//		for (Vector2i m : getMoves()) {
//			System.out.println(getClass().getSimpleName() + ": {" + m.x + "|" + m.y + "}");
//		}
//		if (getMoves().size() == 0)
//			System.out.println(getClass().getSimpleName() + ": ---");
//		
//		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
//			List<Vector2i> moves = getMoves();
//			Collections.shuffle(moves);
//
//			if (moves.size() > 0) {
//				movePiece(moves.get(0).x, moves.get(0).y);
//			}
//		}
//		
//		System.out.println();
	}
}
