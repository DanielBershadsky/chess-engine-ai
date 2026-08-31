import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// A lightweight chess engine that picks a move for one color using minimax search with alpha
// beta pruning, built entirely on top of MoveGenerator's existing move rules and check
// detection. It does not add any new chess rules of its own, it only searches ahead through
// legal moves and scores the resulting positions.
public class ChessAI
{
	private Board board;
	private MoveGenerator moveGen;
	private int maxDepth;

	private static final int PAWN_VALUE=100, KNIGHT_VALUE=320, BISHOP_VALUE=330, ROOK_VALUE=500, QUEEN_VALUE=900, KING_VALUE=20000;
	private static final int CHECKMATE_SCORE=1000000;
	private static final int EXACT=0, LOWER=1, UPPER=2;

	// Standard piece square tables (values in centipawns) that reward pieces for sitting on
	// generally useful squares, on top of plain material count. Written below in the common
	// rank 8 down to rank 1 order found in most chess programming references, then flipped once
	// by reverseRows so index 0 lines up with Board's own row 0, which is rank 1.
	private static final int[][] PAWN_TABLE = reverseRows(new int[][] {
		{ 0,  0,  0,  0,  0,  0,  0,  0},
		{50, 50, 50, 50, 50, 50, 50, 50},
		{10, 10, 20, 30, 30, 20, 10, 10},
		{ 5,  5, 10, 25, 25, 10,  5,  5},
		{ 0,  0,  0, 20, 20,  0,  0,  0},
		{ 5, -5,-10,  0,  0,-10, -5,  5},
		{ 5, 10, 10,-20,-20, 10, 10,  5},
		{ 0,  0,  0,  0,  0,  0,  0,  0}
	});

	private static final int[][] KNIGHT_TABLE = reverseRows(new int[][] {
		{-50,-40,-30,-30,-30,-30,-40,-50},
		{-40,-20,  0,  0,  0,  0,-20,-40},
		{-30,  0, 10, 15, 15, 10,  0,-30},
		{-30,  5, 15, 20, 20, 15,  5,-30},
		{-30,  0, 15, 20, 20, 15,  0,-30},
		{-30,  5, 10, 15, 15, 10,  5,-30},
		{-40,-20,  0,  5,  5,  0,-20,-40},
		{-50,-40,-30,-30,-30,-30,-40,-50}
	});

	private static final int[][] BISHOP_TABLE = reverseRows(new int[][] {
		{-20,-10,-10,-10,-10,-10,-10,-20},
		{-10,  0,  0,  0,  0,  0,  0,-10},
		{-10,  0,  5, 10, 10,  5,  0,-10},
		{-10,  5,  5, 10, 10,  5,  5,-10},
		{-10,  0, 10, 10, 10, 10,  0,-10},
		{-10, 10, 10, 10, 10, 10, 10,-10},
		{-10,  5,  0,  0,  0,  0,  5,-10},
		{-20,-10,-10,-10,-10,-10,-10,-20}
	});

