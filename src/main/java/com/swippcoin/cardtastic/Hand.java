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

import com.swippcoin.cardtastic.card.Card;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.list.FixedSizeList;
import static org.apache.commons.collections4.list.FixedSizeList.*;
import org.apache.commons.lang3.tuple.Pair;

public class Hand {
	public final static int MAX_CARDS = 5;

	private final FixedSizeList<Pair<Action, Card>> cards = fixedSizeList(Arrays.asList(new Pair[MAX_CARDS]));

	public boolean isEmpty() {
		return cards.get(0) == null;
	}

	public void setAction(Action action, Card card) {
		final int i = cards.indexOf(card);
		cards.set(i, Pair.of(action, card));
	}

	public void setAction(Action action, int index) {
		final Card c = cards.get(index).getRight();
		cards.set(index, Pair.of(action, c));
	}

	public void setActionAll(Action action) {
		for (int i = 0; i < cards.size(); i++) {
			final Card c = cards.get(i).getRight();
			cards.set(i, Pair.of(action, c));
		}
	}

	public List<Card> drawFrom(Deck deck) {
		final List<Card> thrown = new ArrayList<>();

		for (int i = 0; i < MAX_CARDS; i++) {
			final Pair<Action, Card> ac = cards.get(i);

			if (ac == null) {
				cards.set(i, Pair.of(Action.HOLD, deck.pull()));
			} else if (ac.getLeft() == Action.THROW) {
				thrown.add(ac.getRight());
				cards.set(i, Pair.of(Action.THROW, deck.pull()));
			}
		}

		return thrown;
	}

	public List<Card> get() {
		return cards.stream().map(x -> x.getRight()).collect(Collectors.toList());
	}
}