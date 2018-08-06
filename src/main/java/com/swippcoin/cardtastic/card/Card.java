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
package com.swippcoin.cardtastic.card;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Card implements Comparable<Card> {
	@Getter private final Suit suit;
	@Getter private final int value;

	public Card(Card card) {
		suit = card.suit;
		value = card.value;
	}

	public Card(Suit suit, Face face) {
		this.suit = suit;
		value = face.getValue();
	}

	@Override
	public int compareTo(Card card) {
		int result = value * 10 - card.getValue() * 10;

		if (result == 0) {
			result = result + suit.getValue() - card.suit.getValue();
		}

		return result;
	}
}