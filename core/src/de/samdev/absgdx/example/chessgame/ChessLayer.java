package de.samdev.absgdx.example.chessgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import de.samdev.absgdx.example.Textures;
import de.samdev.absgdx.example.chessgame.piece.ChessBishop;
import de.samdev.absgdx.example.chessgame.piece.ChessKing;
import de.samdev.absgdx.example.chessgame.piece.ChessKnight;
import de.samdev.absgdx.example.chessgame.piece.ChessPawn;
import de.samdev.absgdx.example.chessgame.piece.ChessPiece;
import de.samdev.absgdx.example.chessgame.piece.ChessQueen;
import de.samdev.absgdx.example.chessgame.piece.ChessRook;
import de.samdev.absgdx.framework.AgdxGame;
import de.samdev.absgdx.framework.layer.GameLayer;
import de.samdev.absgdx.framework.map.TileMap;
import de.samdev.absgdx.framework.map.background.RepeatingBackground;
import de.samdev.absgdx.framework.map.mapscaleresolver.ShowCompleteMapScaleResolver;

public class ChessLayer extends GameLayer {

	float deltasum = 0;
	float targettime = 2500;
	int targetplayer = 0;
	Random random = new Random();
	
	public ChessPiece[][] board = new ChessPiece[8][8];
	
	public List<List<ChessPiece>> pieces = new ArrayList<List<ChessPiece>>();
	
	public ChessLayer(AgdxGame owner) {
		super(owner, createMap());

		pieces.add(new ArrayList<ChessPiece>());
		pieces.add(new ArrayList<ChessPiece>());
		
		init();
		
		setMapScaleResolver(new ShowCompleteMapScaleResolver());

		setRawOffset(new Vector2(-(getVisibleMapBox().width - getMap().width)/2, -(getVisibleMapBox().height - getMap().height)/2));
	}
	
	@Override
	public void onResize() {
		super.onResize();
		
		setRawOffset(new Vector2(-(getVisibleMapBox().width - getMap().width)/2, -(getVisibleMapBox().height - getMap().height)/2));
	}

	private static TileMap createMap() {
		TileMap tm = new TileMap(0XA, 0xA);
		
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				if ((x%9) * (y%9) == 0) tm.setTile(x, y, new ChessTile(2));
				else tm.setTile(x, y, new ChessTile((x%2) ^ (y%2)));
			}
		}
		
		return tm;
	}

	private void init() {
		addPiece(new ChessRook  (0, 0, 0));
		addPiece(new ChessKnight(0, 1, 0));
		addPiece(new ChessBishop(0, 2, 0));
		addPiece(new ChessQueen (0, 3, 0));
		addPiece(new ChessKing  (0, 4, 0));
		addPiece(new ChessBishop(0, 5, 0));
		addPiece(new ChessKnight(0, 6, 0));
		addPiece(new ChessRook  (0, 7, 0));

		addPiece(new ChessPawn(0, 0, 1));
		addPiece(new ChessPawn(0, 1, 1));
		addPiece(new ChessPawn(0, 2, 1));
		addPiece(new ChessPawn(0, 3, 1));
		addPiece(new ChessPawn(0, 4, 1));
		addPiece(new ChessPawn(0, 5, 1));
		addPiece(new ChessPawn(0, 6, 1));
		addPiece(new ChessPawn(0, 7, 1));

		addPiece(new ChessRook  (1, 0, 7));
		addPiece(new ChessKnight(1, 1, 7));
		addPiece(new ChessBishop(1, 2, 7));
		addPiece(new ChessQueen (1, 3, 7));
		addPiece(new ChessKing  (1, 4, 7));
		addPiece(new ChessBishop(1, 5, 7));
		addPiece(new ChessKnight(1, 6, 7));
		addPiece(new ChessRook  (1, 7, 7));

		addPiece(new ChessPawn(1, 0, 6));
		addPiece(new ChessPawn(1, 1, 6));
		addPiece(new ChessPawn(1, 2, 6));
		addPiece(new ChessPawn(1, 3, 6));
		addPiece(new ChessPawn(1, 4, 6));
		addPiece(new ChessPawn(1, 5, 6));
		addPiece(new ChessPawn(1, 6, 6));
		addPiece(new ChessPawn(1, 7, 6));
		
		addBackground(new RepeatingBackground(Textures.tex_chess_tiles[2], 1));
	}

	private void addPiece(ChessPiece chessPiece) {
		addEntity(chessPiece);
		
		pieces.get(chessPiece.player).add(chessPiece);
		
		board[chessPiece.getBoardPos().x][chessPiece.getBoardPos().y] = chessPiece;
	}

	@Override
	public void onUpdate(float delta) {
		deltasum += delta;
		
		if (deltasum > targettime) {
			deltasum = 0;
			targettime = random.nextInt(500)+1000;
			targetplayer = (1-(targetplayer*2-1))/2;

			doPlayerMove(targetplayer);
		}
	}

	private void doPlayerMove(int player) {
		Collections.shuffle(pieces.get(player));
		
		for (ChessPiece cp : pieces.get(player)) {
			List<Vector2i> moves = cp.getMoves();
			
			Collections.shuffle(moves);
			
			for (Vector2i move : moves) {
				cp.movePiece(move.x, move.y);
				
				return;
			}
		}
	}

}
