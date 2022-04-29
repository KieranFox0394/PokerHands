
public class Results {
	
	private int handRank;
	private CardCollection playingHand;
	private CardCollection nonPlayingHand;
	private boolean finalResult;
	public Results(int rank, CardCollection playingHand, CardCollection nonPlayingHand, boolean finalResult) {
		this.handRank = rank;
		this.playingHand = playingHand;
		this.nonPlayingHand = nonPlayingHand;
		this.finalResult = finalResult;
	}
	public Results() {
		
	}
	public int getHandRank() {
		return handRank;
	}
	public void setHandRank(int handRank) {
		this.handRank = handRank;
	}
	public CardCollection getPlayingHand() {
		return playingHand;
	}
	public void setPlayingHand(CardCollection playingHand) {
		this.playingHand = playingHand;
	}
	public CardCollection getNonPlayingHand() {
		return nonPlayingHand;
	}
	public void setNonPlayingHand(CardCollection nonPlayingHand) {
		this.nonPlayingHand = nonPlayingHand;
	}
	public boolean isFinalResult() {
		return finalResult;
	}
	public void setFinalResult(boolean finalResult) {
		this.finalResult = finalResult;
	}
	
	
}


