import java.util.ArrayList;
import java.util.Scanner;

public class Game
{
	private Board boardUtil;
	private MoveGenerator moveGen;

	public Game(Board boardUtil, MoveGenerator moveGen)
	{
		this.boardUtil = boardUtil;
		this.moveGen = moveGen;
	}

	//move (was Wmove/Bmove, merged by color)
	public void move(String[][] board, ArrayList<String> wLost, ArrayList<String> bLost, String color)
	{
		String enemy=(color.equals("W"))?"B":"W";
		ArrayList<Integer> check = new ArrayList<Integer>();
		ArrayList<Integer> columnsAndRows1 = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		rows.clear();
		columns.clear();
		columnsAndRows1.clear();
		boolean noKingMove=false, kingMustMove=false, blockOrKingMove=false, end=false;
		// Wmove and Bmove originally ran this preamble (turn announcement, stalemate checks,
		// check-status message) in a different order per color. Preserved exactly per color here
		// rather than picking one order, since that would be a behavior change, not just a dedup.
		if(color.equals("W"))
		{
			System.out.println("White's Turn");
			//king problem
			end=moveGen.stalemate(board, "W");
			if(end)
			{
				System.out.println("Stalemate! No moves for white!");
				System.exit(0);
			}
			end=moveGen.stalemate(board, "B");
			if(end)
			{
				System.out.println("Stalemate! No moves for Black!");
				System.exit(0);
			}
			//rook problem
			columnsAndRows1=moveGen.KingInCheck(board, color);
		}
		else
		{
			columnsAndRows1=moveGen.KingInCheck(board, color);
			System.out.println("Black's Turn");
		}
		if(columnsAndRows1.size()>0)
		{
			int code=columnsAndRows1.get(columnsAndRows1.size()-1);
			String colorName=color.equals("W")?"White":"Black";
			if(code==11)
			{
				//no king move so block or take
				System.out.println("1. "+colorName+" King In CHECK!\nMust Block the Check, No Legal Moves for King");
				noKingMove=true;
			}
			else if(code==22)
			{
				//king move or block or take
				System.out.println("2. "+colorName+" King In CHECK!\nMust Block the Check or use Legal Moves for King");
				blockOrKingMove=true;
			}
			else if(code==33)
			{
				//king move
				System.out.println("3. "+colorName+" King In CHECK!\nMust Move King");
				kingMustMove=true;
			}
			else if(code==44)
			{
				//checkmate
				System.out.println("Checkmate");
				System.exit(0);
			}
			columnsAndRows1.remove(columnsAndRows1.size()-1);
		}
		if(color.equals("B"))
		{
			end=moveGen.stalemate(board, "W");
			if(end)
			{
				System.out.println("Stalemate! No moves for white!");
				System.exit(0);
			}
			end=moveGen.stalemate(board, "B");
			if(end)
			{
				System.out.println("Stalemate! No moves for Black!");
				System.exit(0);
			}
		}
		Scanner reader = new Scanner(System.in);
		char column=32;
		int row=0;
		//find piece you want to move
		while((row<1)||(row>8))
		{
			System.out.println("Enter the row of the piece you wish to move (Number 1-8) (0 - Resign) (-1 - Propose draw)");
			row=reader.nextInt();
			if(row==0)
			{
				System.out.println((color.equals("W")?"White":"Black")+" resigns!");
				System.exit(0);
				row=-23;
			}
			if(row==-1)
			{
				System.out.println((color.equals("W")?"White":"Black")+" Proposes Draw!");
				char yn='i';
				while((yn!='y')&&(yn!='n'))
				{
					System.out.println("Does "+(color.equals("W")?"Black":"White")+" accept? (y/n)");
					yn=reader.next().charAt(0);
				}
				if(yn=='y')
				{
					System.out.println("Its a draw!");
					System.exit(0);
				}
				else
				{
					System.out.println((color.equals("W")?"Black":"White")+" declines");
				}
				row=-23;
			}
		}
		//must go down one 0-7
		row--;
		while((column<97)||(column>104))
		{
			System.out.println("Enter the column of the piece you wish to move (character a-h)");
			column=reader.next().charAt(0);
		}
		//check if own color
		if((board[row][column-97].substring(0,1).equals(enemy)))
		{
			System.out.println("Enter the location of a "+(color.equals("W")?"white":"black")+" piece please");
			move(board, wLost, bLost, color);
			return;
		}
		//if the space you chose is empty choose again
		if(((board[row][column-97].equals("  ")))||((board[row][column-97].equals("##"))))
		{
			System.out.println("Empty Spot: Enter the location of a piece please");
			move(board, wLost, bLost, color);
			return;
		}
		if((board[row][column-97].equals(color+"K"))&&(noKingMove))
		{
			System.out.println("No legal moves for "+(color.equals("W")?"White":"Black")+" king: Enter the location of a new piece please");
			move(board, wLost, bLost, color);
			return;
		}
		else if(!(board[row][column-97].equals(color+"K"))&&(kingMustMove))
		{
			System.out.println("No other legal moves except for "+(color.equals("W")?"White":"Black")+" king: Enter the location of the "+(color.equals("W")?"white":"black")+" king please ");
			move(board, wLost, bLost, color);
			return;
		}
		int FRow=row, FColumn=(int)(column-97);
		column=32;
		row=0;
		//ask for space to wish to move to
		//row
		while((row<1)||(row>8))
		{
			System.out.println("Enter the row of where you want to move your piece (Number 1-8) (Enter 88 for posible moves)");
			row=reader.nextInt();
			if(row==88)
			{
				moveGen.ShowLegalMoves(FRow, FColumn, board);
			}
		}
		//column
		while((column<97)||(column>104))
		{
			System.out.println("Enter the column of where you want to move your piece (character a-h)");
			column=reader.next().charAt(0);
		}

		//2nd move
		row--;
		int SRow=row, SColumn=(int)(column-97);
		boolean blockOrTake=false;

		if((blockOrKingMove)&&(!(board[FRow][FColumn].equals(color+"K"))))
		{
			for(int i=0; i<columnsAndRows1.size(); i++)
			{
				if(i%2==0)
				{
					rows.add(columnsAndRows1.get(i));
				}
				else
				{
					columns.add(columnsAndRows1.get(i));
				}
			}
			for(int i=0; i<rows.size(); i++)
			{
				if((rows.get(i).equals(SRow))&&(columns.get(i).equals(SColumn)))
				{
					blockOrTake=true;
				}
			}
			if(!blockOrTake)
			{
				System.out.println("Must block the check!");
				move(board, wLost, bLost, color);
				return;
			}
		}
		if(noKingMove)
		{
			for(int i=0; i<columnsAndRows1.size(); i++)
			{
				if(i%2==0)
				{
					rows.add(columnsAndRows1.get(i));
				}
				else
				{
					columns.add(columnsAndRows1.get(i));
				}
			}
			for(int i=0; i<rows.size(); i++)
			{
				if((rows.get(i).equals(SRow))&&(columns.get(i).equals(SColumn)))
				{
					blockOrTake=true;
				}
			}
			if(!blockOrTake)
			{
				System.out.println("Must block the check!");
				move(board, wLost, bLost, color);
				return;
			}
		}
		boolean isCorrect=false;
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> checkinCheck = new ArrayList<Integer>();
		//return possible moves
		columnsAndRows=moveGen.LegalMoves(FRow, FColumn, board, false);
		//check if given is legal
		for(int i=0; i<columnsAndRows.size(); i+=2)
		{
			if((SRow==columnsAndRows.get(i))&&(SColumn==columnsAndRows.get(i+1)))
			{
				isCorrect=true;
			}
		}
		String save[][] = new String [8][8];
		boardUtil.equalBoard(board, save);
		save[SRow][SColumn]=save[FRow][FColumn];
		save[FRow][FColumn]="  ";
		checkinCheck=moveGen.KingInCheck(save, color);
		if(checkinCheck.size()>0)
		{
			int code2=checkinCheck.get(checkinCheck.size()-1);
			if(code2>10)
			{
				System.out.println("Error: This Move caused your king to be checked pick a new spot to move");
				move(board, wLost, bLost, color);
				return;
			}
		}
		if(isCorrect)
		{
			System.out.println("This move is legal!\n");
			///switch

			//move dead pieces
			//black
			if((board[FRow][FColumn].substring(0, 1).equals("W"))&&(board[SRow][SColumn].substring(0, 1).equals("B")))
			{
				wLost.add(board[SRow][SColumn]);
			}
			//white
			else if((board[FRow][FColumn].substring(0, 1).equals("B"))&&(board[SRow][SColumn].substring(0, 1).equals("W")))
			{
				bLost.add(board[SRow][SColumn]);
			}
			board[SRow][SColumn]=board[FRow][FColumn];
			//put behind black background
			if(FRow%2!=0)
			{
				if(FColumn%2!=0)
				{
					board[FRow][FColumn]="##";
				}
				else if(FColumn%2==0)
				{
					board[FRow][FColumn]="  ";
				}
			}
			else if(FRow%2==0)
			{
				if(FColumn%2==0)
				{
					board[FRow][FColumn]="##";
				}
				else if(FColumn%2!=0)
				{
					board[FRow][FColumn]="  ";
				}
			}
			//print board after finding legal move
			boardUtil.print(board, wLost, bLost);
			// A successful White move chains straight into Black's turn here; ChessMain's outer loop
			// is what calls White's turn again after Black finishes, so Black doesn't chain forward.
			if(color.equals("W"))
			{
				move(board, wLost, bLost, "B");
			}
			return;
		}
		else
		{
			//illegal move, send back to top of move
			System.out.println("Illegal Move: Enter a valid location to move your piece");
			move(board, wLost, bLost, color);
			return;
		}
	}
}
