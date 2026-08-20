import java.util.ArrayList;
public class ChessMain
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		ArrayList<String> WLost = new ArrayList<String>();
		ArrayList<String> BLost = new ArrayList<String>();
		Board board = new Board();
		MoveGenerator moveGen = new MoveGenerator(board);
		Game game = new Game(board, moveGen);
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
