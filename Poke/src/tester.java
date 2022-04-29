import java.util.ArrayList;
import java.util.Arrays;

public class tester {

	public static void main(String args[]) throws HandledException {
		int resultArray[] = new int[11];
		
//		ArrayList<CardCollection> testCases = new ArrayList<>();
//		CardCollection cards = new CardCollection();
//		testCases.add(cards);
//		testCases.get(0).addCardToCollection(new Card(10));
//		testCases.get(0).addCardToCollection(new Card(11));
//		testCases.get(0).addCardToCollection(new Card(24));
//		testCases.get(0).addCardToCollection(new Card(36));
//		testCases.get(0).addCardToCollection(new Card(42));
//		testCases.get(0).addCardToCollection(new Card(49));
//		testCases.get(0).addCardToCollection(new Card(52));
//		Results result = CardService.determineHandRank(testCases.get(0), true);
//		resultArray[result.getHandRank()] ++;
		
		int i = 0;
		for (i = 0; i < 10000; i++) {
			CardCollection deck = CardService.generateDeck();
			deck.shuffle();

			CardCollection hand = new CardCollection();
			hand.getCardCollection().addAll(deck.pop(7));
			Results result = CardService.determineHandRank(hand, true);
			resultArray[result.getHandRank()] ++;
		}
		
		System.out.println(i + " samples");
		System.out.println();
		System.out.println(Arrays.toString(resultArray));
		
		float chancePercent[] = new float[11];
		float chanceOdds[] = new float[11];
		
		for(int j = 1; j<resultArray.length; j++)
		{	float x = resultArray[j];
			chancePercent[j] = (x/i)*100;
			chanceOdds[j] = (100/chancePercent[j]);
			
		}
		System.out.println();
		System.out.println(Arrays.toString(chancePercent));
		
		System.out.println();
		System.out.println(Arrays.toString(chanceOdds));
	}

}
