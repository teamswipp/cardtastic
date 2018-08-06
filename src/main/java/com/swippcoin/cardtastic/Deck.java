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

import com.swippcoin.cardtastic.card.Suit;
import com.swippcoin.cardtastic.card.Card;
import com.swippcoin.cardtastic.card.CardNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collections;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Deck {
	private final LinkedList<Card> cards = new LinkedList();

	public Deck() {
		for (Suit suit : Suit.values()) {
			for (int i = 2; i <= 14; i++) {
				cards.add(new Card(suit, i));
			}
		}
	}

	public Deck(Card ...cards) {
		this.cards.addAll(Arrays.asList(cards));
	}

	public void shuffle() {
		do {
			/* Reshuffle if needed. Even if the probability of getting new deck order is 52!,
			   it can technically happen ;) */
			Collections.shuffle(cards);
		} while (equals(new Deck()));
	}

	public Card pull() {
		return cards.removeFirst();
	}

	public void put(Card card) {
		cards.add(card);
	}

	public void moveToFront(Card card) throws CardNotFoundException {
		if (cards.remove(card)) {
			cards.addFirst(card);
		} else {
			throw new CardNotFoundException(card);
		}
	}

	public int size() {
		return cards.size();
	}
}