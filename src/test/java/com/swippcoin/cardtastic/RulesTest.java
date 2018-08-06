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
import com.swippcoin.cardtastic.card.Suit;
import com.swippcoin.cardtastic.card.Card;
import com.swippcoin.cardtastic.card.CardNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class RulesTest {
	private Deck deck;
	private Hand hand;

	@Before
	public void before() {
		 deck = new Deck();
		 hand = new Hand();
	}

	@Test
	public void testIsJacksOrBetter() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, Face.JACK));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.JACK));
		hand.drawFrom(deck);
		assertTrue(Rules.isJacksOrBetter(hand));

		hand.setActionAll(Action.THROW);
		assertEquals(Hand.MAX_CARDS, hand.drawFrom(deck).size());
		assertFalse(Rules.isJacksOrBetter(hand));

		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.DIAMONDS, 10));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertFalse(Rules.isJacksOrBetter(hand));

		deck.moveToFront(new Card(Suit.HEARTS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, Face.KING));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertFalse(Rules.isJacksOrBetter(hand));
	}

	@Test
	public void testIsTwoPair() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.KING));
		deck.moveToFront(new Card(Suit.SPADES, Face.QUEEN));
		hand.drawFrom(deck);
		assertFalse(Rules.isTwoPair(hand));

		deck.moveToFront(new Card(Suit.SPADES, 9));
		deck.moveToFront(new Card(Suit.DIAMONDS, 9));
		deck.moveToFront(new Card(Suit.DIAMONDS, 7));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.ACE));
		deck.moveToFront(new Card(Suit.SPADES, Face.ACE));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Rules.isTwoPair(hand));
	}

	@Test
	public void testIsThreeOfAKind() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.CLUBS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 3));
		hand.drawFrom(deck);
		assertTrue(Rules.isThreeOfAKind(hand));

		deck.moveToFront(new Card(Suit.SPADES, Face.JACK));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.KING));
		deck.moveToFront(new Card(Suit.CLUBS, Face.JACK));
		deck.moveToFront(new Card(Suit.CLUBS, Face.QUEEN));
		deck.moveToFront(new Card(Suit.CLUBS, Face.ACE));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertFalse(Rules.isThreeOfAKind(hand));
	}

	@Test
	public void testIsStraight() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, Face.ACE));
		deck.moveToFront(new Card(Suit.DIAMONDS, 2));
		deck.moveToFront(new Card(Suit.CLUBS, 3));
		deck.moveToFront(new Card(Suit.HEARTS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 5));
		hand.drawFrom(deck);
		assertTrue(Rules.isStraight(hand));

		deck.moveToFront(new Card(Suit.DIAMONDS, 9));
		deck.moveToFront(new Card(Suit.DIAMONDS, 7));
		deck.moveToFront(new Card(Suit.CLUBS, 8));
		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.CLUBS, Face.ACE));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertFalse(Rules.isStraight(hand));

		deck.moveToFront(new Card(Suit.CLUBS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 2));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.HEARTS, 6));
		deck.moveToFront(new Card(Suit.HEARTS, 5));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Rules.isStraight(hand));
	}

	@Test
	public void testIsFlush() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, Face.ACE));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.SPADES, 4));
		deck.moveToFront(new Card(Suit.SPADES, 5));
		hand.drawFrom(deck);
		assertFalse(Rules.isFlush(hand));

		deck.moveToFront(new Card(Suit.HEARTS, 2));
		deck.moveToFront(new Card(Suit.HEARTS, 7));
		deck.moveToFront(new Card(Suit.HEARTS, 8));
		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.HEARTS, Face.ACE));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Rules.isFlush(hand));
	}

	@Test
	public void testIsFourOfAKind() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.HEARTS, 3));
		deck.moveToFront(new Card(Suit.HEARTS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 5));
		hand.drawFrom(deck);
		assertFalse(Rules.isFourOfAKind(hand));

		deck.moveToFront(new Card(Suit.SPADES, Face.KING));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.KING));
		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.HEARTS, Face.KING));
		deck.moveToFront(new Card(Suit.CLUBS, Face.KING));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Rules.isFourOfAKind(hand));
	}

	@Test
	public void testIsStraightFlush() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, 4));
		deck.moveToFront(new Card(Suit.HEARTS, 5));
		deck.moveToFront(new Card(Suit.HEARTS, 6));
		deck.moveToFront(new Card(Suit.CLUBS, 7));
		hand.drawFrom(deck);
		assertFalse(Rules.isStraightFlush(hand));

		deck.moveToFront(new Card(Suit.SPADES, 5));
		deck.moveToFront(new Card(Suit.SPADES, 9));
		deck.moveToFront(new Card(Suit.SPADES, 8));
		deck.moveToFront(new Card(Suit.SPADES, 7));
		deck.moveToFront(new Card(Suit.SPADES, 6));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Rules.isStraightFlush(hand));
	}

	public void testIsRoyalFlush() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 5));
		deck.moveToFront(new Card(Suit.SPADES, 6));
		deck.moveToFront(new Card(Suit.SPADES, 7));
		deck.moveToFront(new Card(Suit.SPADES, 8));
		deck.moveToFront(new Card(Suit.SPADES, 9));
		hand.drawFrom(deck);
		assertFalse(Rules.isRoyalFlush(hand));

		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.HEARTS, Face.KING));
		deck.moveToFront(new Card(Suit.HEARTS, Face.ACE));
		deck.moveToFront(new Card(Suit.HEARTS, Face.JACK));
		deck.moveToFront(new Card(Suit.HEARTS, Face.QUEEN));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Rules.isRoyalFlush(hand));
	}
}