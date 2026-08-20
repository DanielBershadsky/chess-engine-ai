import java.util.ArrayList;

public class Board
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
}
