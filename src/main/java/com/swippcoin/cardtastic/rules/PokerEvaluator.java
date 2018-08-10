package com.swippcoin.cardtastic.rules;

import com.swippcoin.cardtastic.Hand;

public class PokerEvaluator implements Evaluator<Poker> {
	@Override
	public Poker evaluate(Hand hand) {
		for (Poker p : Poker.values()) {
			if (p.isFound(hand)) {
				return p;
			}
		}

		return Poker.EMPTY_HAND;
	}
}
