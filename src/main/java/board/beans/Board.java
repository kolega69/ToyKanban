package board.beans;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Board {
	
	private Map<Phase, List<Card>> cardsByPhase = new HashMap<>(); 

	public void add(Card card) {
		Phase phase = card.getPhase();
		List<Card> cards = cardsByPhase.get(phase);
		if (cards == null) {
			cards = new LinkedList<>();
			cardsByPhase.put(phase, cards);
		}
		cards.add(card);
	}
	
	public List<Card> getCardsByPhase(Phase phase) {
		return cardsByPhase.get(phase);
	}
	
	public List<Card> getCardsByPhase(String phase) {
		return getCardsByPhase(Phase.valueOf(phase));
	}
	
	public boolean moveToInProgress(Card card) {
		if (!card.getPhase().equals(Phase.planned)) return false;
		getCardsByPhase(Phase.planned).remove(card);
		getCardsByPhase(Phase.inProgress).add(card);
		return true;
	}
	
	public boolean moveToInProgress(String cardName) {
		List<Card> list = getCardsByPhase(Phase.planned);
		for (Card card : list) {
			if (card.getName().equals(cardName)) {
				return moveToInProgress(card);
			}
		}

		return false;
	}
	
	public boolean moveToRejected(Card card) {
		Phase phase = card.getPhase();
		if (phase.equals(Phase.executed) || phase.equals(Phase.rejected)) {
			return false;
		}
		getCardsByPhase(phase).remove(card);
		getCardsByPhase(Phase.rejected).add(card);
		return true;
	}
	
	public Map<Phase, List<Card>> getCardsMap() {
		return cardsByPhase;
	}
}
