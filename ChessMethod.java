import java.util.ArrayList;
import java.util.Scanner;

public class ChessMethod 
{
	public void BuildBoard(String board[][]) 
	{
		//fill board with white and black spaces
		for(int row=0; row<8; row++)
		{
			for(int column=0; column<8; column++)
			{
				if(row%2!=0)
				{
					if(column%2!=0)
					{
						board[row][column]="##";
					}
					else if(column%2==0)
					{
						board[row][column]="  ";
					}
				}
				else if(row%2==0)
				{
					if(column%2==0)
					{
						board[row][column]="##";
					}
					else if(column%2!=0)
					{
						board[row][column]="  ";
					}
				}

			}
		}
		//fill board with pieces
		for(int column=0; column<8; column++)
		{
			board[1][column]="WP";
			board[6][column]="BP";
		}
		/*if(column==3)
			{
				//to make check easier
			}
			else if(column==4)
			{
				//same thing
			}*/
		//what ive added

		//Prevent King from going where threatened (2440)
		//check King and force movement or blocking (2917) (3288) - check for if blocking is legal
		//prevent king from taking a piece thats protected (2598)
		//pice cannot move somware and cause its own king to be threatened

		//endings
		//stalemate
		//checkmate
		//resignation and agrreded upon draw
		board[0][0]="WR";
		board[0][7]="WR";
		board[0][1]="WH";
		board[0][6]="WH";
		board[0][2]="WB";
		board[0][5]="WB";
		board[0][4]="WK";
		board[0][3]="WQ";
		
		board[7][0]="BR";
		board[7][7]="BR";
		board[7][1]="BH";
		board[7][6]="BH";
		board[7][2]="BB";
		board[7][5]="BB";
		board[7][4]="BK";
		board[7][3]="BQ";
	}

	public void equalBoard(String board[][], String New[][]) 
	{
		for(int row=7; row>=0; row--)
		{
			for(int column=0; column<=7; column++)
			{
				String news=board[row][column];
				New[row][column]=news;
			}
		}	
	}

