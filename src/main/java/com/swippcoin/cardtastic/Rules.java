/*  This file is part of cardtastic.
 *
 *  Copyright (c) 2018 The Swipp developers
 *
 *  Cardtastic is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  Cardtastic is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  You should have received a copy of the GNU General Public License
 *  and GNU Lesser General Public License along with cardtastic.
 *  If not, see <https://www.gnu.org/licenses/>.
 */
package com.swippcoin.cardtastic;

import com.swippcoin.cardtastic.card.Face;
import com.swippcoin.cardtastic.card.Card;
import com.swippcoin.cardtastic.card.Suit;
import java.util.Collections;
import java.util.List;

public class Rules {
	private static boolean isNumCardsOver(Hand hand, int numCards, int minValue) {
		final List<Card> cards = hand.get();

		Collections.sort(cards);
		int previousValue = -1;
		int series = 0;

		for (Card c : cards) {
			if (c.getValue() == previousValue && c.getValue() >= minValue) {
				if (++series == numCards) {
					return true;
				}
			} else {
				previousValue = c.getValue();
				series = 1;
			}
		}

		return false;

	}

	public static boolean isJacksOrBetter(Hand hand) {
		return isNumCardsOver(hand, 2, Face.JACK.getValue());
	}

	public static boolean isTwoPair(Hand hand) {
		final List<Card> cards = hand.get();

		Collections.sort(cards);
		int previousValue = -1;
		int pairs = 0;

		for (Card c : cards) {
			if (c.getValue() == previousValue) {
				pairs++;
				previousValue = -1;
			} else {
				previousValue = c.getValue();
			}
		}

		return pairs == 2;
	}

	public static boolean isThreeOfAKind(Hand hand) {
		return isNumCardsOver(hand, 3, 2);
	}

	public static boolean isStraight(Hand hand) {
		final List<Card> cards = hand.get();
		final Card lastCard = (Card) cards.get(cards.size() - 1);

		Collections.sort(cards);

		/* If the last card is an ace, we have a special case (as it has two values) */
		if (lastCard.getValue() == Face.ACE.getValue()) {
			cards.add(0, new Card(lastCard.getSuit(), 1));
		}

		int previousValue = -1;
		int series = 0;

		for (Card c : cards) {
			if (c.getValue() - 1 == previousValue) {
				if (++series == 5) {
					return true;
				}
			} else {
				series = 1;
			}

			previousValue = c.getValue();
		}

		return false;
	}

	public static boolean isFlush(Hand hand) {
		final List<Card> cards = hand.get();
		Suit wantedSuit = hand.get().get(0).getSuit();
		int series = 0;

		for (Card c : cards) {
			if (c.getSuit() == wantedSuit) {
				series++;
			}
		}

		return series == 5;
	}

	public static boolean isFullHouse(Hand hand) {
		return isTwoPair(hand) && isThreeOfAKind(hand);
	}

	public static boolean isFourOfAKind(Hand hand) {
		return isNumCardsOver(hand, 4, 2);
	}

	public static boolean isStraightFlush(Hand hand) {
		return isStraight(hand) && isFlush(hand);
	}

	public static boolean isRoyalFlush(Hand hand) {
		final List<Card> cards = hand.get();
		final Card lastCard = (Card) cards.get(cards.size() - 1);

		Collections.sort(cards);
		return lastCard.getValue() == Face.ACE.getValue() && isStraightFlush(hand);
	}
}