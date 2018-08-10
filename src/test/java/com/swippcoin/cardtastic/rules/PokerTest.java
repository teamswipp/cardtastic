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

import com.swippcoin.cardtastic.Action;
import com.swippcoin.cardtastic.Deck;
import com.swippcoin.cardtastic.Hand;
import com.swippcoin.cardtastic.card.Face;
import com.swippcoin.cardtastic.card.Suit;
import com.swippcoin.cardtastic.card.Card;
import com.swippcoin.cardtastic.card.CardNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class PokerTest {
	private Deck deck;
	private Hand hand;

	@Before
	public void before() {
		 deck = new Deck();
		 hand = new Hand();
	}

	@Test
	public void testJacksOrBetterIsFound() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, Face.JACK));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.JACK));
		hand.drawFrom(deck);
		assertTrue(Poker.JACKS_OR_BETTER.isFound(hand));

		hand.setActionAll(Action.THROW);
		assertEquals(Hand.MAX_CARDS, hand.drawFrom(deck).size());
		assertFalse(Poker.JACKS_OR_BETTER.isFound(hand));

		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.DIAMONDS, 10));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertFalse(Poker.JACKS_OR_BETTER.isFound(hand));

		deck.moveToFront(new Card(Suit.HEARTS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, Face.KING));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertFalse(Poker.JACKS_OR_BETTER.isFound(hand));
	}

	@Test
	public void testTwoPairIsFound() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.KING));
		deck.moveToFront(new Card(Suit.SPADES, Face.QUEEN));
		hand.drawFrom(deck);
		assertFalse(Poker.TWO_PAIR.isFound(hand));

		deck.moveToFront(new Card(Suit.SPADES, 9));
		deck.moveToFront(new Card(Suit.DIAMONDS, 9));
		deck.moveToFront(new Card(Suit.DIAMONDS, 7));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.ACE));
		deck.moveToFront(new Card(Suit.SPADES, Face.ACE));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Poker.TWO_PAIR.isFound(hand));
	}

	@Test
	public void testThreeOfAKindIsFound() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.CLUBS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 3));
		hand.drawFrom(deck);
		assertTrue(Poker.THREE_OF_A_KIND.isFound(hand));

		deck.moveToFront(new Card(Suit.SPADES, Face.JACK));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.KING));
		deck.moveToFront(new Card(Suit.CLUBS, Face.JACK));
		deck.moveToFront(new Card(Suit.CLUBS, Face.QUEEN));
		deck.moveToFront(new Card(Suit.CLUBS, Face.ACE));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertFalse(Poker.THREE_OF_A_KIND.isFound(hand));
	}

	@Test
	public void testStraightIsFound() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, Face.ACE));
		deck.moveToFront(new Card(Suit.DIAMONDS, 2));
		deck.moveToFront(new Card(Suit.CLUBS, 3));
		deck.moveToFront(new Card(Suit.HEARTS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 5));
		hand.drawFrom(deck);
		assertTrue(Poker.STRAIGHT.isFound(hand));

		deck.moveToFront(new Card(Suit.DIAMONDS, 9));
		deck.moveToFront(new Card(Suit.DIAMONDS, 7));
		deck.moveToFront(new Card(Suit.CLUBS, 8));
		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.CLUBS, Face.ACE));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertFalse(Poker.STRAIGHT.isFound(hand));

		deck.moveToFront(new Card(Suit.CLUBS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 2));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.HEARTS, 6));
		deck.moveToFront(new Card(Suit.HEARTS, 5));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Poker.STRAIGHT.isFound(hand));
	}

	@Test
	public void testFlushIsFound() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, Face.ACE));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.SPADES, 4));
		deck.moveToFront(new Card(Suit.SPADES, 5));
		hand.drawFrom(deck);
		assertFalse(Poker.FLUSH.isFound(hand));

		deck.moveToFront(new Card(Suit.HEARTS, 2));
		deck.moveToFront(new Card(Suit.HEARTS, 7));
		deck.moveToFront(new Card(Suit.HEARTS, 8));
		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.HEARTS, Face.ACE));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Poker.FLUSH.isFound(hand));
	}

	@Test
	public void testFourOfAKindIsFound() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, 3));
		deck.moveToFront(new Card(Suit.HEARTS, 3));
		deck.moveToFront(new Card(Suit.HEARTS, 4));
		deck.moveToFront(new Card(Suit.CLUBS, 5));
		hand.drawFrom(deck);
		assertFalse(Poker.FOUR_OF_A_KIND.isFound(hand));

		deck.moveToFront(new Card(Suit.SPADES, Face.KING));
		deck.moveToFront(new Card(Suit.DIAMONDS, Face.KING));
		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.HEARTS, Face.KING));
		deck.moveToFront(new Card(Suit.CLUBS, Face.KING));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Poker.FOUR_OF_A_KIND.isFound(hand));
	}

	@Test
	public void testStraightFlushIsFound() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 3));
		deck.moveToFront(new Card(Suit.DIAMONDS, 4));
		deck.moveToFront(new Card(Suit.HEARTS, 5));
		deck.moveToFront(new Card(Suit.HEARTS, 6));
		deck.moveToFront(new Card(Suit.CLUBS, 7));
		hand.drawFrom(deck);
		assertFalse(Poker.STRAIGHT_FLUSH.isFound(hand));

		deck.moveToFront(new Card(Suit.SPADES, 5));
		deck.moveToFront(new Card(Suit.SPADES, 9));
		deck.moveToFront(new Card(Suit.SPADES, 8));
		deck.moveToFront(new Card(Suit.SPADES, 7));
		deck.moveToFront(new Card(Suit.SPADES, 6));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Poker.STRAIGHT_FLUSH.isFound(hand));
	}

	public void testRoyalFlushIsFound() throws CardNotFoundException {
		deck.moveToFront(new Card(Suit.SPADES, 5));
		deck.moveToFront(new Card(Suit.SPADES, 6));
		deck.moveToFront(new Card(Suit.SPADES, 7));
		deck.moveToFront(new Card(Suit.SPADES, 8));
		deck.moveToFront(new Card(Suit.SPADES, 9));
		hand.drawFrom(deck);
		assertFalse(Poker.ROYAL_FLUSH.isFound(hand));

		deck.moveToFront(new Card(Suit.HEARTS, 10));
		deck.moveToFront(new Card(Suit.HEARTS, Face.KING));
		deck.moveToFront(new Card(Suit.HEARTS, Face.ACE));
		deck.moveToFront(new Card(Suit.HEARTS, Face.JACK));
		deck.moveToFront(new Card(Suit.HEARTS, Face.QUEEN));
		hand.setActionAll(Action.THROW);
		hand.drawFrom(deck);
		assertTrue(Poker.ROYAL_FLUSH.isFound(hand));
	}
}