	public void easyprint(String board[][]) 
	{
		for(int row=7; row>=0; row--)
		{
			for(int column=0; column<=7; column++)
			{
				if(column==0)
				{
					System.out.print((row+1)+" ");
					//numbers at the side
				}
				//change color on white if possible
				if(board[row][column].substring(0,1).equals("W"))
				{
					System.out.print(board[row][column]+" ");
				}
				//change color on black if possible
				else
				{
					System.out.print(board[row][column]+" ");
				}
			}
			System.out.println("");

		}
		//bottom line
		System.out.println("  a  b  c  d  e  f  g  h");
		System.out.println("");
	}
	public void print(String board[][], ArrayList<String> wLost, ArrayList<String> bLost) 
	{
		//print board
		for(int row=7; row>=0; row--)
		{
			for(int column=0; column<=7; column++)
			{
				if(column==0)
				{
					System.out.print((row+1)+" ");
					//numbers at the side
				}
				//change color on white if possible
				if(board[row][column].substring(0,1).equals("W"))
				{
					System.out.print(board[row][column]+" ");
				}
				//change color on black if possible
				else
				{
					System.out.print(board[row][column]+" ");
				}
			}
			System.out.println("");

		}
		//bottom line
		System.out.println("  a  b  c  d  e  f  g  h");
		System.out.println("");

		int b=0;
		//lost black piece value
		for(int i=0; i<wLost.size(); i++)
		{
			if(wLost.get(i).substring(1).equals("P"))
			{
				b+=1;
			}
			else if(wLost.get(i).substring(1).equals("R"))
			{
				b+=5;
			}
			else if(wLost.get(i).substring(1).equals("H"))
			{
				b+=3;
			}
			else if(wLost.get(i).substring(1).equals("B"))
			{
				b+=3;
			}
			else if(wLost.get(i).substring(1).equals("Q"))
			{
				b+=9;
			}
		}
		int w=0;
		//find white points per piece 
		for(int i=0; i<bLost.size(); i++)
		{
			if(bLost.get(i).substring(1).equals("P"))
			{
				w+=1;
			}
			else if(bLost.get(i).substring(1).equals("R"))
			{
				w+=5;
			}
			else if(bLost.get(i).substring(1).equals("H"))
			{
				w+=3;
			}
			else if(bLost.get(i).substring(1).equals("B"))
			{
				w+=3;
			}
			else if(bLost.get(i).substring(1).equals("Q"))
			{
				w+=9;
			}
		}
		//lost black pieces
		for(int i=0; i<wLost.size(); i++)
		{
			System.out.print(wLost.get(i)+", ");
		}
		//print black lost point value
		if(b-w>0)
		{
			System.out.println("+ "+(b-w));
		}
		if(wLost.size()>0)
		{
			System.out.println("");
		}
		//print lost white pieces
		for(int i=0; i<bLost.size(); i++)
		{
			System.out.print(bLost.get(i)+", ");
		}
		//print white lost points
		if(w-b>0)
		{
			System.out.println("+"+(w-b));
		}
		if(bLost.size()>0)
		{
			System.out.println("");
		}
	}
	//white move
	public void Wmove(String[][] board, ArrayList<String> wLost, ArrayList<String> bLost)
	{
		ArrayList<Integer> check = new ArrayList<Integer>();
		ArrayList<Integer> columnsAndRows1 = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		rows.clear();
		columns.clear();
		columnsAndRows1.clear();
		System.out.println("White's Turn");
		//System.out.println(columnsAndRows2.size()+" 2");
		boolean end=false;
		//king problem
		end=Wstalemate(board);
		if(end)
		{
			System.out.println("Stalemate! No moves for white!");
			System.exit(0);	
		}
		end=Bstalemate(board);
		if(end)
		{
			System.out.println("Stalemate! No moves for Black!");
			System.exit(0);	
		}
		//rook problem
		columnsAndRows1=WKingInCheck(board);
		//System.out.println(columnsAndRows3.size());
		boolean WnoKing=false, Wking=false, Wall=false;
		if(columnsAndRows1.size()>0)
		{
			int code=columnsAndRows1.get(columnsAndRows1.size()-1);
			//System.out.println(code);
			if(code==11)
			{
				//no king move so block or take
				System.out.println("1. White King In CHECK!\nMust Block the Check, No Legal Moves for King");
				WnoKing=true;
			}
			else if(code==22)
			{
				//king move or block or take	
				System.out.println("2. White King In CHECK!\nMust Block the Check or use Legal Moves for King");
				Wall=true;
			}
			else if(code==33)
			{
				//king move
				System.out.println("3. White King In CHECK!\nMust Move King");
				Wking=true;
			}
			else if(code==44)
			{
				//checkmate
				System.out.println("Checkmate");
				System.exit(0);
				//Bking=true;
			}
			columnsAndRows1.remove(columnsAndRows1.size()-1);
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
				System.out.println("White resigns!");
				System.exit(0);	
				row=-23;
			}
			if(row==-1)
			{
				System.out.println("White Proposes Draw!");
				char yn='i';
				while((yn!='y')&&(yn!='n'))
				{
					System.out.println("Does Black accept? (y/n)");
					yn=reader.next().charAt(0);
				}
				if(yn=='y')
				{
					System.out.println("Its a draw!");
					System.exit(0);	
				}
				else
				{
					System.out.println("Black declines");
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
		//check if white
		if((board[row][column-97].substring(0,1).equals("B")))
		{
			System.out.println("Enter the location of a white piece please");
			/*row=0;
			column=32;*/
			Wmove(board, wLost, bLost);
		}
		//if the space you chose is empty choose again
		if(((board[row][column-97].equals("  ")))||((board[row][column-97].equals("##"))))
		{
			System.out.println("Empty Spot: Enter the location of a piece please");
			/*row=0;
			column=32;*/
			Wmove(board, wLost, bLost);
		}
		if((board[row][column-97].equals("WK"))&&(WnoKing))
		{
			System.out.println("No legal moves for White king: Enter the location of a new piece please");
			/*row=0;
			column=32;*/
			Wmove(board, wLost, bLost);
		}
		else if(!(board[row][column-97].equals("WK"))&&(Wking))
		{
			System.out.println("No other legal moves except for White king: Enter the location of the white king please ");
			/*row=0;
			column=32;*/
			Wmove(board, wLost, bLost);
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
				ShowLegalMoves(FRow, FColumn, board);
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

		if((Wall)&&(!(board[FRow][FColumn].equals("WK"))))
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
				/*FRow=0;
				FColumn=0;*/
				Wmove(board, wLost, bLost);
			}
		}
		if(WnoKing)
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
				/*FRow=0;
				FColumn=0;*/
				Wmove(board, wLost, bLost);
			}		
		}
		boolean isCorrect=false;
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> checkinCheck = new ArrayList<Integer>();
		//return possible moves
		columnsAndRows=LegalMoves(FRow, FColumn, board, false);
		//check if given is legal
		for(int i=0; i<columnsAndRows.size(); i+=2)
		{
			if((SRow==columnsAndRows.get(i))&&(SColumn==columnsAndRows.get(i+1)))
			{
				isCorrect=true;
			}
		}
		String save[][] = new String [8][8];
		equalBoard(board, save);
		save[SRow][SColumn]=save[FRow][FColumn];
		save[FRow][FColumn]="  ";
		checkinCheck=WKingInCheck(save);
		if(checkinCheck.size()>0)
		{
			int code2=checkinCheck.get(checkinCheck.size()-1);
			if(code2>10)
			{
				System.out.println("Error: This Move caused your king to be checked pick a new spot to move");
				/*FRow=0;
				FColumn=0;*/
				Wmove(board, wLost, bLost);
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
			/*FRow=0;
			FColumn=0;*/
			//print board after finding legal move
			print(board, wLost, bLost);
			Bmove(board, wLost, bLost);
		}
		else
		{
			//illegal move, send back to top of move
			System.out.println("Illegal Move: Enter a valid location to move your piece");
			/*FRow=0;
			FColumn=0;*/
			Wmove(board, wLost, bLost);
		}
	}
	//black
	public void Bmove(String[][] board, ArrayList<String> wLost, ArrayList<String> bLost)
	{
		ArrayList<Integer> check = new ArrayList<Integer>();
		ArrayList<Integer> columnsAndRows2 = new ArrayList<Integer>();
		ArrayList<Integer> rows2 = new ArrayList<Integer>();
		ArrayList<Integer> columns2 = new ArrayList<Integer>();
		rows2.clear();
		columns2.clear();
		columnsAndRows2.clear();
		columnsAndRows2=BKingInCheck(board);
		//System.out.println(columnsAndRows2.size()+" 2");
		System.out.println("Black's Turn");
		boolean BnoKing=false, Bking=false, Ball=false, end=false;
		if(columnsAndRows2.size()>0)
		{
			int code=columnsAndRows2.get(columnsAndRows2.size()-1);
			if(code==11)
			{
				//no king move so block or take
				System.out.println("1. Black King In CHECK!\nMust Block the Check, No Legal Moves for King");
				BnoKing=true;
			}
			else if(code==22)
			{
				//king move or block or take	
				System.out.println("2. Black King In CHECK!\nMust Block the Check or use Legal Moves for King");
				Ball=true;
			}
			else if(code==33)
			{
				//king move
				System.out.println("3. Black King In CHECK!\nMust Move King");
				Bking=true;
			}
			else if(code==44)
			{
				//checkmate
				System.out.println("Checkmate");
				System.exit(0);
				//Bking=true;
			}
			columnsAndRows2.remove(columnsAndRows2.size()-1);
			//System.out.println(columnsAndRows2.size());
			for(int y=0; y<columnsAndRows2.size(); y+=2)
			{
				//System.out.print(columnsAndRows2.get(y));	
				//System.out.println(", "+columnsAndRows2.get(y+1));	
			}
		}
		end=Wstalemate(board);
		if(end)
		{
			System.out.println("Stalemate! No moves for white!");
			System.exit(0);	
		}
		end=Bstalemate(board);
		if(end)
		{
			System.out.println("Stalemate! No moves for Black!");
			System.exit(0);	
		}
		Scanner reader = new Scanner(System.in);
		char column=25;
		int row=-2334;
		//find piece you want to move
		while((row<1)||(row>8))
		{
			System.out.println("Enter the row of the piece you wish to move (Number 1-8) (0 - Resign) (-1 - Propose draw)");
			row=reader.nextInt();
			if(row==0)
			{
				System.out.println("Black resigns!");
				System.exit(0);	
				row=-23;
			}
			if(row==-1)
			{
				System.out.println("Black Proposes Draw!");
				char yn='i';
				while((yn!='y')&&(yn!='n'))
				{
					System.out.println("Does White accept? (y/n)");
					yn=reader.next().charAt(0);
				}
				if(yn=='y')
				{
					System.out.println("Its a draw!");
					System.exit(0);	
				}
				else
				{
					System.out.println("White declines");
				}
				row=-23;
			}
		}
		//must go down one 0-7
		row--;
		while((column<97)||(column>104))
		{
			System.out.println("Enter the column of the piece you with to move (character a-h)");
			column=reader.next().charAt(0);
		}
		//check if black
		if((board[row][column-97].substring(0,1).equals("W")))
		{
			System.out.println("Enter the location of a white piece please");
			/*row=-1;
			column=32;*/
			Bmove(board, wLost, bLost);
		}
		//if the space you chose is empty choose again
		if(((board[row][column-97].equals("  ")))||((board[row][column-97].equals("##"))))
		{
			System.out.println("Empty Spot: Enter the location of a piece please");
			/*row=-1;
			column=32;*/
			Bmove(board, wLost, bLost);
		}
		if((board[row][column-97].equals("BK"))&&(BnoKing))
		{
			System.out.println("No legal moves for Black king: Enter the location of a new piece please");
			/*row=-1;
			column=32;*/
			Bmove(board, wLost, bLost);
		}
		else if(!(board[row][column-97].equals("BK"))&&(Bking))
		{
			System.out.println("No other legal moves except for Black king: Enter the location of the black king please ");
			/*row=0;
			column=32;*/
			Bmove(board, wLost, bLost);
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
				ShowLegalMoves(FRow, FColumn, board);
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

		if((Ball)&&(!(board[FRow][FColumn].equals("BK"))))
		{
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
			for(int i=0; i<rows2.size(); i++)
			{
				if((rows2.get(i).equals(SRow))&&(columns2.get(i).equals(SColumn)))
				{
					blockOrTake=true;
				}
			}
			if(!blockOrTake)
			{
				System.out.println("Must block the check!");
				Bmove(board, wLost, bLost);
			}
		}
		if(BnoKing)
		{
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
			for(int i=0; i<rows2.size(); i++)
			{
				if((rows2.get(i).equals(SRow))&&(columns2.get(i).equals(SColumn)))
				{
					blockOrTake=true;
				}
			}
			if(!blockOrTake)
			{
				System.out.println("Must block the check!");
			/*	FRow=0;
				FColumn=32;*/
				Bmove(board, wLost, bLost);
			}		
		}

		boolean isCorrect=false;
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> checkinCheck = new ArrayList<Integer>();
		//return possible moves
		columnsAndRows=LegalMoves(FRow, FColumn, board, false);
		//check if given is legal
		for(int i=0; i<columnsAndRows.size(); i+=2)
		{
			if((SRow==columnsAndRows.get(i))&&(SColumn==columnsAndRows.get(i+1)))
			{
				isCorrect=true;
			}
		}
		String save[][] = new String [8][8];
		equalBoard(board, save);
		save[SRow][SColumn]=save[FRow][FColumn];
		save[FRow][FColumn]="  ";
		//System.out.println(checkinCheck.size());
		checkinCheck=BKingInCheck(save);
		if(checkinCheck.size()>0)
		{
			int code2=checkinCheck.get(checkinCheck.size()-1);
			if(code2>10)
			{
				System.out.println("Error: This Move caused your king to be checked pick a new spot to move");
				/*FRow=0;
				FColumn=32;*/
				Bmove(board, wLost, bLost);
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
			///switch
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
			FRow=-2;
			FColumn=-4;
			//print board after finding legal move
			print(board, wLost, bLost);
		}
		else
		{
			//illegal move, send back to top of move
			System.out.println("Illegal Move: Enter a valid location to move your piece");
			FRow=0;
			FColumn=32;
			Bmove(board, wLost, bLost);
		}
	}

	public void move(String[][] board, ArrayList<String> wLost, ArrayList<String> bLost)
	{
		ArrayList<Integer> check = new ArrayList<Integer>();
		ArrayList<Integer> rows2 = new ArrayList<Integer>();
		ArrayList<Integer> columns2 = new ArrayList<Integer>();
		ArrayList<Integer> columnsAndRows2 = new ArrayList<Integer>();
		ArrayList<Integer> columnsAndRows3 = new ArrayList<Integer>();
		ArrayList<Integer> rows3 = new ArrayList<Integer>();
		ArrayList<Integer> columns3 = new ArrayList<Integer>();
		rows2.clear();
		columns2.clear();
		columnsAndRows2.clear();
		rows3.clear();
		columns3.clear();
		columnsAndRows3.clear();
		columnsAndRows2=BKingInCheck(board);
		//System.out.println(columnsAndRows2.size()+" 2");
		boolean BnoKing=false, Bking=false, Ball=false, end=false;
		if(columnsAndRows2.size()>0)
		{
			int code=columnsAndRows2.get(columnsAndRows2.size()-1);
			if(code==11)
			{
				//no king move so block or take
				System.out.println("1. Black King In CHECK!\nMust Block the Check, No Legal Moves for King");
				BnoKing=true;
			}
			else if(code==22)
			{
				//king move or block or take	
				System.out.println("2. Black King In CHECK!\nMust Block the Check or use Legal Moves for King");
				Ball=true;
			}
			else if(code==33)
			{
				//king move
				System.out.println("3. Black King In CHECK!\nMust Move King");
				Bking=true;
			}
			else if(code==44)
			{
				//checkmate
				System.out.println("Checkmate");
				System.exit(0);
				//Bking=true;
			}
			columnsAndRows2.remove(columnsAndRows2.size()-1);
			//System.out.println(columnsAndRows2.size());
			for(int y=0; y<columnsAndRows2.size(); y+=2)
			{
				//System.out.print(columnsAndRows2.get(y));	
				//System.out.println(", "+columnsAndRows2.get(y+1));	
			}
		}
		end=Wstalemate(board);
		if(end)
		{
			System.out.println("Stalemate! No moves for white!");
			System.exit(0);	
		}
		end=Bstalemate(board);
		if(end)
		{
			System.out.println("Stalemate! No moves for Black!");
			System.exit(0);	
		}
		//System.out.println("qefgljehrouteqrglwtyuwiruqetwetgr"); 
		columnsAndRows3=WKingInCheck(board);
		//System.out.println(columnsAndRows3.size());
		boolean WnoKing=false, Wking=false, Wall=false;
		if(columnsAndRows3.size()>0)
		{

			int code=columnsAndRows3.get(columnsAndRows3.size()-1);
			//System.out.println(code);
			if(code==11)
			{
				//no king move so block or take
				System.out.println("1. White King In CHECK!\nMust Block the Check, No Legal Moves for King");
				WnoKing=true;
			}
			else if(code==22)
			{
				//king move or block or take	
				System.out.println("2. White King In CHECK!\nMust Block the Check or use Legal Moves for King");
				Wall=true;
			}
			else if(code==33)
			{
				//king move
				System.out.println("3. White King In CHECK!\nMust Move King");
				Wking=true;
			}
			else if(code==44)
			{
				//checkmate
				System.out.println("Checkmate");
				System.exit(0);
				//Bking=true;
			}
			columnsAndRows3.remove(columnsAndRows3.size()-1);
		}
		Scanner reader = new Scanner(System.in);
		char column=32;
		int row=0;
		//find piece you want to move
		while((row<1)||(row>8))
		{
			System.out.println("Enter the row of the piece you wish to move (Number 1-8)");
			row=reader.nextInt();
		}
		//must go down one 0-7
		row--;
		while((column<97)||(column>104))
		{
			System.out.println("Enter the column of the piece you with to move (character a-h)");
			column=reader.next().charAt(0);
		}
		//if the space you chose is empty choose again
		if(((board[row][column-97].equals("  ")))||((board[row][column-97].equals("##"))))
		{
			System.out.println("Empty Spot: Enter the location of a piece please");
			move(board, wLost, bLost);
		}
		if((board[row][column-97].equals("BK"))&&(BnoKing))
		{
			System.out.println("No legal moves for Black king: Enter the location of a new piece please");
			move(board, wLost, bLost);
		}
		if((board[row][column-97].equals("WK"))&&(WnoKing))
		{
			System.out.println("No legal moves for White king: Enter the location of a new piece please");
			move(board, wLost, bLost);
		}
		else if(!(board[row][column-97].equals("BK"))&&(Bking))
		{
			System.out.println("No other legal moves except for Black king: Enter the location of the black king please ");
			move(board, wLost, bLost);
		}
		else if(!(board[row][column-97].equals("WK"))&&(Wking))
		{
			System.out.println("No other legal moves except for White king: Enter the location of the white king please ");
			move(board, wLost, bLost);
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
				ShowLegalMoves(FRow, FColumn, board);
			}
		}
		//column
		while((column<97)||(column>104))
		{
			System.out.println("Enter the column of where you want to move your piece (character a-h)");
			column=reader.next().charAt(0);
		}

		//2nd moveBnoKing
		row--;
		int SRow=row, SColumn=(int)(column-97);
		boolean blockOrTake=false;
		if((Ball)&&(!(board[FRow][FColumn].equals("BK"))))
		{
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
			for(int i=0; i<rows2.size(); i++)
			{
				if((rows2.get(i).equals(SRow))&&(columns2.get(i).equals(SColumn)))
				{
					blockOrTake=true;
				}
			}
			if(!blockOrTake)
			{
				System.out.println("Must block the check!");
				move(board, wLost, bLost);
			}
		}
		if(BnoKing)
		{
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
			for(int i=0; i<rows2.size(); i++)
			{
				if((rows2.get(i).equals(SRow))&&(columns2.get(i).equals(SColumn)))
				{
					blockOrTake=true;
				}
			}
			if(!blockOrTake)
			{
				System.out.println("Must block the check!");
				move(board, wLost, bLost);
			}		
		}

		if((Wall)&&(!(board[FRow][FColumn].equals("WK"))))
		{
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
			for(int i=0; i<rows2.size(); i++)
			{
				if((rows2.get(i).equals(SRow))&&(columns2.get(i).equals(SColumn)))
				{
					blockOrTake=true;
				}
			}
			if(!blockOrTake)
			{
				System.out.println("Must block the check!");
				move(board, wLost, bLost);
			}
		}
		if(WnoKing)
		{
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
			for(int i=0; i<rows2.size(); i++)
			{
				if((rows2.get(i).equals(SRow))&&(columns2.get(i).equals(SColumn)))
				{
					blockOrTake=true;
				}
			}
			if(!blockOrTake)
			{
				System.out.println("Must block the check!");
				move(board, wLost, bLost);
			}		
		}
		boolean isCorrect=false;
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		//return possible moves
		columnsAndRows=LegalMoves(FRow, FColumn, board, false);
		//check if given is legal
		for(int i=0; i<columnsAndRows.size(); i+=2)
		{
			if((SRow==columnsAndRows.get(i))&&(SColumn==columnsAndRows.get(i+1)))
			{
				isCorrect=true;
			}
		}
		if(isCorrect)
		{
			System.out.println("This move is legal!\n");
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
			///switch
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
			FRow=-2;
			FColumn=-4;
			//print board after finding legal move
			print(board, wLost, bLost);
		}
		else
		{
			//illegal move, send back to top of move
			System.out.println("Illegal Move: Enter a valid location to move your piece");
			move(board, wLost, bLost);
		}
	}

	public boolean Wstalemate(String[][]Board)
	{
		ArrayList<Integer> unbox1 = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();

		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if(Board[r][c].substring(0, 1).equals("W"))
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
			if((rows.get(i)!=88)&&(Board[rows.get(i)][columns.get(i)].equals("WK")))
			{
				rows.remove(i);
				columns.remove(i);
				break;
			}
		}
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

	public boolean Bstalemate(String[][]Board)
	{
		ArrayList<Integer> unbox1 = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();

		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if(Board[r][c].substring(0, 1).equals("B"))
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
			if((rows.get(i)!=88)&&(Board[rows.get(i)][columns.get(i)].equals("BK")))
			{
				rows.remove(i);
				columns.remove(i);
				break;
			}
		}
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
		for(int i=0; i<rows.size(); i++)
		{
			//System.out.println(rows.get(i));
			//System.out.println(columns.get(i));
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

	public void ShowLegalMoves(int FRow, int FColumn, String[][]Board)
	{
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		columnsAndRows=LegalMoves(FRow, FColumn, Board, false);
		String Boardmoves[][] = new String [8][8];
		equalBoard(Board, Boardmoves);
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
		easyprint(Boardmoves);
	}

	public ArrayList<Integer> LegalMoves(int FRow, int FColumn, String[][]Board, boolean takes)
	{
		/*System.out.println("NOWWWWWWWWWWWWWWWWWWWWWWW");
		System.out.println(FRow+", "+FColumn);*/
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		rows.clear();
		columns.clear();
		columnsAndRows.clear();
		//white or black PAWNS
		if((Board[FRow][FColumn].equals("WP"))||(Board[FRow][FColumn].equals("BP")))
		{
			//white pawn
			if(Board[FRow][FColumn].equals("WP"))
			{
				if(takes)
				{
					//takes
					if((FColumn-1>-1)&&(FRow+1<8))
					{
						if(Board[FRow+1][FColumn-1].substring(0, 1).equals("B"))
						{
							rows.add(FRow+1);
							columns.add(FColumn-1);
						}
					}
					if((FColumn+1<8)&&(FRow+1<8))
					{
						if(Board[FRow+1][FColumn+1].substring(0, 1).equals("B"))
						{
							rows.add(FRow+1);
							columns.add(FColumn+1);
						}
					}
				}
				else
				{
					//first move (normal)
					if(((Board[FRow+1][FColumn].equals("  ")))||((Board[FRow+1][FColumn].equals("##"))))
					{
						rows.add(FRow+1);
						columns.add(FColumn);
					}
					//if on starting spot 2 moves up
					if(FRow==1)
					{
						if(((Board[FRow+1][FColumn].equals("  ")))||((Board[FRow+1][FColumn].equals("##"))))
						{
							if(((Board[FRow+2][FColumn].equals("  ")))||((Board[FRow+2][FColumn].equals("##"))))
							{
								rows.add(FRow+2);
								columns.add(FColumn);
							}
						}
					}
					//takes
					if((FColumn-1>-1)&&(FRow+1<8))
					{
						if(Board[FRow+1][FColumn-1].substring(0, 1).equals("B"))
						{
							rows.add(FRow+1);
							columns.add(FColumn-1);
						}
					}
					if((FColumn+1<8)&&(FRow+1<8))
					{
						if(Board[FRow+1][FColumn+1].substring(0, 1).equals("B"))
						{
							rows.add(FRow+1);
							columns.add(FColumn+1);
						}
					}
				}
			}
			//Black pawn
			else if(Board[FRow][FColumn].equals("BP"))
			{
				if(takes)
				{
					//takes
					if((FColumn-1>-1)&&(FRow-1>-1))
					{
						if(Board[FRow-1][FColumn-1].substring(0, 1).equals("W"))
						{
							rows.add(FRow-1);
							columns.add(FColumn-1);
						}
					}
					if((FColumn+1<8)&&(FRow-1>-1))
					{
						if(Board[FRow-1][FColumn+1].substring(0, 1).equals("W"))
						{
							rows.add(FRow-1);
							columns.add(FColumn+1);
						}
					}	
				}
				else
				{
					//first move (normal)
					if(((Board[FRow-1][FColumn].equals("  ")))||((Board[FRow-1][FColumn].equals("##"))))
					{
						rows.add(FRow-1);
						columns.add(FColumn);
					}
					//if on starting spot 2 moves up
					if(FRow==6)
					{
						if(((Board[FRow-1][FColumn].equals("  ")))||((Board[FRow-1][FColumn].equals("##"))))
						{
							if(((Board[FRow-2][FColumn].equals("  ")))||((Board[FRow-2][FColumn].equals("##"))))
							{
								rows.add(FRow-2);
								columns.add(FColumn);
							}
						}
					}
					//takes
					if((FColumn-1>-1)&&(FRow-1>-1))
					{
						if(Board[FRow-1][FColumn-1].substring(0, 1).equals("W"))
						{
							rows.add(FRow-1);
							columns.add(FColumn-1);
						}
					}
					if((FColumn+1<8)&&(FRow-1>-1))
					{
						if(Board[FRow-1][FColumn+1].substring(0, 1).equals("W"))
						{
							rows.add(FRow-1);
							columns.add(FColumn+1);
						}
					}	
				}

			}
		}
		//white and black knights
		else if((Board[FRow][FColumn].equals("WH"))||(Board[FRow][FColumn].equals("BH")))
		{
			//white knights
			if(Board[FRow][FColumn].equals("WH"))
			{
				//top 2 spaces
				if(FRow+2<8)
				{
					if(FColumn-1>-1)
					{
						if(!(Board[FRow+2][FColumn-1].substring(0, 1).equals("W")))
						{
							rows.add(FRow+2);
							columns.add(FColumn-1);
						}
						//Board[fMover+2][fMovec-1]	
					}
					if(FColumn+1<8)
					{
						if(!(Board[FRow+2][FColumn+1].substring(0, 1).equals("W")))
						{
							rows.add(FRow+2);
							columns.add(FColumn+1);
						}
						//Board[fMover+2][fMovec+1]
					}
				}
				//top of right and top of left
				if(FRow+1<8)
				{
					if(FColumn+2<8)
					{
						if(!(Board[FRow+1][FColumn+2].substring(0, 1).equals("W")))
						{
							rows.add(FRow+1);
							columns.add(FColumn+2);
						}
						//Board[fMover+1][fMovec+2]
					}
					if(FColumn-2>-1)
					{
						if(!(Board[FRow+1][FColumn-2].substring(0, 1).equals("W")))
						{
							rows.add(FRow+1);
							columns.add(FColumn-2);
						}
						//Board[fMover+1][fMovec-2]
					}
				}
				//bottom of left and right
				if(FRow-1>-1)
				{
					if(FColumn-2>-1)
					{
						if(!(Board[FRow-1][FColumn-2].substring(0, 1).equals("W")))
						{
							rows.add(FRow-1);
							columns.add(FColumn-2);
						}
						//Board[fMover-1][fMovec-2]
					}
					if(FColumn+2<8)
					{
						if(!(Board[FRow-1][FColumn+2].substring(0, 1).equals("W")))
						{
							rows.add(FRow-1);
							columns.add(FColumn+2);
						}
						//Board[fMover-1][fMovec+2]
					}
				}
				//bottom 2
				if(FRow-2>-1)
				{
					if(FColumn+1<8)
					{
						if(!(Board[FRow-2][FColumn+1].substring(0, 1).equals("W")))
						{
							rows.add(FRow-2);
							columns.add(FColumn+1);
						}
						//Board[fMover-2][fMovec+1]
					}
					if(FColumn-1>-1)
					{
						if(!(Board[FRow-2][FColumn-1].substring(0, 1).equals("W")))
						{
							rows.add(FRow-2);
							columns.add(FColumn-1);
						}
						//Board[fMover-2][fMovec-1]
					}
				}

			}
			//black knights
			else if(Board[FRow][FColumn].equals("BH"))
			{
				//top 2 spaces
				if(FRow+2<8)
				{
					if(FColumn-1>-1)
					{
						if(!(Board[FRow+2][FColumn-1].substring(0, 1).equals("B")))
						{
							rows.add(FRow+2);
							columns.add(FColumn-1);
						}
						//Board[fMover+2][fMovec-1]	
					}
					if(FColumn+1<8)
					{
						if(!(Board[FRow+2][FColumn+1].substring(0, 1).equals("B")))
						{
							rows.add(FRow+2);
							columns.add(FColumn+1);
						}
						//Board[fMover+2][fMovec+1]

					}
				}
				//top of right and top of left
				if(FRow+1<8)
				{
					if(FColumn+2<8)
					{
						if(!(Board[FRow+1][FColumn+2].substring(0, 1).equals("B")))
						{
							rows.add(FRow+1);
							columns.add(FColumn+2);
						}
						//Board[fMover+1][fMovec+2]
					}
					if(FColumn-2>-1)
					{
						if(!(Board[FRow+1][FColumn-2].substring(0, 1).equals("B")))
						{
							rows.add(FRow+1);
							columns.add(FColumn-2);
						}
						//Board[fMover+1][fMovec-2]
					}
				}
				//bottom of left and right
				if(FRow-1>-1)
				{
					if(FColumn-2>-1)
					{
						if(!(Board[FRow-1][FColumn-2].substring(0, 1).equals("B")))
						{
							rows.add(FRow-1);
							columns.add(FColumn-2);
						}
						//Board[fMover-1][fMovec-2]
					}
					if(FColumn+2<8)
					{
						if(!(Board[FRow-1][FColumn+2].substring(0, 1).equals("B")))
						{
							rows.add(FRow-1);
							columns.add(FColumn+2);
						}
						//Board[fMover-1][fMovec+2]
					}
				}
				//bottom 2
				if(FRow-2>-1)
				{
					if(FColumn+1<8)
					{
						if(!(Board[FRow-2][FColumn+1].substring(0, 1).equals("B")))
						{
							rows.add(FRow-2);
							columns.add(FColumn+1);
						}
						//Board[fMover-2][fMovec+1]
					}
					if(FColumn-1>-1)
					{
						if(!(Board[FRow-2][FColumn-1].substring(0, 1).equals("B")))
						{
							rows.add(FRow-2);
							columns.add(FColumn-1);
						}
						//Board[fMover-2][fMovec-1]
					}
				}

			}
		}
		//first check stop
		//white and black rooks
		else if((Board[FRow][FColumn].equals("WR"))||(Board[FRow][FColumn].equals("BR")))
		{
			//white rook
			if(Board[FRow][FColumn].equals("WR"))
			{

				//start/end to portion
				rows.add(88);
				columns.add(88);

				//right
				if(FColumn+1<8)
				{
					for(int i=FColumn+1; i<8; i++)
					{
						if(!(Board[FRow][i].substring(0, 1).equals("W")))
						{
							rows.add(FRow);
							columns.add(i);
							if((Board[FRow][i].substring(0, 1).equals("B")))
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
						if(!(Board[FRow][i].substring(0, 1).equals("W")))
						{
							rows.add(FRow);
							columns.add(i);
							if((Board[FRow][i].substring(0, 1).equals("B")))
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
						if(!(Board[i][FColumn].substring(0, 1).equals("W")))
						{
							rows.add(i);
							columns.add(FColumn);
							if((Board[i][FColumn].substring(0, 1).equals("B")))
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
						if(!(Board[i][FColumn].substring(0, 1).equals("W")))
						{
							rows.add(i);
							columns.add(FColumn);
							if((Board[i][FColumn].substring(0, 1).equals("B")))
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
			//black rook
			if(Board[FRow][FColumn].equals("BR"))
			{

				//start/end to portion
				rows.add(88);
				columns.add(88);

				//right
				if(FColumn+1<8)
				{
					for(int i=FColumn+1; i<8; i++)
					{
						if(!(Board[FRow][i].substring(0, 1).equals("B")))
						{
							rows.add(FRow);
							columns.add(i);
							if((Board[FRow][i].substring(0, 1).equals("W")))
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
						if(!(Board[FRow][i].substring(0, 1).equals("B")))
						{
							rows.add(FRow);
							columns.add(i);
							if((Board[FRow][i].substring(0, 1).equals("W")))
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
						if(!(Board[i][FColumn].substring(0, 1).equals("B")))
						{
							rows.add(i);
							columns.add(FColumn);
							if((Board[i][FColumn].substring(0, 1).equals("W")))
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
						if(!(Board[i][FColumn].substring(0, 1).equals("B")))
						{
							rows.add(i);
							columns.add(FColumn);
							if((Board[i][FColumn].substring(0, 1).equals("W")))
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
		}
		//white and black bishop
		else if((Board[FRow][FColumn].equals("WB"))||(Board[FRow][FColumn].equals("BB")))
		{
			//white bishop
			if(Board[FRow][FColumn].equals("WB"))
			{

				//start/end to portion
				rows.add(88);
				columns.add(88);

				//up right
				int j=1;
				while((FRow+j<8)&&(FColumn+j<8))
				{
					if(!(Board[FRow+j][FColumn+j].substring(0, 1).equals("W")))
					{
						rows.add(FRow+j);
						columns.add(FColumn+j);
						if((Board[FRow+j][FColumn+j].substring(0, 1).equals("B")))
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
					if(!(Board[FRow-j][FColumn+j].substring(0, 1).equals("W")))
					{
						rows.add(FRow-j);
						columns.add(FColumn+j);
						if((Board[FRow-j][FColumn+j].substring(0, 1).equals("B")))
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
					if(!(Board[FRow+j][FColumn-j].substring(0, 1).equals("W")))
					{
						rows.add(FRow+j);
						columns.add(FColumn-j);
						if((Board[FRow+j][FColumn-j].substring(0, 1).equals("B")))
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
					if(!(Board[FRow-j][FColumn-j].substring(0, 1).equals("W")))
					{
						rows.add(FRow-j);
						columns.add(FColumn-j);
						if((Board[FRow-j][FColumn-j].substring(0, 1).equals("B")))
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
			//black bishop
			if(Board[FRow][FColumn].equals("BB"))
			{

				//start/end to portion
				rows.add(88);
				columns.add(88);

				//up right
				int j=1;
				while((FRow+j<8)&&(FColumn+j<8))
				{
					if(!(Board[FRow+j][FColumn+j].substring(0, 1).equals("B")))
					{
						rows.add(FRow+j);
						columns.add(FColumn+j);
						if((Board[FRow+j][FColumn+j].substring(0, 1).equals("W")))
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
					if(!(Board[FRow-j][FColumn+j].substring(0, 1).equals("B")))
					{
						rows.add(FRow-j);
						columns.add(FColumn+j);
						if((Board[FRow-j][FColumn+j].substring(0, 1).equals("W")))
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
					if(!(Board[FRow+j][FColumn-j].substring(0, 1).equals("B")))
					{
						rows.add(FRow+j);
						columns.add(FColumn-j);
						if((Board[FRow+j][FColumn-j].substring(0, 1).equals("W")))
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
					if(!(Board[FRow-j][FColumn-j].substring(0, 1).equals("B")))
					{
						rows.add(FRow-j);
						columns.add(FColumn-j);
						if((Board[FRow-j][FColumn-j].substring(0, 1).equals("W")))
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
		}
		//black and white queen
		else if((Board[FRow][FColumn].equals("WQ"))||(Board[FRow][FColumn].equals("BQ")))
		{
			//white queen
			if(Board[FRow][FColumn].equals("WQ"))
			{
				//bishop movement

				//start/end to portion
				rows.add(88);
				columns.add(88);

				//up right
				int j=1;
				while((FRow+j<8)&&(FColumn+j<8))
				{
					if(!(Board[FRow+j][FColumn+j].substring(0, 1).equals("W")))
					{
						rows.add(FRow+j);
						columns.add(FColumn+j);
						if((Board[FRow+j][FColumn+j].substring(0, 1).equals("B")))
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
					if(!(Board[FRow-j][FColumn+j].substring(0, 1).equals("W")))
					{
						rows.add(FRow-j);
						columns.add(FColumn+j);
						if((Board[FRow-j][FColumn+j].substring(0, 1).equals("B")))
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
					if(!(Board[FRow+j][FColumn-j].substring(0, 1).equals("W")))
					{
						rows.add(FRow+j);
						columns.add(FColumn-j);
						if((Board[FRow+j][FColumn-j].substring(0, 1).equals("B")))
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
					if(!(Board[FRow-j][FColumn-j].substring(0, 1).equals("W")))
					{
						rows.add(FRow-j);
						columns.add(FColumn-j);
						if((Board[FRow-j][FColumn-j].substring(0, 1).equals("B")))
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
						if(!(Board[FRow][i].substring(0, 1).equals("W")))
						{
							rows.add(FRow);
							columns.add(i);
							if((Board[FRow][i].substring(0, 1).equals("B")))
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
						if(!(Board[FRow][i].substring(0, 1).equals("W")))
						{
							rows.add(FRow);
							columns.add(i);
							if((Board[FRow][i].substring(0, 1).equals("B")))
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
						if(!(Board[i][FColumn].substring(0, 1).equals("W")))
						{
							rows.add(i);
							columns.add(FColumn);
							if((Board[i][FColumn].substring(0, 1).equals("B")))
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
						if(!(Board[i][FColumn].substring(0, 1).equals("W")))
						{
							rows.add(i);
							columns.add(FColumn);
							if((Board[i][FColumn].substring(0, 1).equals("B")))
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
			//black queen
			else if(Board[FRow][FColumn].equals("BQ"))
			{
				//bishop movement

				//start/end to portion
				rows.add(88);
				columns.add(88);

				//up right
				int j=1;
				while((FRow+j<8)&&(FColumn+j<8))
				{
					if(!(Board[FRow+j][FColumn+j].substring(0, 1).equals("B")))
					{
						rows.add(FRow+j);
						columns.add(FColumn+j);
						if((Board[FRow+j][FColumn+j].substring(0, 1).equals("W")))
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
					if(!(Board[FRow-j][FColumn+j].substring(0, 1).equals("B")))
					{
						rows.add(FRow-j);
						columns.add(FColumn+j);
						if((Board[FRow-j][FColumn+j].substring(0, 1).equals("W")))
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
					if(!(Board[FRow+j][FColumn-j].substring(0, 1).equals("B")))
					{
						rows.add(FRow+j);
						columns.add(FColumn-j);
						if((Board[FRow+j][FColumn-j].substring(0, 1).equals("W")))
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
					if(!(Board[FRow-j][FColumn-j].substring(0, 1).equals("B")))
					{
						rows.add(FRow-j);
						columns.add(FColumn-j);
						if((Board[FRow-j][FColumn-j].substring(0, 1).equals("W")))
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
						if(!(Board[FRow][i].substring(0, 1).equals("B")))
						{
							rows.add(FRow);
							columns.add(i);
							if((Board[FRow][i].substring(0, 1).equals("W")))
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
						if(!(Board[FRow][i].substring(0, 1).equals("B")))
						{
							rows.add(FRow);
							columns.add(i);
							if((Board[FRow][i].substring(0, 1).equals("W")))
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
						if(!(Board[i][FColumn].substring(0, 1).equals("B")))
						{
							rows.add(i);
							columns.add(FColumn);
							if((Board[i][FColumn].substring(0, 1).equals("W")))
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
						if(!(Board[i][FColumn].substring(0, 1).equals("B")))
						{
							rows.add(i);
							columns.add(FColumn);
							if((Board[i][FColumn].substring(0, 1).equals("W")))
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
		}

		//black king and white king
		if((Board[FRow][FColumn].equals("BK"))||(Board[FRow][FColumn].equals("WK")))
		{
			columnsAndRows.clear();
			rows.clear();
			columns.clear();
			if(Board[FRow][FColumn].equals("BK"))
			{
				//Black king legal moves, returns list of legal moves
				columnsAndRows=BKingLegalMovesThretened(FRow, FColumn,Board);

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
			else if(Board[FRow][FColumn].equals("WK"))
			{
				//White king legal moves, returns list of legal moves
				columnsAndRows=WKingLegalMovesThretened(FRow, FColumn,Board);

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
		}
		//return rows and columns
		for(int i=0; i<rows.size(); i++)
		{
			columnsAndRows.add(rows.get(i));
			columnsAndRows.add(columns.get(i));
		}
		return columnsAndRows;
	}
	//Black king legal moves, returns list of legal moves
	public ArrayList<Integer> BKingLegalMovesThretened(int FRow, int FColumn, String[][]Board)
	{
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> unbox1 = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		ArrayList<Integer> rows2 = new ArrayList<Integer>();
		ArrayList<Integer> columns2 = new ArrayList<Integer>();
		unbox1.clear();
		String BKMoves[][] = new String [8][8];
		String Protect1[][] = new String [8][8];
		equalBoard(Board, BKMoves);
		equalBoard(Board, Protect1);
		Protect1[FRow][FColumn]="  ";
		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if((Protect1[r][c].substring(0, 1).equals("W"))&&(!(Protect1[r][c].equals("WK"))))
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
							BKMoves[rows.get(i)][columns.get(i)]="XX";
						}
					}

				}
			}
		}
		Protect1[FRow][FColumn]="BK";
		rows.clear();
		columns.clear();
		columnsAndRows.clear();
		//moves
		//up
		if(FRow+1<8)
		{
			if(!(BKMoves[FRow+1][FColumn].equals("XX")))
			{
				if(!(Board[FRow+1][FColumn].substring(0, 1).equals("B")))
				{
					rows.add(FRow+1);	
					columns.add(FColumn);
				}

			}
			//up right
			if(FColumn+1<8)
			{
				if(!(BKMoves[FRow+1][FColumn+1].equals("XX")))
				{
					if(!(Board[FRow+1][FColumn+1].substring(0, 1).equals("B")))
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
			if(!(BKMoves[FRow][FColumn+1].equals("XX")))
			{
				if(!(Board[FRow][FColumn+1].substring(0, 1).equals("B")))
				{
					rows.add(FRow);	
					columns.add(FColumn+1);
				}
			}
			//down right
			if(FRow-1>-1)
			{
				if(!(BKMoves[FRow-1][FColumn+1].equals("XX")))
				{
					if(!(Board[FRow-1][FColumn+1].substring(0, 1).equals("B")))
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
			if(!(BKMoves[FRow-1][FColumn].equals("XX")))
			{
				if(!(Board[FRow-1][FColumn].substring(0, 1).equals("B")))
				{
					rows.add(FRow-1);	
					columns.add(FColumn);
				}
			}
			//down left
			if(FColumn-1>-1)
			{
				if(!(BKMoves[FRow-1][FColumn-1].equals("XX")))
				{
					if(!(Board[FRow-1][FColumn-1].substring(0, 1).equals("B")))
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
			if(!(BKMoves[FRow][FColumn-1].equals("XX")))
			{
				if(!(Board[FRow][FColumn-1].substring(0, 1).equals("B")))
				{
					rows.add(FRow);	
					columns.add(FColumn-1);
				}
			}
			//up left
			if(FRow+1<8)
			{
				if(!(BKMoves[FRow+1][FColumn-1].equals("XX")))
				{
					if(!(Board[FRow+1][FColumn-1].substring(0, 1).equals("B")))
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
			//System.out.print(rows.get(i)+", ");
			//System.out.println(columns.get(i));
		}
		for(int i=0; i<rows.size(); i++)
		{
			if(rows.get(i)!=88)
			{
				equalBoard(Board, Protect);
				Protect[rows.get(i)][columns.get(i)]="BK";
				Protect[FRow][FColumn]="  ";
				for(int r=0; r<Board.length; r++)
				{
					for(int c=0; c<Board[0].length; c++)
					{
						if((Board[r][c].substring(0, 1).equals("W"))&&(!(Board[r][c].equals("WK"))))
						{
							//get legal moves
							//System.out.print("row: "+r);
							//System.out.println("column: "+c);
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

								if((rows2.get(j)!=88)&&(Protect[rows2.get(j)][columns2.get(j)].equals("BK")))
								{
									//System.out.println(rows2.get(j)+",weweweweweweweweweweweweweweeewweeeewwww "+columns2.get(j));
									if(!(i>=rows.size()))
									{
										rows.remove(i);
										columns.remove(i);
										break;
									}

									//System.out.println("lkwrqgahrpweghpiu3reg");

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
		/*System.out.println(rows.size());
		System.out.println("final");
		for(int i=0; i<rows.size(); i++)
		{
			System.out.print(rows.get(i)+", ");
			System.out.println(columns.get(i));
		}*/
		//return rows and columns
		for(int i=0; i<rows.size(); i++)
		{
			columnsAndRows.add(rows.get(i));
			//System.out.println(rows.get(i));
			columnsAndRows.add(columns.get(i));
			//System.out.println(columns.get(i));
		}
		return columnsAndRows;
	}
	//White king legal moves, returns list of legal moves
	public ArrayList<Integer> WKingLegalMovesThretened(int FRow, int FColumn, String[][]Board)
	{
		ArrayList<Integer> columnsAndRows = new ArrayList<Integer>();
		ArrayList<Integer> unbox1 = new ArrayList<Integer>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		ArrayList<Integer> columns = new ArrayList<Integer>();
		ArrayList<Integer> rows2 = new ArrayList<Integer>();
		ArrayList<Integer> columns2 = new ArrayList<Integer>();
		ArrayList<Integer> rows3 = new ArrayList<Integer>();
		ArrayList<Integer> columns3 = new ArrayList<Integer>();
		columns3.clear();
		rows3.clear();
		unbox1.clear();
		String WKMoves[][] = new String [8][8];
		String Protect1[][] = new String [8][8];
		equalBoard(Board, WKMoves);
		equalBoard(Board, Protect1);
		Protect1[FRow][FColumn]="  ";
		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if((Protect1[r][c].substring(0, 1).equals("B"))&&(!(Protect1[r][c].equals("BK"))))
				{
					//get legal moves
					unbox1=LegalMoves(r,c,Protect1, true);	
					//unbox columnsAndRows
					for(int i=0; i<unbox1.size(); i++)
					{
						if(i%2==0)
						{
							rows3.add(unbox1.get(i));
						}
						else
						{
							columns3.add(unbox1.get(i));
						}
					}
					//fill moves w possible moves and the type of piece
					for(int i=0; i<rows3.size(); i++)
					{ 
						if(!(rows3.get(i)==88))
						{
							WKMoves[rows3.get(i)][columns3.get(i)]="XX";
						}
					}

				}
			}
		}
		Protect1[FRow][FColumn]="WK";
		rows.clear();
		columns.clear();
		columnsAndRows.clear();
		//moves
		//up
		if(FRow+1<8)
		{
			if(!(WKMoves[FRow+1][FColumn].equals("XX")))
			{
				if(!(Board[FRow+1][FColumn].substring(0, 1).equals("W")))
				{
					rows.add(FRow+1);	
					columns.add(FColumn);
				}

			}
			//up right
			if(FColumn+1<8)
			{
				if(!(WKMoves[FRow+1][FColumn+1].equals("XX")))
				{
					if(!(Board[FRow+1][FColumn+1].substring(0, 1).equals("W")))
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
			if(!(WKMoves[FRow][FColumn+1].equals("XX")))
			{
				if(!(Board[FRow][FColumn+1].substring(0, 1).equals("W")))
				{
					rows.add(FRow);	
					columns.add(FColumn+1);
				}
			}
			//down right
			if(FRow-1>-1)
			{
				if(!(WKMoves[FRow-1][FColumn+1].equals("XX")))
				{
					if(!(Board[FRow-1][FColumn+1].substring(0, 1).equals("W")))
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
			if(!(WKMoves[FRow-1][FColumn].equals("XX")))
			{
				if(!(Board[FRow-1][FColumn].substring(0, 1).equals("W")))
				{
					rows.add(FRow-1);	
					columns.add(FColumn);
				}
			}
			//down left
			if(FColumn-1>-1)
			{
				if(!(WKMoves[FRow-1][FColumn-1].equals("XX")))
				{
					if(!(Board[FRow-1][FColumn-1].substring(0, 1).equals("W")))
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
			if(!(WKMoves[FRow][FColumn-1].equals("XX")))
			{
				if(!(Board[FRow][FColumn-1].substring(0, 1).equals("W")))
				{
					rows.add(FRow);	
					columns.add(FColumn-1);
				}
			}
			//up left
			if(FRow+1<8)
			{
				if(!(WKMoves[FRow+1][FColumn-1].equals("XX")))
				{
					if(!(Board[FRow+1][FColumn-1].substring(0, 1).equals("W")))
					{
						rows.add(FRow+1);	
						columns.add(FColumn-1);
					}
				}
			}
		}
		//easyprint(WKMoves);
		//problem is here
		columnsAndRows.clear();
		//switch king and check if he would be checked
		String Protect[][] = new String [8][8];
		for(int i=0; i<rows.size(); i++)
		{
			//System.out.print(rows.get(i)+", ");
			//System.out.println(columns.get(i));
		}
		for(int i=0; i<rows.size(); i++)
		{
			if(rows.get(i)!=88)
			{
				equalBoard(Board, Protect);
				Protect[rows.get(i)][columns.get(i)]="WK";
				Protect[FRow][FColumn]="  ";
				for(int r=0; r<Board.length; r++)
				{
					for(int c=0; c<Board[0].length; c++)
					{
						if((Board[r][c].substring(0, 1).equals("B"))&&(!(Board[r][c].equals("BK"))))
						{
							//get legal moves
							//System.out.print("row: "+r);
							//System.out.println("column: "+c);
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

								if((!(rows2.get(j)==88))&&(Protect[rows2.get(j)][columns2.get(j)].equals("WK")))
								{
									//System.out.println(rows2.get(j)+", "+columns2.get(j));
									rows.remove(i);
									columns.remove(i);
									//System.out.println("lkwrqgahrpweghpiu3reg");
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

		columnsAndRows.clear();
		/*System.out.println(rows.size());
		System.out.println("final");
		for(int i=0; i<rows.size(); i++)
		{
			System.out.print(rows.get(i)+", ");
			System.out.println(columns.get(i));
		}*/
		//return rows and columns
		for(int i=0; i<rows.size(); i++)
		{
			columnsAndRows.add(rows.get(i));
			//System.out.println(rows.get(i));
			columnsAndRows.add(columns.get(i));
			//System.out.println(columns.get(i));
		}
		return columnsAndRows;
	}

	public ArrayList<Integer> BKingInCheck(String[][]Board)
	{
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
		String BKMoves[][] = new String [8][8];
		equalBoard(Board, BKMoves);
		boolean check=false;
		int doublecheck=0;
		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if((Board[r][c].substring(0, 1).equals("W"))&&(!(Board[r][c].substring(1).equals("K"))))
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
						if((rows.get(i)!=88)&&(Board[rows.get(i)][columns.get(i)].equals("BK")))
						{
							if(doublecheck==0)
							{
								check=true;
								//normal
								//System.out.println(r);
								//System.out.println(c);
								//System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
								BkTRow=rows.get(i);
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
						//System.out.println("weqfjghweuyfwuiqefdupqwerh[pihf");
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
								if((rows.get(d)!=88)&&(Board[rows.get(d)][columns.get(d)].equals("BK")))
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
									if(!(Board[rows.get(d)][columns.get(d)].equals("BK")))
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
		boolean checkmate;
		CheckcolumnsAndRows=BKingLegalMovesThretened(BkTRow,BkTColumn, Board);	
		//CheckcolumnsAndRows=BKingLegalMovesThretened(BkTRow,BkTColumn, Board);
		checkmate=BKingInCheckMate(Board, columnsAndRowsneeded);
		if(check)
		{
			int amount=0;
			if((CheckcolumnsAndRows.size()==0)&&(checkmate))
			{
				//no king move so block or take by piece
				amount=11;
			}
			if((CheckcolumnsAndRows.size()>0)&&(!checkmate))
			{
				//king move
				amount=33;
			}
			if((CheckcolumnsAndRows.size()>0)&&(checkmate))
			{
				//king move or block or take by piece
				amount=22;
			}
			if(doublecheck>1)
			{
				//king must move
				amount=33;
			}
			if((CheckcolumnsAndRows.size()==0)&&(!checkmate))
			{
				//check mate
				amount=44;
			}
			columnsAndRowsneeded.add(amount);
		}
		return columnsAndRowsneeded;
	}
	//wwwwwwwwwwwwwwwwww

	public ArrayList<Integer> WKingInCheck(String[][]Board)
	{
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
		String BKMoves[][] = new String [8][8];
		equalBoard(Board, BKMoves);
		boolean check=false;
		int doublecheck=0;
		for(int r=0; r<Board.length; r++)
		{
			for(int c=0; c<Board[0].length; c++)
			{
				if((Board[r][c].substring(0, 1).equals("B"))&&(!(Board[r][c].substring(1).equals("K"))))
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
						if(((rows.get(i)!=88))&&(Board[rows.get(i)][columns.get(i)].equals("WK")))
						{
							if(doublecheck==0)
							{
								check=true;
								//normal
								/*System.out.println(r);
								System.out.println(c);
								System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");*/
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
						//System.out.println("weqfjghweuyfwuiqefdupqwerh[pihf");
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
								if((rows.get(d)!=88)&&(Board[rows.get(d)][columns.get(d)].equals("WK")))
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
									if(!(Board[rows.get(d)][columns.get(d)].equals("WK")))
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
			//System.out.println(rowsneeded.get(i)+", "+columnsneeded.get(i));
			columnsAndRowsneeded.add(rowsneeded.get(i));
			columnsAndRowsneeded.add(columnsneeded.get(i));
		}
		boolean checkmate=false;
		CheckcolumnsAndRows.clear();
		CheckcolumnsAndRows=WKingLegalMovesThretened(BkTRow,BkTColumn, Board);
		checkmate=WKingInCheckMate(Board, columnsAndRowsneeded);
		if(check)
		{
			int amount=0;

			if((CheckcolumnsAndRows.size()==0)&&(checkmate))
			{
				//no king move so block or take by piece
				amount=11;
				System.out.println("1st");
			}
			if((CheckcolumnsAndRows.size()>0)&&(!checkmate))
			{
				//king move
				amount=33;
				System.out.println("2nd");
			}
			if((CheckcolumnsAndRows.size()>0)&&(checkmate))
			{
				//king move or block or take by piece
				amount=22;
				System.out.println("3rd");
			}
			if(doublecheck>1)
			{
				//king must move
				amount=33;
				System.out.println("4th");
			}
			System.out.println(CheckcolumnsAndRows.size());

			if((CheckcolumnsAndRows.size()==0)&&(!checkmate))
			{
				//check mate
				amount=44;
				System.out.println("5th");
			}
			columnsAndRowsneeded.add(amount);
		}
		return columnsAndRowsneeded;
	}

	//wwwwwwwwwwwwwwwwwwwwwwwwww
	//declare arrylist check that the king's pieces are able to block
	public boolean BKingInCheckMate(String[][]Board, ArrayList<Integer> columnsAndRows2)
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
				if((Board[r][c].substring(0, 1).equals("B"))&&(!(Board[r][c].equals("BK"))))
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
										/*System.out.println(rows.get(i)+", "+columns.get(i));
										System.out.println(rows2.get(p)+", "+columns2.get(p));
										System.out.println(Board[r][c]);
										System.out.println(r+", "+c);*/
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

	public boolean WKingInCheckMate(String[][]Board, ArrayList<Integer> columnsAndRows2)
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
				if((Board[r][c].substring(0, 1).equals("W"))&&(!(Board[r][c].equals("WK"))))
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
										/*System.out.println(rows.get(i)+", "+columns.get(i));
										System.out.println(rows2.get(p)+", "+columns2.get(p));
										System.out.println(Board[r][c]);
										System.out.println(r+", "+c);*/
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
}