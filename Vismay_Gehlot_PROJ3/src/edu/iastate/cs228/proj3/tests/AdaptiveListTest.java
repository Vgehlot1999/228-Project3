package edu.iastate.cs228.proj3.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs228.proj3.AdaptiveList;

/**
 * 
 * @author Vismay Gehlot
 *
 */
public class AdaptiveListTest {
	
	private AdaptiveList<String> list;
	private Collection<String> list2;
	
	@Before
	public void init() {
		list = new AdaptiveList<>();
		list2 = Arrays.asList("a", "b", "c");
	}
	
	@Test
	public void SizeTest() {
		 int llength = list.size();
		 assertEquals(0, llength);
	}
	
	@Test
	public void AddTest1() {
		list.add("A");
		assertEquals("(A)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void AddTest2() {
		 list.add("A");
		 assertEquals(1, list.size());
	}

	@Test
	public void AddTest3() {
		list.add("A");
		list.add("B");
		assertEquals("(B, A)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void PositionTest() {
		list.add("A");
		list.add("B");
		list.add(0, "C");
		assertEquals("(C, B, A)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void PositionTest2() {
		list.add("A");
		list.add("B");
		list.add(0, "C");
		list.add(0, "D");
		assertEquals("(D, C, B, A)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void PositionTest3() {
		list.add("A");
		list.add(0, "C");
		list.add(0, "D");
		list.add(1, "B");
		assertEquals("(D, B, C, A)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void AddAll() {
		list.add("A");
		list.add("B");
		list.addAll(list2);
		assertEquals("(B, a, b, c, A)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void RemoveTest() {
		list.add("A");
		list.add("B");
		list.remove(0);
		assertEquals("(A)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void RemoveTest2() {
		list.add("A");
		list.add("B");
		list.remove(0);
		list.remove(0);
		assertEquals("()", list.toStringLinked().substring(55));
	}
	
	@Test
	public void RemoveTest3() {
		list.add("A");
		list.add("B");
		list.add("C");
		list.remove(1);
		assertEquals("(B, A)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void ReverseTest() {
		list.add("A");
		list.add("B");
		list.add("C");
		list.reverse();
		assertEquals("(A, B, C)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void ClearTest() {
		list.add("A");
		list.add("B");
		list.add("C");
		list.clear();
		assertEquals("()", list.toStringLinked().substring(55));
	}
	
	@Test
	public void SetTest() {
		list.add("B");
		list.add("B");
		list.add("C");
		list.set(0, "A");
		assertEquals("(A, C, B)", list.toStringLinked().substring(55));
	}
	
	@Test
	public void ContainsTest() {
		list.add("A");
		list.add("B");
		list.add("C");
		assertTrue(list.contains("A"));
	}
	
	@Test
	public void ContainsTest2() {
		list.add("A");
		list.add("B");
		list.add("C");
		assertFalse(list.contains("D"));
	}

	@Test
	public void IndexTest() {
		list.add("A");
		list.add("B");
		list.add("C");
		assertEquals(2, list.indexOf("A"));
	}
	
}