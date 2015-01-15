package board.beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Board {
	
	private Map<Phase, List<Card>> cardsByPhase = new HashMap<>(); 

	public boolean add(Card card) {
		Phase phase = card.getPhase();
		List<Card> cards = getCardsByPhase(phase);
		return cards.add(card);
	}
	
	public List<Card> getCardsByPhase(Phase phase) {
		List<Card> cards = cardsByPhase.get(phase);
		if (cards == null) {
			cards = new LinkedList<>();
			cardsByPhase.put(phase, cards);
		}
		return cards;
	}
	
	public List<Card> getCardsByPhase(String phase) {
		return getCardsByPhase(Phase.valueOf(phase));
	}
	
	public boolean moveToInProgress(Card card) {
		if (!card.getPhase().equals(Phase.planned)) return false;
		getCardsByPhase(Phase.planned).remove(card);
		card.setPhase(Phase.inProgress);
		return add(card);
	}
	
	public boolean moveToInProgress(String cardId) {
		Card card = getCard(cardId, Phase.planned);
		if (card == null) return false;
		return moveToInProgress(card);
	}
	
	public boolean moveToRejected(Card card) {
		if (card == null) return false;
		Phase phase = card.getPhase();
		if (phase.equals(Phase.executed) || phase.equals(Phase.rejected)) {
			return false;
		}
		getCardsByPhase(phase).remove(card);
		card.setPhase(Phase.rejected);
		return add(card);
	}
	
	public boolean moveToRejected(String cardId) {
		Phase[] phases = { Phase.planned, Phase.inProgress };
		Card card = getCard(cardId, phases);
		return moveToRejected(card);
	}
	
	public boolean moveToExecuted(Card card) {
		Phase phase = card.getPhase();
		if (!phase.equals(Phase.inProgress)) return false;
		getCardsByPhase(phase).remove(card);
		card.setPhase(Phase.executed);
		return add(card);
	}
	
	public boolean moveToExecuted(String cardId) {
		Card card = getCard(cardId, Phase.inProgress);
		if (card == null) return false;
		return moveToExecuted(card);
	}
	
	public Card getCard(String cardId, Phase phase) {
		List<Card> cards = getCardsByPhase(phase);
		for (Card card : cards) {
			if(card.getId().equals(cardId)) {
				return card;
			}
		}
		return null;
	}
	
	public Card getCard(String cardId, Phase[] phases) {
		Card card = null;
		for(Phase phase: phases) {
			card = getCard(cardId, phase);
			if (card != null) break;
		}
		return card;
	}
	
	public boolean changeCardData(String cardId, String newCardName, String newDescription, Phase phase) {
		Card card = getCard(cardId, phase);
		if (card == null) return false;
		card.setName(newCardName);
		card.setDescription(newDescription);
		return true;
	}
	
	public boolean removeCard(String cardId) {
		Phase[] phases = { Phase.executed, Phase.rejected };
		for (Phase phase: phases) {
			List<Card> list = getCardsByPhase(phase);
			Iterator<Card> it = list.iterator(); 
			while(it.hasNext()) {
				Card card = it.next();
				if (card.getId().equals(cardId)) {
					it.remove();
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean changeCardPriority(String cardId, String priority) {
		Phase[] phases = { Phase.planned, Phase.inProgress };
		Card card = getCard(cardId, phases);
		if (card == null) return false;
		card.setPriority(priority);
		return true;
	}
	
	
	public Map<Phase, List<Card>> getCardsMap() {
		return cardsByPhase;
	}
}