	private static final int[][] ROOK_TABLE = reverseRows(new int[][] {
		{ 0,  0,  0,  0,  0,  0,  0,  0},
		{ 5, 10, 10, 10, 10, 10, 10,  5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{ 0,  0,  0,  5,  5,  0,  0,  0}
	});

	private static final int[][] QUEEN_TABLE = reverseRows(new int[][] {
		{-20,-10,-10, -5, -5,-10,-10,-20},
		{-10,  0,  0,  0,  0,  0,  0,-10},
		{-10,  0,  5,  5,  5,  5,  0,-10},
		{ -5,  0,  5,  5,  5,  5,  0, -5},
		{  0,  0,  5,  5,  5,  5,  0, -5},
		{-10,  5,  5,  5,  5,  5,  0,-10},
		{-10,  0,  5,  0,  0,  0,  0,-10},
		{-20,-10,-10, -5, -5,-10,-10,-20}
	});

	// A middle game king table that rewards staying tucked behind the back rank pawns and
	// penalizes wandering into the center, where the king is exposed to more attacks.
	private static final int[][] KING_TABLE = reverseRows(new int[][] {
		{-30,-40,-40,-50,-50,-40,-40,-30},
		{-30,-40,-40,-50,-50,-40,-40,-30},
		{-30,-40,-40,-50,-50,-40,-40,-30},
		{-30,-40,-40,-50,-50,-40,-40,-30},
		{-20,-30,-30,-40,-40,-30,-30,-20},
		{-10,-20,-20,-20,-20,-20,-20,-10},
		{ 20, 20,  0,  0,  0,  0, 20, 20},
		{ 20, 30, 10,  0,  0, 10, 30, 20}
	});

	private static int[][] reverseRows(int[][] table)
	{
		int[][] flipped = new int[8][8];
		for(int r=0; r<8; r++)
		{
			flipped[r] = table[7-r];
		}
		return flipped;
	}

	private HashMap<String, TTEntry> transpositionTable = new HashMap<String, TTEntry>();

	public ChessAI(Board board, MoveGenerator moveGen, int depth)
	{
		this.board = board;
		this.moveGen = moveGen;
		this.maxDepth = depth;
	}

	// A single candidate move, used only inside the AI's own search. It is deliberately separate
	// from the row and column ArrayList style the rest of the project uses for move lists, since
	// the search needs to keep the four coordinates of one move together as it sorts and recurses.
	private static class Move
	{
		int fRow, fColumn, sRow, sColumn;
		Move(int fRow, int fColumn, int sRow, int sColumn)
		{
			this.fRow=fRow;
			this.fColumn=fColumn;
			this.sRow=sRow;
			this.sColumn=sColumn;
		}
	}

	// One cached search result. flag records whether score is the exact value for that position,
	// or only a lower or upper bound, because alpha beta pruning can cut a search short before
	// its true value is fully known.
	private static class TTEntry
	{
		int depth;
		int score;
		int flag;
		TTEntry(int depth, int score, int flag)
		{
			this.depth=depth;
			this.score=score;
			this.flag=flag;
		}
	}

	// Picks the best move for this color by running minimax from the root and returns
	// {FRow, FColumn, SRow, SColumn}, the same four coordinates Game.move() already works with
	// for a human's chosen move. The transposition table is cleared at the start of each call
	// since it should only cache results within one search, not carry stale entries between the
	// very different positions that come up move to move over the course of a real game.
	public int[] findBestMove(String[][] currentBoard, String color)
	{
		transpositionTable.clear();
		boolean maximizing = color.equals("W");
		String enemy = color.equals("W") ? "B" : "W";
		List<Move> moves = generateLegalMoves(currentBoard, color);
		orderMoves(currentBoard, moves);

		Move bestMove = null;
		int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
		int best = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for(int i=0; i<moves.size(); i++)
		{
			Move move = moves.get(i);
			String next[][] = applyMove(currentBoard, move.fRow, move.fColumn, move.sRow, move.sColumn);
			int score = minimax(next, maxDepth-1, alpha, beta, enemy, !maximizing);
			if(maximizing)
			{
				if((bestMove==null)||(score>best))
				{
					best=score;
					bestMove=move;
				}
				if(best>alpha)
				{
					alpha=best;
				}
			}
			else
			{
				if((bestMove==null)||(score<best))
				{
					best=score;
					bestMove=move;
				}
				if(best<beta)
				{
					beta=best;
				}
			}
		}
		if(bestMove==null)
		{
			return null;
		}
		return new int[] {bestMove.fRow, bestMove.fColumn, bestMove.sRow, bestMove.sColumn};
	}

	// Classic minimax with alpha beta pruning. maximizing is true when it is White's turn to
	// move, since evaluate() always scores a position from White's perspective. Every enemy
	// piece's checking status is asked about through moveGen.KingInCheck, the same function
	// Game.move() already relies on, so a checkmate here means exactly what it means there.
	private int minimax(String[][] currentBoard, int depth, int alpha, int beta, String colorToMove, boolean maximizing)
	{
		ArrayList<Integer> checkStatus = moveGen.KingInCheck(currentBoard, colorToMove);
		int code = checkStatus.get(checkStatus.size()-1);
		if(code==44)
		{
			// Faster mates score higher than slower ones, found deeper in the search, so the AI
			// prefers the quickest forced mate available instead of a needlessly slow one.
			int mateScore = CHECKMATE_SCORE + depth;
			return maximizing ? -mateScore : mateScore;
		}
		if(depth==0)
		{
			return evaluate(currentBoard);
		}

		String key = boardKey(currentBoard, colorToMove, depth);
		TTEntry cached = transpositionTable.get(key);
		if((cached!=null)&&(cached.depth>=depth))
		{
			if(cached.flag==EXACT)
			{
				return cached.score;
			}
			if((cached.flag==LOWER)&&(cached.score>alpha))
			{
				alpha=cached.score;
			}
			if((cached.flag==UPPER)&&(cached.score<beta))
			{
				beta=cached.score;
			}
			if(alpha>=beta)
			{
				return cached.score;
			}
		}

		List<Move> moves = generateLegalMoves(currentBoard, colorToMove);
		if(moves.isEmpty())
		{
			// Not checkmate (that was already ruled out above) but no legal moves means
			// stalemate, a draw, scored as neutral.
			return 0;
		}
		orderMoves(currentBoard, moves);

		String enemy = colorToMove.equals("W") ? "B" : "W";
		int originalAlpha=alpha;
		int best = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for(int i=0; i<moves.size(); i++)
		{
			Move move = moves.get(i);
			String next[][] = applyMove(currentBoard, move.fRow, move.fColumn, move.sRow, move.sColumn);
			int score = minimax(next, depth-1, alpha, beta, enemy, !maximizing);
			if(maximizing)
			{
				if(score>best)
				{
					best=score;
				}
				if(best>alpha)
				{
					alpha=best;
				}
			}
			else
			{
				if(score<best)
				{
					best=score;
				}
				if(best<beta)
				{
					beta=best;
				}
			}
			if(beta<=alpha)
			{
				break;
			}
		}

		int flag;
		if(best<=originalAlpha)
		{
			flag=UPPER;
		}
		else if(best>=beta)
		{
			flag=LOWER;
		}
		else
		{
			flag=EXACT;
		}
		transpositionTable.put(key, new TTEntry(depth, best, flag));
		return best;
	}

	// Every legal destination for every piece of this color, already filtered so that making the
	// move never leaves this color's own king in check. This mirrors the clone the board, apply
	// the move, call KingInCheck, check the trailing code pattern Game.move() already uses (see
	// the check in check verification right after a human enters a move), just run here for every
	// candidate instead of only the one a human already picked.
	private List<Move> generateLegalMoves(String[][] currentBoard, String color)
	{
		List<Move> legalMoves = new ArrayList<Move>();
		for(int r=0; r<8; r++)
		{
			for(int c=0; c<8; c++)
			{
				if(currentBoard[r][c].substring(0,1).equals(color))
				{
					ArrayList<Integer> destinations = moveGen.LegalMoves(r, c, currentBoard, false);
					for(int i=0; i<destinations.size(); i+=2)
					{
						int sRow=destinations.get(i), sColumn=destinations.get(i+1);
						if(sRow==88)
						{
							continue;
						}
						String simulated[][] = applyMove(currentBoard, r, c, sRow, sColumn);
						ArrayList<Integer> stillInCheck = moveGen.KingInCheck(simulated, color);
						if(stillInCheck.get(stillInCheck.size()-1)>10)
						{
							continue;
						}
						legalMoves.add(new Move(r, c, sRow, sColumn));
					}
				}
			}
		}
		return legalMoves;
	}

	// Clones the board and applies one move on the copy, leaving the original untouched. The
	// vacated square is always set to plain empty rather than restoring the checkerboard
	// background color, which is fine since MoveGenerator already treats both empty markers the
	// same everywhere, and this board copy only ever lives inside the AI's own search.
	private String[][] applyMove(String[][] currentBoard, int fRow, int fColumn, int sRow, int sColumn)
	{
		String next[][] = new String[8][8];
		board.equalBoard(currentBoard, next);
		next[sRow][sColumn] = next[fRow][fColumn];
		next[fRow][fColumn] = "  ";
		return next;
	}

	// Sorts captures ahead of quiet moves, most valuable captured piece first. Alpha beta pruning
	// cuts off more of the tree the earlier a strong move is tried, so trying likely captures
	// before quiet moves tends to find good cutoffs sooner.
	private void orderMoves(String[][] currentBoard, List<Move> moves)
	{
		final String[][] boardForOrdering = currentBoard;
		Collections.sort(moves, new Comparator<Move>()
		{
			public int compare(Move a, Move b)
			{
				return moveOrderScore(boardForOrdering, b) - moveOrderScore(boardForOrdering, a);
			}
		});
	}

	private int moveOrderScore(String[][] currentBoard, Move move)
	{
		String target = currentBoard[move.sRow][move.sColumn];
		if(target.equals("  ")||target.equals("##"))
		{
			return 0;
		}
		return pieceValue(target);
	}

	// Scores a position from White's perspective, positive is good for White, negative is good
	// for Black. Combines material, piece square positioning, a cheap mobility estimate and a
	// small king safety bonus for pawns sheltering the king.
	private int evaluate(String[][] currentBoard)
	{
		int score=0;
		for(int r=0; r<8; r++)
		{
			for(int c=0; c<8; c++)
			{
				String square=currentBoard[r][c];
				if(square.equals("  ")||square.equals("##"))
				{
					continue;
				}
				String color=square.substring(0,1);
				String type=square.substring(1);
				int value=pieceValue(square);
				int[][] table=tableFor(type);
				int positional = color.equals("W") ? table[r][c] : table[7-r][c];
				if(color.equals("W"))
				{
					score += value + positional;
				}
				else
				{
					score -= value + positional;
				}
			}
		}
		score += mobilityScore(currentBoard, "W") - mobilityScore(currentBoard, "B");
		score += kingSafetyScore(currentBoard, "W") - kingSafetyScore(currentBoard, "B");
		return score;
	}

	// A cheap mobility estimate built from the raw destination count LegalMoves already computes,
	// without the extra own king safety filtering generateLegalMoves does for actually picking a
	// move. That filtering is only needed when a move might really be played, not for scoring
	// every piece on the board purely to rate how active it is.
	private int mobilityScore(String[][] currentBoard, String color)
	{
		int count=0;
		for(int r=0; r<8; r++)
		{
			for(int c=0; c<8; c++)
			{
				if(currentBoard[r][c].substring(0,1).equals(color))
				{
					ArrayList<Integer> destinations = moveGen.LegalMoves(r, c, currentBoard, false);
					for(int i=0; i<destinations.size(); i+=2)
					{
						if(destinations.get(i)!=88)
						{
							count++;
						}
					}
				}
			}
		}
		return count*2;
	}

	// A simple pawn shield bonus, friendly pawns sitting directly in front of the king are worth
	// a little extra safety, since a king with an open path in front of it is easier to attack.
	private int kingSafetyScore(String[][] currentBoard, String color)
	{
		int dir=color.equals("W") ? 1 : -1;
		int bonus=0;
		for(int r=0; r<8; r++)
		{
			for(int c=0; c<8; c++)
			{
				if(currentBoard[r][c].equals(color+"K"))
				{
					for(int dc=-1; dc<=1; dc++)
					{
						int pr=r+dir, pc=c+dc;
						if((pr>-1)&&(pr<8)&&(pc>-1)&&(pc<8))
						{
							if(currentBoard[pr][pc].equals(color+"P"))
							{
								bonus += 10;
							}
						}
					}
				}
			}
		}
		return bonus;
	}

	// A text key describing a position well enough for the transposition table, the side to move
	// and the remaining search depth, since a position searched shallower is worth less to reuse
	// than one already searched as deep or deeper. Both empty square markers collapse to the same
	// text here, since they mean the same thing everywhere else in this project.
	private String boardKey(String[][] currentBoard, String colorToMove, int depth)
	{
		StringBuilder key = new StringBuilder(140);
		for(int r=0; r<8; r++)
		{
			for(int c=0; c<8; c++)
			{
				String square = currentBoard[r][c];
				if(square.equals("  ")||square.equals("##"))
				{
					key.append("..");
				}
				else
				{
					key.append(square);
				}
			}
		}
		key.append(colorToMove).append(depth);
		return key.toString();
	}

	private int pieceValue(String pieceCode)
	{
		String type=pieceCode.substring(1);
		if(type.equals("P")) return PAWN_VALUE;
		if(type.equals("H")) return KNIGHT_VALUE;
		if(type.equals("B")) return BISHOP_VALUE;
		if(type.equals("R")) return ROOK_VALUE;
		if(type.equals("Q")) return QUEEN_VALUE;
		return KING_VALUE;
	}

	private int[][] tableFor(String type)
	{
		if(type.equals("P")) return PAWN_TABLE;
		if(type.equals("H")) return KNIGHT_TABLE;
		if(type.equals("B")) return BISHOP_TABLE;
		if(type.equals("R")) return ROOK_TABLE;
		if(type.equals("Q")) return QUEEN_TABLE;
		return KING_TABLE;
	}
}
