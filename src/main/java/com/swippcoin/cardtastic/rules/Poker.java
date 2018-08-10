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
package com.swippcoin.cardtastic.rules;

import com.swippcoin.cardtastic.Hand;
import com.swippcoin.cardtastic.card.Card;
import com.swippcoin.cardtastic.card.Face;
import com.swippcoin.cardtastic.card.Suit;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Poker implements Payable, Rule {
	ROYAL_FLUSH("Royal flush") {
		@Override
		public int getReward(int bet) {
			if (bet >= 50) {
				return bet * 600;
			}

			return bet * 250;
		}

		@Override
		public boolean isFound(Hand hand) {
			final List<Card> cards = hand.get();
			final Card lastCard = (Card) cards.get(cards.size() - 1);

			Collections.sort(cards);
			return lastCard.getValue() == Face.ACE.getValue() && Poker.STRAIGHT_FLUSH.isFound(hand);
		}
	}, STRAIGHT_FLUSH("Straight flush") {
		@Override
		public int getReward(int bet) {
			return bet * 50;
		}

		@Override
		public boolean isFound(Hand hand) {
			return Poker.STRAIGHT.isFound(hand) && Poker.FLUSH.isFound(hand);
		}
	}, FOUR_OF_A_KIND("4 of a kind") {
		@Override
		public int getReward(int bet) {
			return bet * 25;
		}

		@Override
		public boolean isFound(Hand hand) {
			return isNumCardsOver(hand, 4, 2);
		}
	}, FULL_HOUSE("Full house") {
		@Override
		public int getReward(int bet) {
			return bet * 8;
		}

		@Override
		public boolean isFound(Hand hand) {
			return Poker.TWO_PAIR.isFound(hand) && Poker.THREE_OF_A_KIND.isFound(hand);
		}
	}, FLUSH("Flush") {
		@Override
		public int getReward(int bet) {
			return bet * 6;
		}

		@Override
		public boolean isFound(Hand hand) {
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
	}, STRAIGHT("Straight") {
		@Override
		public int getReward(int bet) {
			return bet * 4;
		}

		@Override
		public boolean isFound(Hand hand) {
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
	}, THREE_OF_A_KIND("3 of a kind") {
		@Override
		public int getReward(int bet) {
			return bet * 3;
		}

		@Override
		public boolean isFound(Hand hand) {
			return isNumCardsOver(hand, 3, 2);
		}
	}, TWO_PAIR("Two pair") {
		@Override
		public int getReward(int bet) {
			return bet * 2;
		}

		@Override
		public boolean isFound(Hand hand) {
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
	}, JACKS_OR_BETTER("Jacks or better") {
		@Override
		public int getReward(int bet) {
			return bet;
		}

		@Override
		public boolean isFound(Hand hand) {
			return isNumCardsOver(hand, 2, Face.JACK.getValue());
		}
	}, EMPTY_HAND("Empty hand") {
		@Override
		public int getReward(int bet) {
			return 0;
		}

		@Override
		public boolean isFound(Hand hand) {
			return true;
		}
	};

	@Getter private final String description;

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
}