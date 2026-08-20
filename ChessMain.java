import java.util.ArrayList;
public class ChessMain
{

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		ArrayList<String> WLost = new ArrayList<String>();
		ArrayList<String> BLost = new ArrayList<String>();
		ChessMethod object = new ChessMethod();
		String board[][] = new String [8][8];
		object.BuildBoard(board);
		object.print(board, WLost, BLost);
		for(;;)
		{
			object.Wmove(board, WLost, BLost);
			//object.print(board, WLost, BLost);
		}
	}

}
