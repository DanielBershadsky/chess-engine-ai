import java.util.ArrayList;

// Everything to do with figuring out where pieces are allowed to move, whether a king is in
// check, checkmate or stalemate, and what the 88 show legal moves console feature displays.
// Board.java owns the board array itself and printing; Game.java owns the console prompts and
// applying a move once it's confirmed legal.
public class MoveGenerator
{
	private Board board;

	public MoveGenerator(Board board)
	{
		this.board = board;
	}

	// The core move generator: given a square, returns every square that piece could move to,
	// ignoring whether the move would leave your own king in check (that safety check happens
	// later, in Game.move()). Called with takes=true when we only care about which squares a
	// piece attacks for check detection, rather than every square it could legally move to. For
	// example a pawn's forward move isn't an attack, only its diagonal captures are.
	// Results come back as a flat list of row and column pairs. For sliding pieces, rook, bishop
	// and queen, a pair of 88s is inserted between each direction scanned, for example between the
	// up direction and the down direction, as a separator. Other code uses these to figure out
	// which squares lie between an attacking piece and the king when checking whether a check
	// can be blocked.
	public ArrayList<Integer> LegalMoves(int FRow, int FColumn, String[][]Board, boolean takes)
	{
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		rows.clear();
		columns.clear();
		columnsAndRows.clear();
		// PAWN MOVEMENT. White and black pawns move the same way, just in opposite directions, so
		// dir/startRow flip the direction and starting row depending on color instead of writing
		// the white and black cases out separately.
		//white or black PAWNS
		if((Board[FRow][FColumn].equals("WP"))||(Board[FRow][FColumn].equals("BP")))
		{
			String color=Board[FRow][FColumn].substring(0,1);
			String enemy=(color.equals("W"))?"B":"W";
			int dir=(color.equals("W"))?1:-1; // +1 = moving up the board (white), -1 = moving down (black)
			int startRow=(color.equals("W"))?1:6; // row a pawn is allowed to move two squares from
			if(takes)
			{
				//takes
				if((FColumn-1>-1)&&(FRow+dir<8)&&(FRow+dir>-1))
				{
					if(Board[FRow+dir][FColumn-1].substring(0, 1).equals(enemy))
					{
						rows.add(FRow+dir);
						columns.add(FColumn-1);
					}
				}
				if((FColumn+1<8)&&(FRow+dir<8)&&(FRow+dir>-1))
				{
					if(Board[FRow+dir][FColumn+1].substring(0, 1).equals(enemy))
					{
						rows.add(FRow+dir);
						columns.add(FColumn+1);
					}
				}
			}
			else
			{
				// TODO(bugfix pass): missing FRow+dir bounds check here. A pawn that reaches the
				// last rank (promotion isn't implemented yet) crashes the next time this runs (AUDIT.md #2)
				//first move (normal)
				if(((Board[FRow+dir][FColumn].equals("  ")))||((Board[FRow+dir][FColumn].equals("##"))))
				{
					rows.add(FRow+dir);
					columns.add(FColumn);
				}
				//if on starting spot 2 moves up
				if(FRow==startRow)
				{
					if(((Board[FRow+dir][FColumn].equals("  ")))||((Board[FRow+dir][FColumn].equals("##"))))
					{
						if(((Board[FRow+2*dir][FColumn].equals("  ")))||((Board[FRow+2*dir][FColumn].equals("##"))))
						{
							rows.add(FRow+2*dir);
							columns.add(FColumn);
						}
					}
				}
				//takes
				if((FColumn-1>-1)&&(FRow+dir<8)&&(FRow+dir>-1))
				{
					if(Board[FRow+dir][FColumn-1].substring(0, 1).equals(enemy))
					{
						rows.add(FRow+dir);
						columns.add(FColumn-1);
					}
				}
				if((FColumn+1<8)&&(FRow+dir<8)&&(FRow+dir>-1))
				{
					if(Board[FRow+dir][FColumn+1].substring(0, 1).equals(enemy))
					{
						rows.add(FRow+dir);
						columns.add(FColumn+1);
					}
				}
			}
		}
		// KNIGHT MOVEMENT. All 8 knight jumps are checked individually. Knights don't slide, so
		// there's no stop when you hit a piece logic like the rook, bishop and queen below have.
		// Each jump just checks that the square is on the board and not occupied by a piece of
		// the knight's own color.
		//white and black knights
		else if((Board[FRow][FColumn].equals("WH"))||(Board[FRow][FColumn].equals("BH")))
		{
			String color=Board[FRow][FColumn].substring(0,1);
			//top 2 spaces
			if(FRow+2<8)
			{
				if(FColumn-1>-1)
				{
					if(!(Board[FRow+2][FColumn-1].substring(0, 1).equals(color)))
					{
						rows.add(FRow+2);
						columns.add(FColumn-1);
					}
				}
				if(FColumn+1<8)
				{
					if(!(Board[FRow+2][FColumn+1].substring(0, 1).equals(color)))
					{
						rows.add(FRow+2);
						columns.add(FColumn+1);
					}
				}
			}
			//top of right and top of left
			if(FRow+1<8)
			{
				if(FColumn+2<8)
				{
					if(!(Board[FRow+1][FColumn+2].substring(0, 1).equals(color)))
					{
						rows.add(FRow+1);
						columns.add(FColumn+2);
					}
				}
				if(FColumn-2>-1)
				{
					if(!(Board[FRow+1][FColumn-2].substring(0, 1).equals(color)))
					{
						rows.add(FRow+1);
						columns.add(FColumn-2);
					}
				}
			}
			//bottom of left and right
			if(FRow-1>-1)
			{
				if(FColumn-2>-1)
				{
					if(!(Board[FRow-1][FColumn-2].substring(0, 1).equals(color)))
					{
						rows.add(FRow-1);
						columns.add(FColumn-2);
					}
				}
				if(FColumn+2<8)
				{
					if(!(Board[FRow-1][FColumn+2].substring(0, 1).equals(color)))
					{
						rows.add(FRow-1);
						columns.add(FColumn+2);
					}
				}
			}
			//bottom 2
			if(FRow-2>-1)
			{
				if(FColumn+1<8)
				{
					if(!(Board[FRow-2][FColumn+1].substring(0, 1).equals(color)))
					{
						rows.add(FRow-2);
						columns.add(FColumn+1);
					}
				}
				if(FColumn-1>-1)
				{
					if(!(Board[FRow-2][FColumn-1].substring(0, 1).equals(color)))
					{
						rows.add(FRow-2);
						columns.add(FColumn-1);
					}
				}
			}
		}
		// ROOK MOVEMENT. Slides in each of the 4 straight directions until it hits the edge of the
		// board, one of its own pieces, in which case it stops before that square, or an enemy
		// piece, in which case it includes that square since it can capture it, then stops. Each
		// start and end to portion comment below adds a marker pair so later code can tell where
		// one direction's squares end and the next direction begins.
		//first check stop
		//white and black rooks
		else if((Board[FRow][FColumn].equals("WR"))||(Board[FRow][FColumn].equals("BR")))
		{
			String color=Board[FRow][FColumn].substring(0,1);
			String enemy=(color.equals("W"))?"B":"W";

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//right
			if(FColumn+1<8)
			{
				for(int i=FColumn+1; i<8; i++)
				{
					if(!(Board[FRow][i].substring(0, 1).equals(color)))
					{
						rows.add(FRow);
						columns.add(i);
						if((Board[FRow][i].substring(0, 1).equals(enemy)))
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//left
			if(FColumn-1>-1)
			{
				for(int i=FColumn-1; i>-1; i--)
				{
					if(!(Board[FRow][i].substring(0, 1).equals(color)))
					{
						rows.add(FRow);
						columns.add(i);
						if((Board[FRow][i].substring(0, 1).equals(enemy)))
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//down
			if(FRow-1>-1)
			{
				for(int i=FRow-1; i>-1; i--)
				{
					if(!(Board[i][FColumn].substring(0, 1).equals(color)))
					{
						rows.add(i);
						columns.add(FColumn);
						if((Board[i][FColumn].substring(0, 1).equals(enemy)))
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//up
			if(FRow+1<8)
			{
				for(int i=FRow+1; i<8; i++)
				{
					if(!(Board[i][FColumn].substring(0, 1).equals(color)))
					{
						rows.add(i);
						columns.add(FColumn);
						if((Board[i][FColumn].substring(0, 1).equals(enemy)))
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);
		}
		// BISHOP MOVEMENT. Same sliding idea as the rook, just along the 4 diagonals instead of the
		// 4 straight directions.
		//white and black bishop
		else if((Board[FRow][FColumn].equals("WB"))||(Board[FRow][FColumn].equals("BB")))
		{
			String color=Board[FRow][FColumn].substring(0,1);
			String enemy=(color.equals("W"))?"B":"W";

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//up right
			int j=1;
			while((FRow+j<8)&&(FColumn+j<8))
			{
				if(!(Board[FRow+j][FColumn+j].substring(0, 1).equals(color)))
				{
					rows.add(FRow+j);
					columns.add(FColumn+j);
					if((Board[FRow+j][FColumn+j].substring(0, 1).equals(enemy)))
					{
						break;
					}
				}
				else
				{
					break;
				}
				j++;
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//down right
			j=1;
			while((FRow-j>-1)&&(FColumn+j<8))
			{
				if(!(Board[FRow-j][FColumn+j].substring(0, 1).equals(color)))
				{
					rows.add(FRow-j);
					columns.add(FColumn+j);
					if((Board[FRow-j][FColumn+j].substring(0, 1).equals(enemy)))
					{
						break;
					}
				}
				else
				{
					break;
				}
				j++;
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//up left
			j=1;
			while((FRow+j<8)&&(FColumn-j>-1))
			{
				if(!(Board[FRow+j][FColumn-j].substring(0, 1).equals(color)))
				{
					rows.add(FRow+j);
					columns.add(FColumn-j);
					if((Board[FRow+j][FColumn-j].substring(0, 1).equals(enemy)))
					{
						break;
					}
				}
				else
				{
					break;
				}
				j++;
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//down left
			j=1;
			while((FRow-j>-1)&&(FColumn-j>-1))
			{
				if(!(Board[FRow-j][FColumn-j].substring(0, 1).equals(color)))
				{
					rows.add(FRow-j);
					columns.add(FColumn-j);
					if((Board[FRow-j][FColumn-j].substring(0, 1).equals(enemy)))
					{
						break;
					}
				}
				else
				{
					break;
				}
				j++;
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);
		}
		// QUEEN MOVEMENT. Just a bishop's 4 diagonals plus a rook's 4 straight directions, back to
		// back. The queen doesn't get its own movement rules, it's literally both pieces combined.
		//black and white queen
		else if((Board[FRow][FColumn].equals("WQ"))||(Board[FRow][FColumn].equals("BQ")))
		{
			String color=Board[FRow][FColumn].substring(0,1);
			String enemy=(color.equals("W"))?"B":"W";
			//bishop movement

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//up right
			int j=1;
			while((FRow+j<8)&&(FColumn+j<8))
			{
				if(!(Board[FRow+j][FColumn+j].substring(0, 1).equals(color)))
				{
					rows.add(FRow+j);
					columns.add(FColumn+j);
					if((Board[FRow+j][FColumn+j].substring(0, 1).equals(enemy)))
					{
						break;
					}
				}
				else
				{
					break;
				}
				j++;
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//down right
			j=1;
			while((FRow-j>-1)&&(FColumn+j<8))
			{
				if(!(Board[FRow-j][FColumn+j].substring(0, 1).equals(color)))
				{
					rows.add(FRow-j);
					columns.add(FColumn+j);
					if((Board[FRow-j][FColumn+j].substring(0, 1).equals(enemy)))
					{
						break;
					}
				}
				else
				{
					break;
				}
				j++;
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//up left
			j=1;
			while((FRow+j<8)&&(FColumn-j>-1))
			{
				if(!(Board[FRow+j][FColumn-j].substring(0, 1).equals(color)))
				{
					rows.add(FRow+j);
					columns.add(FColumn-j);
					if((Board[FRow+j][FColumn-j].substring(0, 1).equals(enemy)))
					{
						break;
					}
				}
				else
				{
					break;
				}
				j++;
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//down left
			j=1;
			while((FRow-j>-1)&&(FColumn-j>-1))
			{
				if(!(Board[FRow-j][FColumn-j].substring(0, 1).equals(color)))
				{
					rows.add(FRow-j);
					columns.add(FColumn-j);
					if((Board[FRow-j][FColumn-j].substring(0, 1).equals(enemy)))
					{
						break;
					}
				}
				else
				{
					break;
				}
				j++;
			}
			//rook movement

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//right
			if(FColumn+1<8)
			{
				for(int i=FColumn+1; i<8; i++)
				{
					if(!(Board[FRow][i].substring(0, 1).equals(color)))
					{
						rows.add(FRow);
						columns.add(i);
						if((Board[FRow][i].substring(0, 1).equals(enemy)))
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//left
			if(FColumn-1>-1)
			{
				for(int i=FColumn-1; i>-1; i--)
				{
					if(!(Board[FRow][i].substring(0, 1).equals(color)))
					{
						rows.add(FRow);
						columns.add(i);
						if((Board[FRow][i].substring(0, 1).equals(enemy)))
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//down
			if(FRow-1>-1)
			{
				for(int i=FRow-1; i>-1; i--)
				{
					if(!(Board[i][FColumn].substring(0, 1).equals(color)))
					{
						rows.add(i);
						columns.add(FColumn);
						if((Board[i][FColumn].substring(0, 1).equals(enemy)))
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);

			//up
			if(FRow+1<8)
			{
				for(int i=FRow+1; i<8; i++)
				{
					if(!(Board[i][FColumn].substring(0, 1).equals(color)))
					{
						rows.add(i);
						columns.add(FColumn);
						if((Board[i][FColumn].substring(0, 1).equals(enemy)))
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
			}

			//start/end to portion
			rows.add(88);
			columns.add(88);
		}

		// KING MOVEMENT. The king is the one piece whose legal moves depend on more than just
		// whether the square is on the board and not occupied by its own color piece. Moving into
		// a square the enemy attacks isn't allowed either, so this hands off to
		// KingLegalMovesThretened() below, which does that extra safety filtering.
		// black king and white king
		if((Board[FRow][FColumn].equals("BK"))||(Board[FRow][FColumn].equals("WK")))
		{
			columnsAndRows.clear();
			rows.clear();
			columns.clear();
			String color=Board[FRow][FColumn].substring(0,1);
			//King legal moves, returns list of legal moves
			columnsAndRows=KingLegalMovesThretened(FRow, FColumn,Board,color);

			for(int i=0; i<columnsAndRows.size(); i++)
			{
				if(i%2==0)
				{
					rows.add(columnsAndRows.get(i));
				}
				else
				{
					columns.add(columnsAndRows.get(i));
				}
			}
		}
		//return rows and columns
		for(int i=0; i<rows.size(); i++)
		{
			columnsAndRows.add(rows.get(i));
			columnsAndRows.add(columns.get(i));
		}
		return columnsAndRows;
	}

	// STALEMATE CHECK. Collects every legal move for every piece of the given color and reports
	// true if that side has literally zero legal moves anywhere on the board, which, if that side
	// isn't in check, means the game is a stalemate and a draw. Game.java is what actually decides
	// whether it's a stalemate versus a checkmate, this just answers whether this color has any
	// move at all.
	public boolean stalemate(String[][]Board, String color)
	{
		ArrayList<Integer> unbox1 = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();

		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if(Board[r][c].substring(0, 1).equals(color))
				{
					//get legal moves
					unbox1=LegalMoves(r,c,Board, false);
					//unbox columnsAndRows
					for(int i=0; i<unbox1.size(); i++)
					{
						if(i%2==0)
						{
							rows.add(unbox1.get(i));
						}
						else
						{
							columns.add(unbox1.get(i));
						}
					}
				}
			}
		}
		for(int i=0; i<rows.size(); i++)
		{
			if((rows.get(i)!=88)&&(Board[rows.get(i)][columns.get(i)].equals(color+"K")))
			{
				rows.remove(i);
				columns.remove(i);
				break;
			}
		}
		// TODO(bugfix pass): this loop's condition is never true when rows.size()>0, so it never
		// actually strips the 88 separators, which means stalemate is almost never detected while a
		// rook/bishop/queen remains on the board (AUDIT.md #5)
		for(int i=rows.size(); i<=0; i++)
		{
			if(rows.size()==0)
			{
				break;
			}
			if(rows.get(i)==88)
			{
				rows.remove(i);
				columns.remove(i);
				if(rows.size()==0)
				{
					break;
				}
			}
		}
		if(rows.size()==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// Powers the enter 88 to see possible moves console feature. Prints a copy of the board with
	// every square the selected piece could move to marked with an XX.
	public void ShowLegalMoves(int FRow, int FColumn, String[][]Board)
	{
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		columnsAndRows=LegalMoves(FRow, FColumn, Board, false);
		String Boardmoves[][] = new String [8][8];
		board.equalBoard(Board, Boardmoves);
		//get rows and columns from the columnsAndRows array list
		for(int i=0; i<columnsAndRows.size(); i++)
		{
			if(i%2==0)
			{
				rows.add(columnsAndRows.get(i));
			}
			else
			{
				columns.add(columnsAndRows.get(i));
			}
		}
		for(int i=0; i<rows.size(); i++)
		{
			if(!(rows.get(i)==88))
			{
				Boardmoves[rows.get(i)][columns.get(i)]="XX";
			}
		}
		System.out.println("");
		board.easyprint(Boardmoves);
	}

	// Works out which of the king's 8 surrounding squares are actually safe to move to, as opposed
	// to just empty or holding an enemy piece, which is all LegalMoves() checks for every other
	// piece. Three passes:
	//  1) Mark every square the enemy attacks, with the king temporarily removed from the board
	//     so a king can't hide behind itself against a sliding piece.
	//  2) Build the list of the king's normal 8 adjacent squares, skipping any that are marked
	//     attacked or have one of the king's own pieces on them.
	//  3) Recheck each remaining candidate by actually placing the king there and scanning
	//     every enemy piece's legal moves again to confirm none of them could then capture it.
	//King legal moves, returns list of legal moves
	public ArrayList<Integer> KingLegalMovesThretened(int FRow, int FColumn, String[][]Board, String color)
	{
		String enemy=(color.equals("W"))?"B":"W";
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> unbox1 = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		ArrayList<Integer> rows2 = new ArrayList<Integer>();
		ArrayList<Integer> columns2 = new ArrayList<Integer>();
		unbox1.clear();
		String KMoves[][] = new String [8][8];
		String Protect1[][] = new String [8][8];
		board.equalBoard(Board, KMoves);
		board.equalBoard(Board, Protect1);
		Protect1[FRow][FColumn]="  ";
		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if((Protect1[r][c].substring(0, 1).equals(enemy))&&(!(Protect1[r][c].equals(enemy+"K"))))
				{
					//get legal moves
					unbox1=LegalMoves(r,c,Protect1, true);
					//unbox columnsAndRows
					for(int i=0; i<unbox1.size(); i++)
					{
						if(i%2==0)
						{
							rows.add(unbox1.get(i));
						}
						else
						{
							columns.add(unbox1.get(i));
						}
					}
					//fill moves w possible moves and the type of piece
					for(int i=0; i<rows.size(); i++)
					{
						if(!(rows.get(i)==88))
						{
							KMoves[rows.get(i)][columns.get(i)]="XX";
						}
					}

				}
			}
		}
		Protect1[FRow][FColumn]=color+"K";
		rows.clear();
		columns.clear();
		columnsAndRows.clear();
		//moves
		//up
		if(FRow+1<8)
		{
			if(!(KMoves[FRow+1][FColumn].equals("XX")))
			{
				if(!(Board[FRow+1][FColumn].substring(0, 1).equals(color)))
				{
					rows.add(FRow+1);
					columns.add(FColumn);
				}

			}
			//up right
			if(FColumn+1<8)
			{
				if(!(KMoves[FRow+1][FColumn+1].equals("XX")))
				{
					if(!(Board[FRow+1][FColumn+1].substring(0, 1).equals(color)))
					{
						rows.add(FRow+1);
						columns.add(FColumn+1);
					}
				}
			}
		}
		//right
		if(FColumn+1<8)
		{
			if(!(KMoves[FRow][FColumn+1].equals("XX")))
			{
				if(!(Board[FRow][FColumn+1].substring(0, 1).equals(color)))
				{
					rows.add(FRow);
					columns.add(FColumn+1);
				}
			}
			//down right
			if(FRow-1>-1)
			{
				if(!(KMoves[FRow-1][FColumn+1].equals("XX")))
				{
					if(!(Board[FRow-1][FColumn+1].substring(0, 1).equals(color)))
					{
						rows.add(FRow-1);
						columns.add(FColumn+1);
					}
				}
			}
		}
		//down
		if(FRow-1>-1)
		{
			if(!(KMoves[FRow-1][FColumn].equals("XX")))
			{
				if(!(Board[FRow-1][FColumn].substring(0, 1).equals(color)))
				{
					rows.add(FRow-1);
					columns.add(FColumn);
				}
			}
			//down left
			if(FColumn-1>-1)
			{
				if(!(KMoves[FRow-1][FColumn-1].equals("XX")))
				{
					if(!(Board[FRow-1][FColumn-1].substring(0, 1).equals(color)))
					{
						rows.add(FRow-1);
						columns.add(FColumn-1);
					}
				}
			}
		}
		//left
		if(FColumn-1>-1)
		{
			if(!(KMoves[FRow][FColumn-1].equals("XX")))
			{
				if(!(Board[FRow][FColumn-1].substring(0, 1).equals(color)))
				{
					rows.add(FRow);
					columns.add(FColumn-1);
				}
			}
			//up left
			if(FRow+1<8)
			{
				if(!(KMoves[FRow+1][FColumn-1].equals("XX")))
				{
					if(!(Board[FRow+1][FColumn-1].substring(0, 1).equals(color)))
					{
						rows.add(FRow+1);
						columns.add(FColumn-1);
					}
				}
			}
		}
		columnsAndRows.clear();
		//switch king and check if he would be checked
		String Protect[][] = new String [8][8];
		for(int i=0; i<rows.size(); i++)
		{
			if(rows.get(i)!=88)
			{
				board.equalBoard(Board, Protect);
				Protect[rows.get(i)][columns.get(i)]=color+"K";
				Protect[FRow][FColumn]="  ";
				for(int r=0; r<Board.length; r++)
				{
					for(int c=0; c<Board[0].length; c++)
					{
						if((Board[r][c].substring(0, 1).equals(enemy))&&(!(Board[r][c].equals(enemy+"K"))))
						{
							//get legal moves
							columnsAndRows=LegalMoves(r,c,Protect, true);
							//unbox columnsAndRows
							rows2.clear();
							columns2.clear();
							for(int w=0; w<columnsAndRows.size(); w++)
							{
								if(w%2==0)
								{
									rows2.add(columnsAndRows.get(w));
								}
								else
								{
									columns2.add(columnsAndRows.get(w));
								}
							}
							//fill moves w possible moves and the type of piece
							for(int j=0; j<rows2.size(); j++)
							{

								if((rows2.get(j)!=88)&&(Protect[rows2.get(j)][columns2.get(j)].equals(color+"K")))
								{
									// TODO(bugfix pass): White's original version was missing the size check
									// guard that Black's version had here, which can let a second unsafe king
									// square slip through unfiltered (AUDIT.md #7). Preserved as a color branch
									// for now.
									if(color.equals("B"))
									{
										if(!(i>=rows.size()))
										{
											rows.remove(i);
											columns.remove(i);
											break;
										}
									}
									else
									{
										rows.remove(i);
										columns.remove(i);
										break;
									}
								}
							}
							if(rows.size()==0)
							{
								break;
							}
						}
					}
					if(rows.size()==0)
					{
						break;
					}
				}
			}
		}
		columnsAndRows.clear();
		//return rows and columns
		for(int i=0; i<rows.size(); i++)
		{
			columnsAndRows.add(rows.get(i));
			columnsAndRows.add(columns.get(i));
		}
		return columnsAndRows;
	}

	// Answers whether any of the player's own pieces can reach one of a given set of squares.
	// columnsAndRows2 is the list of squares that would either capture the checking piece or
	// block the check, built by KingInCheck() below. Despite the name, this alone does not decide
	// whether it is checkmate. KingInCheck() combines this with whether the king has any safe
	// square to step to in order to work that out.
	//declare arrylist check that the king's pieces are able to block
	public boolean KingInCheckMate(String[][]Board, ArrayList<Integer> columnsAndRows2, String color)
	{
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		ArrayList<Integer> rows2 = new ArrayList<Integer>();
		ArrayList<Integer> columns2 = new ArrayList<Integer>();

		for(int i=0; i<columnsAndRows2.size(); i++)
		{
			if(i%2==0)
			{
				rows2.add(columnsAndRows2.get(i));
			}
			else
			{
				columns2.add(columnsAndRows2.get(i));
			}
		}
		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if((Board[r][c].substring(0, 1).equals(color))&&(!(Board[r][c].equals(color+"K"))))
				{
					//get legal moves
					columnsAndRows=LegalMoves(r,c,Board, false);
					//unbox columnsAndRows
					for(int i=0; i<columnsAndRows.size(); i++)
					{
						if(i%2==0)
						{
							rows.add(columnsAndRows.get(i));
						}
						else
						{
							columns.add(columnsAndRows.get(i));
						}
					}
					//fill moves w possible moves and the type of piece
					for(int i=0; i<rows.size(); i++)
					{
						if(!(rows.get(i)==88))
						{
							for(int p=0; p<rows2.size(); p++)
							{
								if(rows.get(i)==rows2.get(p))
								{
									if(columns.get(i)==columns2.get(p))
									{
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	// The main function for working out whether this king is in check and what can be done about
	// it. This is what Game.move() calls at the start of every turn. It does four things and packs
	// the results into one returned list:
	//  1) Scans every enemy piece to see if any of them currently attacks this color's king.
	//  2) If the attacker is a rook/bishop/queen, works out every square between the attacker and
	//     the king (the squares a piece could move to in order to block the check). A knight or
	//     pawn check can't be blocked, so that step is skipped for those.
	//  3) Asks KingLegalMovesThretened() whether the king has any safe square to step to, and
	//     KingInCheckMate() whether any own piece can capture the attacker or block it.
	//  4) Combines those two answers into one status code appended to the end of the returned list:
	//       11 = king has no safe move, must block the check or capture the attacker
	//       22 = king could move OR the check could be blocked/captured
	//       33 = king must move (blocking/capturing isn't an option)
	//       44 = checkmate, no safe king move and nothing can block/capture
	//     Game.java reads this code to decide what message to print and which moves to allow.
	public ArrayList<Integer> KingInCheck(String[][]Board, String color)
	{
		String enemy=(color.equals("W"))?"B":"W";
		int BkTRow=0, BkTColumn=0;
		int CheckerRow=0, CheckerColumn=0;
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();

		ArrayList<Integer> CheckcolumnsAndRows = new ArrayList<Integer>();

		ArrayList<Integer> locate88 = new ArrayList<Integer>();

		ArrayList<Integer> columnsAndRowsneeded = new ArrayList<Integer>();
		ArrayList<Integer> rowsneeded = new ArrayList<Integer>();
		ArrayList<Integer> columnsneeded = new ArrayList<Integer>();
		columnsAndRows.clear();
		rows.clear();
		columns.clear();
		CheckcolumnsAndRows.clear();
		rowsneeded.clear();
		columnsneeded.clear();
		String KMoves[][] = new String [8][8];
		board.equalBoard(Board, KMoves);
		boolean check=false;
		int doublecheck=0;
		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if((Board[r][c].substring(0, 1).equals(enemy))&&(!(Board[r][c].substring(1).equals("K"))))
				{
					//get legal moves
					columnsAndRows=LegalMoves(r,c,Board,true);
					rows.clear();
					columns.clear();
					//unbox columnsAndRows
					for(int i=0; i<columnsAndRows.size(); i++)
					{
						if(i%2==0)
						{
							rows.add(columnsAndRows.get(i));
						}
						else
						{
							columns.add(columnsAndRows.get(i));
						}
					}
					//piece trying to check king
					for(int i=0; i<rows.size(); i++)
					{
						//if one of the possible moves is the king
						if((rows.get(i)!=88)&&(Board[rows.get(i)][columns.get(i)].equals(color+"K")))
						{
							if(doublecheck==0)
							{
								check=true;
								//normal
								// TODO(bugfix pass): White's original version never set BkTRow here (only
								// BkTColumn), which corrupts White's king escape square lookup once the White
								// king has moved off row 0 (AUDIT.md #4). Preserved as a color branch for now.
								if(color.equals("B"))
								{
									BkTRow=rows.get(i);
								}
								BkTColumn=columns.get(i);
								CheckerRow=r;
								CheckerColumn=c;
								doublecheck++;
							}
							else
							{
								doublecheck++;
							}
						}
					}
					boolean found=false;
					if(check)
					{
						if(Board[CheckerRow][CheckerColumn].substring(1).equals("P")||Board[CheckerRow][CheckerColumn].substring(1).equals("H"))
						{
						}
						else
						{

							for(int w=0; w<rows.size(); w++)
							{
								if(rows.get(w)==88)
								{
									locate88.add(w);

								}
							}
							int locate=0;
							for(int d=0; d<rows.size(); d++)
							{
								if((rows.get(d)!=88)&&(Board[rows.get(d)][columns.get(d)].equals(color+"K")))
								{
									found=true;
									locate=d;
									break;
								}

							}
							if(found)
							{
								int start=0, end=0;
								for(int d=0; d<locate88.size(); d++)
								{
									if((locate88.get(d)<locate)&&(locate88.get(d+1)>locate))
									{
										start=locate88.get(d);
										end=locate88.get(d+1);
									}
								}
								for(int d=start+1; d<end; d++)
								{
									if(!(Board[rows.get(d)][columns.get(d)].equals(color+"K")))
									{
										rowsneeded.add(rows.get(d));
										columnsneeded.add(columns.get(d));
									}
								}
								if(found)
									break;
								else
								{
									rowsneeded.clear();
									columnsneeded.clear();
								}
							}
						}
					}
				}
			}
		}

		rowsneeded.add(CheckerRow);
		columnsneeded.add(CheckerColumn);

		columnsAndRowsneeded.clear();
		for(int i=0; i<rowsneeded.size(); i++)
		{
			columnsAndRowsneeded.add(rowsneeded.get(i));
			columnsAndRowsneeded.add(columnsneeded.get(i));
		}
		boolean checkmate=false;
		CheckcolumnsAndRows=KingLegalMovesThretened(BkTRow,BkTColumn, Board, color);
		checkmate=KingInCheckMate(Board, columnsAndRowsneeded, color);
		if(check)
		{
			int amount=0;
			// TODO(bugfix pass): the numbered println calls below only ever existed in White's original
			// code path (WKingInCheck). Black's had none. Preserved as a color branch so turn output
			// doesn't silently change; these look like leftover debug prints worth removing later.
			if((CheckcolumnsAndRows.size()==0)&&(checkmate))
			{
				//no king move so block or take by piece
				amount=11;
				if(color.equals("W")) System.out.println("1st");
			}
			if((CheckcolumnsAndRows.size()>0)&&(!checkmate))
			{
				//king move
				amount=33;
				if(color.equals("W")) System.out.println("2nd");
			}
			if((CheckcolumnsAndRows.size()>0)&&(checkmate))
			{
				//king move or block or take by piece
				amount=22;
				if(color.equals("W")) System.out.println("3rd");
			}
			if(doublecheck>1)
			{
				//king must move
				amount=33;
				if(color.equals("W")) System.out.println("4th");
			}
			if(color.equals("W")) System.out.println(CheckcolumnsAndRows.size());
			if((CheckcolumnsAndRows.size()==0)&&(!checkmate))
			{
				//check mate
				amount=44;
				if(color.equals("W")) System.out.println("5th");
			}
			columnsAndRowsneeded.add(amount);
		}
		return columnsAndRowsneeded;
	}
}
