
public class play {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		char player1='O';
		char player2='X';
		
		System.out.println("<TicTacToe game: player1 ='O', player2 = 'X'>");
	
		TicTacToe myGame= new TicTacToe();
	
		myGame.displayBoard();	
		System.out.println("�ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ<start>ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ�");
	
		while(true) {
			myGame.whoseTurn(player1);
			myGame.addMove(player1);
			myGame.displayBoard();	
			myGame.checkIfWinner();
			
			myGame.whoseTurn(player2);
			myGame.addMove(player2);
			myGame.displayBoard();	
			myGame.checkIfWinner();
		}		
	}
}
