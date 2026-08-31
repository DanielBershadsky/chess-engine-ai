import java.util.ArrayList;
import java.util.Scanner;
public class ChessMain
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		ArrayList<String> WLost = new ArrayList<String>();
		ArrayList<String> BLost = new ArrayList<String>();
		Board board = new Board();
		MoveGenerator moveGen = new MoveGenerator(board);

		Scanner menuReader = new Scanner(System.in);
		int choice=0;
		while((choice!=1)&&(choice!=2))
		{
			System.out.println("1. Human vs Human");
			System.out.println("2. Human vs AI");
			choice=menuReader.nextInt();
		}
		Game game;
		if(choice==2)
		{
			int difficulty=0;
			while((difficulty!=1)&&(difficulty!=2)&&(difficulty!=3))
			{
				System.out.println("Choose a difficulty:");
				System.out.println("1. Easy");
				System.out.println("2. Medium");
				System.out.println("3. Hard");
				difficulty=menuReader.nextInt();
			}
			int depth = (difficulty==1)?2:((difficulty==2)?3:4);
			ChessAI ai = new ChessAI(board, moveGen, depth);
			//human is always White, AI is always Black
			game = new Game(board, moveGen, ai, "B");
		}
		else
		{
			game = new Game(board, moveGen);
		}

		String boardArray[][] = new String [8][8];
		board.BuildBoard(boardArray);
		board.print(boardArray, WLost, BLost);
		for(;;)
		{
			game.move(boardArray, WLost, BLost, "W");
			//board.print(boardArray, WLost, BLost);
		}
	}

}
