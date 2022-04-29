import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CardService {
	public final int[] suits = { '1', '2', '3', '4' };
	public final int[] numbers = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
	public final static int aceCardNum = 14;

	public static CardCollection generateDeck() {
		// deck is 52. Loop through valid suits and numbers and generate a card per
		// Then add to collection and return
		CardCollection deck = new CardCollection();
		try {
			for (int i = 1; i <= 52; i++) {
				deck.addCardToCollection(new Card(i));
			}

		} catch (HandledException e) {
			e.printStackTrace();
		}
		return deck;

	}

	public static Results determineHandRank(CardCollection h, boolean remainingCards) throws HandledException {
		// h Card Collection of hand to determine highest rank from
		// highCard boolean that returns the remainder of the hand and sorted by high
		// card

		// This method takes in a hand and returns the first highest hand, and
		// the remainder of the hand sorted if requested.
		// first we transform our hand into the matrix in which we can quickly search
		// for hands.

		// card number * suit. 0th record is the sum
		// hand is AD,AH,AC,3D,3H
		// sum of aces is [1,0] = 3. Sum of Diamonds(1) is [0,1]=2 etc

		// int[][] matrix = generateMatrix(h,false);
		Results result = new Results();
		Results flushes = new Results();
		Results ThreeOfAKind = new Results();
		Results pair = new Results();
		//Collections.sort(h);
		//h.getCardCollection()
		printMatrix(generateMatrix(h, true));

		// >>> 1 + 2Rank: Royal Flush + Straight Flush.
		// a. Check Flush (store in case we need for flush part later)
		// we check for multiple flushes but if we are playing 5 or 7 card then
		// we can only have one flush as there are not enough remaining cards to make
		// another flush. But logic added in case we want to try with more cards
		// If multiple flushs have multiple straights within we return the first
		// b. Check within Flush(es) if there are any straight. If so set resultFound
		// and
		// return the result.
		// If multiple found return just one.
		flushes = determineFlush(new CardCollection(h), true);// this can include 5,6 or 7 card flushes

		if (flushes != null) {
			result = determineStraight(flushes.getPlayingHand(), false, true);
			if (result != null) {
				// we need to manually call the getNonPlayingHand method as we cannot guarantee
				// that the
				// entire card was passed to the determineStraight method
				result.setNonPlayingHand(getNonPlayingHand(new CardCollection(h), result.getPlayingHand()));
				return result;
			}
		}

		// >>> 3: Four Of a Kind
		// Simply send in the hand to determineCardCount() with 4 as the search

		result = determineCardCount(new CardCollection(h), 4);
		if (result != null)
			return result;

		// >>>4: Full House
		// a. Try the hand to see if there is a three pair. Store results for later
		// b. If there is no three pair continue to >>>5
		// c. If there is a three pair, then remove the three cards and attempt to find
		// a two pair.
		// d. If two pair is also found then we need to combine all cards into a
		// collection and put that forward as return

		ThreeOfAKind = determineCardCount(new CardCollection(h), 3);

		if (ThreeOfAKind != null) {
			CardCollection remainCards = getNonPlayingHand(h, ThreeOfAKind.getPlayingHand());
			pair = new Results();
			pair = determineCardCount(remainCards, 2);

			if (pair != null) {
				CardCollection returnCollection = new CardCollection();

				returnCollection.addCardsToCollection(ThreeOfAKind.getPlayingHand());
				returnCollection.addCardsToCollection(pair.getPlayingHand());

				return new Results(4, returnCollection, null, true);

			}

		}

		// >>>5 Flush.
		// We have already stored a flush of at least 5 cards in flushes.
		// We can simply run that through the determineFlushes method and extract just
		// one result
		if (flushes != null) {
			flushes = determineFlush(flushes.getPlayingHand(), false);
			if (flushes != null) {
				return new Results(5, flushes.getPlayingHand(), null, true);
			}
		}

		// >>>6 Straight
		// determine Straight will work with hand

		result = determineStraight(new CardCollection(h), false, false);
		if (result != null)
			return result;

		// >>>7 Three of a Kind
		// determine a three pair

		result = determineCardCount(new CardCollection(h), 3);
		if (result != null)
			return result;

		// >>>8 Two pair
		// determine two pairs

		pair = determineCardCount(h, 2);
		if (pair != null) {
			CardCollection remainCards = getNonPlayingHand(new CardCollection(h), pair.getPlayingHand());
			Results resultsAfterRemoval = new Results();
			resultsAfterRemoval = determineCardCount(remainCards, 2);

			if (resultsAfterRemoval != null) {
				CardCollection returnCollection = new CardCollection();

				returnCollection.addCardsToCollection(pair.getPlayingHand());
				returnCollection.addCardsToCollection(resultsAfterRemoval.getPlayingHand());

				return new Results(8, returnCollection, getNonPlayingHand(new CardCollection(h), returnCollection), true);
			}

		}

		// >>> 9 Pair

		if (pair != null) {
			result = pair;
			return result;
		}

		// >>> 10 High Card
		// return the result 10, hand is already sorted so we just set the first 5 as
		// the result

		CardCollection playingHand = getPlayingHand(new CardCollection(h));

		return (new Results(10, playingHand, getNonPlayingHand(h, playingHand), true));

	}

	private static CardCollection getPlayingHand(CardCollection cardColl) {
		CardCollection returnCollection = new CardCollection();
		for (int i = 1; i < 6; i++) {
			returnCollection
					.addCardToCollection(cardColl.getCardCollection().get(cardColl.getCardCollection().size() - i));
		}
		return returnCollection;
	}

	private static Results determineCardCount(CardCollection h, int cardCount) throws HandledException {
		CardCollection resultCollection = new CardCollection();

		int[][] matrix = generateMatrix(h, false);

		for (int i = matrix[0].length - 1; i > 1; i--) {
			if (matrix[0][i] == cardCount) {
				for (int j = 1; j < matrix.length; j++) {
					if (matrix[j][i] != 0)
						resultCollection.addCardToCollection(new Card(matrix[j][i]));
				}
				if (cardCount == 4)// Four of a Kind
					return new Results(3, resultCollection, getNonPlayingHand(new CardCollection(h), resultCollection), true);
				else if (cardCount == 3) // Three of a kind
					return new Results(7, resultCollection, getNonPlayingHand(new CardCollection(h), resultCollection), true);
				else if (cardCount == 2) // Pair
					return new Results(9, resultCollection, getNonPlayingHand(new CardCollection(h), resultCollection), true);
			}
		}
		return null;
	}

	private static int[][] generateMatrix(CardCollection h, boolean addAces) {
		int[][] matrix = new int[5][15]; // 14(Ace at begin and end) by 4 matrix for number/suit with sum tracker on 0th
		// record

		for (Card c : h.getCardCollection()) {
			matrix[c.getSuit()][c.getNumber()+1] = c.getCardID();
			matrix[0][c.getNumber()+1]++;
			matrix[c.getSuit()][0]++;
			if (c.getNumber() == 13 && addAces) {
				matrix[c.getSuit()][1] = c.getCardID();
				matrix[0][1]++;
			}
		}
		return matrix;
	}

	private static Results determineStraight(CardCollection h, boolean returnFull, boolean isFlush)
			throws HandledException {
		// straight always consider ace high and low. so we need to consider this in
		// our matrix

		int[][] matrix = generateMatrix(h, true);

		// read through number totals if all cells contain at least one then we have a
		// straight. In case we have 2 we simply choose 1.
		if (returnFull) {
			// TODO any cases where we need to gather anything other than the highest
			// straight?
		} else {

			boolean straightFound = false;
			int highCard = 0;
			for (highCard = matrix[0].length - 1; highCard > 5; highCard--) {
				if (matrix[0][highCard] > 0 && matrix[0][highCard - 1] > 0 && matrix[0][highCard - 2] > 0
						&& matrix[0][highCard - 3] > 0 && matrix[0][highCard - 4] > 0) {
					straightFound = true;
					break;
				}
			}

			if (straightFound) {// its possible there are more than one card number associated to the straight
								// so we just chose the first
				// search through the suits for the appropriate card and select the first one.
				// Add this card to the result set
				CardCollection retCollection = new CardCollection();
				for (int y = highCard; y > (highCard - 5); y--) {
					for (int x = 1; x <= 4; x++) {
						if (matrix[x][y] != 0) {
							Card newCard = new Card(matrix[x][y]);
							retCollection.addCardToCollection(newCard);
							break;
						}
					}
				}
				if (isFlush && highCard == aceCardNum) // Royal Flush -> need to add nonPlayingCards from caller method
					return new Results(1, retCollection, null, true);
				else if (isFlush) // Straight Flush -> need to add nonPlayingCards from caller method
					return new Results(2, retCollection, null, true);
				else // Straight
					return new Results(6, retCollection, getNonPlayingHand(h, retCollection), true);// TODO add
																									// non-playing other
																									// cards to this
			}
		}
		return null;
	}

	private static CardCollection getNonPlayingHand(CardCollection set, CardCollection subsetToRemove) {
		CardCollection cardsToRemove = new CardCollection(subsetToRemove);
		CardCollection returnHand =  new CardCollection(set);
		returnHand.removeCardListFromCollection(cardsToRemove.getCardCollection());
		return returnHand;
	}

	public static Results determineFlush(CardCollection h, boolean returnFull) throws HandledException {
		// suit sector of matrix is [0][]... so we search in 1-4 for any amounts >=5
		int[][] matrix = generateMatrix(h, false);
		int[] flushCol = new int[15];
		boolean flushFound = false;
		for (int i = 1; i <= 4; i++) {
			System.out.println(matrix[i][0]);
			if (matrix[i][0] >= 5) {
				matrix[i][0] = 0;
				flushCol = matrix[i];
				flushFound = true;
				break;
			}
		}
		// we now have all flushes stored in the flushCol with the suit Number in the
		// 0th space.
		// if returnAll is true we just pass back the enitre colunm with the indicator
		// final false. (needed for straight flush calculation)
		// if returnAll is False, then we sort by the highest and return the highest 5
		// cards and prepare the result(needed for flush only calculation)
		if (flushFound) {
			if (returnFull) {
				CardCollection returnHand = new CardCollection();

				for (int i : flushCol) {
					if (i != 0) {
						returnHand.addCardToCollection(new Card(i));
					}
				}
				return new Results(5, returnHand, null, false);// TODO add non-playing other cards to this
			} else {
				//TODO select the 5 highest cards in the suit that is stored in i
				CardCollection returnColl = getPlayingHand(new CardCollection(h));
				return new Results(5,returnColl,getNonPlayingHand(new CardCollection(h), returnColl),true);
			}
		}
		return null;
	}

	public static void printMatrix(int[][] matrix) {
		System.out.println(Arrays.deepToString(matrix).replace("], ", "]\n"));

	}

}
