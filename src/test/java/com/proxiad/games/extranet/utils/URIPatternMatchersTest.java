package com.proxiad.games.extranet.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class URIPatternMatchersTest {

	@Test
	public void test_matches() {
		assertTrue(URIPatternMatchers.matches("/people/abcd", "/people/{id}"));
		assertTrue(URIPatternMatchers.matches("/people/1234", "/people/{id}"));
		assertFalse(URIPatternMatchers.matches("/people/1234/toto", "/people/{id}"));
		assertFalse(URIPatternMatchers.matches("/people/toto/1234", "/people/{id}"));
	}
}