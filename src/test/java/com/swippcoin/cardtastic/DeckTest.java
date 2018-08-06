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
import com.swippcoin.cardtastic.card.CardNotFoundException;
import com.swippcoin.cardtastic.card.Face;
import com.swippcoin.cardtastic.card.Suit;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class DeckTest {
	private Deck deck;
	private Hand hand;

	@Before
	public void before() {
		 deck = new Deck();
		 hand = new Hand();
	}

	@Test
	public void testShuffle() throws CardNotFoundException {
		final Deck secondDeck = new Deck();

		assertEquals(deck, secondDeck);
		secondDeck.shuffle();
		assertNotSame(deck, secondDeck);
	}

	@Test
	public void testPull() throws CardNotFoundException {
		assertEquals(deck.pull(), new Card(Suit.CLUBS, 2));
		assertEquals(deck.pull(), new Card(Suit.CLUBS, 3));
		assertEquals(deck.pull(), new Card(Suit.CLUBS, 4));
	}

	@Test
	public void testPut() throws CardNotFoundException {
		assertEquals(deck.pull(), new Card(Suit.CLUBS, 2));
		assertEquals(deck.pull(), new Card(Suit.CLUBS, 3));
		assertEquals(deck.pull(), new Card(Suit.CLUBS, 4));
	}

	@Test(expected = CardNotFoundException.class)
	public void testMoveToFrontInvalid() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 20));
	}

	@Test
	public void testMoveToFront() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.KING));
		deck.moveToFront(new Card(Suit.CLUBS, 4));
		hand.drawFrom(deck);

		assertTrue(hand.get().contains(new Card(Suit.DIAMONDS, Face.KING)));
		assertTrue(hand.get().contains(new Card(Suit.CLUBS, 4)));
	}
}