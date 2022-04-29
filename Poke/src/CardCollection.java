import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CardCollection implements Iterable<Card> {
	private ArrayList<Card> cardCollection;

	public CardCollection() {
		cardCollection = new ArrayList<>();
	}

	public CardCollection(CardCollection cardColl) {
		this.cardCollection = new ArrayList<Card>();
		this.cardCollection.addAll(cardColl.getCardCollection());
	}

	public void addCardToCollection(Card c) {
		this.cardCollection.add(c);
	}

	public void addCardsToCollection(CardCollection cardsToAdd) {
		for (Card card : cardsToAdd) {
			this.cardCollection.add(card);
		}
	}

	public void removeCardFromCollection(int index) {
		cardCollection.remove(index);
	}

	public void removeCardListFromCollection(ArrayList<Card> cardsToRemove) {
		ArrayList<Card> removeAll = new ArrayList<Card>();
		for (Card handCard : this.cardCollection) {
			for (Card remCard : cardsToRemove) {
				if (compare(remCard, handCard)) {
					removeAll.add(handCard);
					break;
				}
			}
		}
		this.cardCollection.removeAll(removeAll);
	}

	public boolean compare(Card a, Card b) {
		if (a.getCardID() == b.getCardID())
			return true;
		return false;
	}

	public void shuffle() {
		Collections.shuffle(cardCollection);
	}

	public Card pop() {
		return cardCollection.get(cardCollection.size() - 1);
	}

	public ArrayList<Card> pop(int popNum) {
		ArrayList<Card> retList = new ArrayList<Card>();
		for (int i = 1; i <= popNum; i++) {
			retList.add(cardCollection.get(cardCollection.size() - i));
			cardCollection.remove(cardCollection.size() - i);
		}
		return retList;
	}

	public ArrayList<Card> getCardCollection() {
		return cardCollection;
	}

	public void setCardCollection(ArrayList<Card> cardCollection) {
		this.cardCollection = cardCollection;
	}

	@Override
	public Iterator<Card> iterator() {
		return new ArrayListIterator();
	}

	class ArrayListIterator implements Iterator<Card> {
		int current = 0;

		@Override
		public boolean hasNext() {
			if (current < CardCollection.this.cardCollection.size())
				return true;
			else
				return false;
		}

		@Override
		public Card next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return cardCollection.get(current++);
		}

	}

	public void sort() {
		for (int x = 0; x < this.cardCollection.size(); x++) {
			for (int y = 0; y < this.cardCollection.size(); y++) {
				if (this.cardCollection.get(x).getNumber() > this.cardCollection.get(y).getNumber()) {
					Card temp = this.cardCollection.get(x);
					this.cardCollection.get(x).setCard(this.getCardCollection().get(y));
					this.cardCollection.get(y).setCard(temp);
				}
			}
		}

	}
}
