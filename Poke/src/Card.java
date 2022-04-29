
public class Card implements Comparable<Card> {
	/*
	 * 
	 */

	private int number;
	private int suit;
	private int cardID;

	public Card() {
		number = 0;
		suit = 0;
		cardID = 0;
	}

	public Card(int suit, int number) {
		this.suit = suit;
		this.number = number;
		this.cardID = derriveCardID(this.suit, this.number);
	}

	public Card(int i) throws HandledException {
		this.cardID = i;
		Card c = derriveCardFromCardID(i);
		this.number = c.getNumber();
		this.suit = c.getSuit();
	}

	public int derriveCardID(int suit, int number) {
		return (((suit - 1) * 13) + number);
	}

	public int derriveCardID() {
		return (((this.suit - 1) * 13) + this.number);
	}

	public Card derriveCardFromCardID(int ID) throws HandledException { // ensure the card is in the range 1-52
		if (ID < 1 || ID > 52) {
			throw new HandledException("1", "MessageHere");
		}
		int cardSuit;
		if (ID % 13 == 0)
			cardSuit = (ID / 13) ;
		else
			cardSuit = (ID / 13) + 1;

		int cardNumber = ID - (13 * (cardSuit - 1));
		Card retCard = new Card(cardSuit, cardNumber);
		return retCard;

	}

	public void setCard(Card card) {
		this.cardID = card.getCardID();
		this.number = card.getNumber();
		this.suit = this.getSuit();

	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSuit() {
		return suit;
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}

	public int getCardID() {
		return cardID;
	}

	public void setCardID(int cardID) {
		this.cardID = cardID;
	}

	@Override
	public int compareTo(Card o) {
		if (this.number < o.getNumber()) {
			return 1;
		}
		if (this.number < o.getNumber()) {
			return -1;
		}
		return 0;
	}
}
