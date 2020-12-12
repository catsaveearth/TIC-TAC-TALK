import java.util.Scanner;

public class TicTacToe {
	char gameboard[][]= {{' ',' ',' '},{' ',' ',' '},{' ',' ',' '}};

	static int count=0;
	Scanner keyboard= new Scanner(System.in);
	
	public void addMove(char player) {
		System.out.print("Enter x, y in gameboard[x][y](ex.0 1): ");
		
		int x=keyboard.nextInt();
		int y=keyboard.nextInt();
		
		if(gameboard[x][y]!=' ') {
			System.out.println("Error: the position is not empty");
			System.exit(0);
		}
			
		gameboard[x][y]=player;
		count++;
	}
	
	public void displayBoard() {
		System.out.println("          �ㅡㅡㅡㅡㅡㅡ�");
		for(int i=0;i<gameboard.length;i++) {
			System.out.print(i+"  | ");
			for(int j=0;j<gameboard[i].length;j++) {
				System.out.print(gameboard[i][j]+"| ");
			}
			System.out.println();
			System.out.println("          �ㅡㅡㅡㅡㅡㅡ�");
		}
		System.out.println("x/y  0  1  2");
	}
	
	public void whoseTurn(char player) {
		if(player=='O') {
			System.out.println("player1, O's turn.");
		}
		else {
			System.out.println("player1, X's turn.");			
		}
	}

	public boolean checkIfWinner() {
		for(int i=0;i<gameboard.length;i++) {
			if (((gameboard[i][0]=='O')||(gameboard[i][0]=='X')) && (gameboard[i][0] == gameboard[i][1]) && (gameboard[i][0] == gameboard[i][2])) {
				System.out.println("There is a Winner");
				checkWhoWinner();
				return true;
			}
			else if (((gameboard[0][i]=='O')||(gameboard[0][i]=='X')) && (gameboard[0][i] == gameboard[1][i]) && (gameboard[0][i] == gameboard[2][i])) {
				System.out.println("There is a Winner");
				checkWhoWinner();
				return true;
			}			
		}
		
		if (((gameboard[0][0]=='O')||(gameboard[0][0]=='X')) && (gameboard[0][0] == gameboard[1][1]) && (gameboard[0][0] == gameboard[2][2])) {
			System.out.println("There is a Winner");
			checkWhoWinner();
			return true;
		}	
		
		else if (((gameboard[0][2]=='O')||(gameboard[0][2]=='X')) && (gameboard[0][2] == gameboard[1][1]) && (gameboard[0][2] == gameboard[2][0])) {
			System.out.println("There is a Winner");
			checkWhoWinner();
			return true;
		}	
		
		
		System.out.println("There is No Winner");
		if(count==9) {
			System.out.print("Game is over.\nDo you want to reinitialize this game? 1= yes, 2= no: ");
			int replay=keyboard.nextInt();
			if(replay==1) {
				reinitialize();
			}
			else {
				System.out.println("Finish the game..");
				System.exit(0);
			}
		}
		
		return false;
	}

	
	public void checkWhoWinner() {
		for(int i=0;i<gameboard.length;i++) {
			if ((gameboard[i][0]=='O')&&(gameboard[i][0] == gameboard[i][1]) &&(gameboard[i][0] == gameboard[i][2])) {
				System.out.println("Player1 'O' is winner!!");
				System.out.print("Game is over.\nDo you want to reinitialize this game? 1= yes, 2= no: ");
				int replay=keyboard.nextInt();
				if(replay==1) {
					reinitialize();
					break;
				}
				else {
					System.out.println("Finish the game..");
					System.exit(0);
				}
			}
			else if ((gameboard[0][i]=='O')&&(gameboard[0][i] == gameboard[1][i]) &&(gameboard[0][i] == gameboard[2][i])) {
				System.out.println("Player1 'O' is winner!!");
				System.out.print("Game is over.\nDo you want to reinitialize this game? 1= yes, 2= no: ");
				int replay=keyboard.nextInt();
				if(replay==1) {
					reinitialize();
					break;
				}
				else {
					System.out.println("Finish the game..");
					System.exit(0);
				}
			}		
			
			else if (((gameboard[0][0]=='O') && (gameboard[0][0] == gameboard[1][1]) && (gameboard[0][0] == gameboard[2][2]))) {
				System.out.println("Player1 'O' is winner!!");
				System.out.print("Game is over.\nDo you want to reinitialize this game? 1= yes, 2= no: ");
				int replay=keyboard.nextInt();
				if(replay==1) {
					reinitialize();
					break;
				}
				else {
					System.out.println("Finish the game..");
					System.exit(0);
				}
			}		
			
			else if (((gameboard[0][2]=='O')) && (gameboard[0][2] == gameboard[1][1]) && (gameboard[0][2] == gameboard[2][0])) {
				System.out.println("Player1 'O' is winner!!");
				System.out.print("Game is over.\nDo you want to reinitialize this game? 1= yes, 2= no: ");
				int replay=keyboard.nextInt();
				if(replay==1) {
					reinitialize();
					break;
				}
				else {
					System.out.println("Finish the game..");
					System.exit(0);
				}
			}		
			
			else {
				System.out.println("Player2 'X' is winner!!");
				System.out.print("Game is over.\nDo you want to reinitialize this game? 1= yes, 2= no: ");
				int replay=keyboard.nextInt();
				if(replay==1) {
					reinitialize();
					break;
				}
				else {
					System.out.println("Finish the game..");
					System.exit(0);
				}
			}	
		}
	}

	
	public void reinitialize() {
		for(int i=0;i<gameboard.length;i++) {
			for(int j=0;j<gameboard[i].length;j++) {
				gameboard[i][j]=' ';
			}
		}

		System.out.println();
		System.out.println("--The game is reset, reinitialize--");
		this.count=0;
	}
}
