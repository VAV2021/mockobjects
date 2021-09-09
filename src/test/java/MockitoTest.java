import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Some tests using Mockito to mock classes in the Java API.
 *
 * @see https://site.mockito.org/javadoc/current/org/mockito/Mockito.html
 */

// @RunWith is one way to enable Mockito to process it's annotations.
// Instead I use MockitoAnnotations.initMocks(this) in the setUp method.
// @RunWith(MockitoJUnitRunner.class)
public class MockitoTest {
	@Mock
	List<String> list;

	/**
	 * Initialize attributes annotated with \@Mock.
 	 * This ensures a new mock is created for each test.
	 */
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test that the test fixture was initialized (mocked),
	 * and that it 'mocks' all list methods with some default values. 
	 *
	 * Mockito will mock all methods.
	 * The default behavior is to return false, 0, or null for object refs.
	 */
	@Test
	public void testInitializedMock() {
        // it really was initialized (mocked)
		assertNotNull(list);

		// default is to return false, 0, or null (for object references)
		assertEquals(0, list.size());
		assertFalse(list.isEmpty());    // actually it _is_ empty :-)
		// the mock returns false by default.
		// a real list would add elements and return true for all of these:
		assertFalse(list.add("foo"));
		assertFalse(list.contains("foo"));
		assertFalse(list.remove("foo"));

		// methods that return an object reference will return null
		assertNull(list.get(0));
		// it doesn't care if the method argument is ridiculous
		assertNull(list.get(1_000_000_000));
		assertNull(list.get(-1));
	}

	/**
	 * Program the mock object to return some "fake" results.
	 * After calling the mocked methods, use verify() to test
	 * that the methods were actually called.
	 */
	@Test
	public void testFakeResults() {

		// programming the mock object
		when(list.get(0)).thenReturn("zero");

		// program to return a different result each time it is called.
		// first time return "one", next return "ichi", then return "neung".
		when(list.get(1))
				.thenReturn("one")
				.thenReturn("ichi")	
				.thenReturn("neung");
		
		assertEquals("zero", list.get(0));
		assertEquals("one",  list.get(1));
		assertEquals("ichi", list.get(1));
		assertEquals("neung",list.get(1));
		// get(0) should always return the same value
		assertEquals("zero", list.get(0));

		// we didn't 'fake' this, so the default value is 0
		assertEquals(0, list.size());
		// we didn't 'fake' this, so the default value is false
		// even though we DID add "one" to the mock list.
		assertFalse(list.contains("one"));

		// ok, let's fake it now...
		when(list.contains("one")).thenReturn(true);
		assertTrue(list.contains("one"));

		// verify which methods were called
		verify(list, times(2)).get(0);
		verify(list, times(3)).get(1);
		verify(list).size();
		// verify a method was called with *any* arguments
		verify(list, times(2)).contains(any());
	}

	/**
	 * Program a mock using conditions on the arguments.
	 * This uses ArgumentMatchers to specify conditions that an
	 * argument to a mocked method should match.
	 *
	 * There are 2 special matchers worth noting:
	 * <pre>
	 *   argThat( custom_matcher )  // can use a lambda expression
	 *   matches( regex )           // match a regular expression
	 * </pre>
	 * The Mockito class extends ArgumentMatcher, so you can access
	 * all the static ArgumentMatchers methods using
	 * <tt>import static org.mockito.Mockito.*;</tt>
	 *
	 * @see https://site.mockito.org/javadoc/current/org/mockito/ArgumentMatchers.html
	 */
	@Test
	public void testProgrammableResults() {
		// any string argument ending with "coffee" has index 1
		when( list.indexOf(endsWith("coffee")) )
				.thenReturn(1);
		// any string argument beginning with "Thai" has index 5
		when( list.indexOf(startsWith("Thai")) )
				.thenReturn(5);

		assertEquals(1, list.indexOf("Espresso coffee"));
		assertEquals(1, list.indexOf("Kenyan coffee"));
		assertEquals(1, list.indexOf("Starbucks coffee"));
		assertEquals(0, list.indexOf("Starbucks"));         // should not match
		assertEquals(0, list.indexOf("Starbucks Coffee"));  // should not match

		assertEquals(5, list.indexOf("Thai food"));
		assertEquals(5, list.indexOf("Thailand"));

		// ambiguous: this argument matches both patterns
		assertEquals(5, list.indexOf("Thai coffee"));
	}

	/**
	 * Program using a lambda expression.
	 * Usage:  mock.methodname(argThat( lambda_expression ))
	 */
	@Test
	public void testProgramWithLambda() {
		// if list.get(n) is called with an odd number, return "odd"
		when(list.get( intThat(n -> n%2 == 1) )).thenReturn("odd");
		// if list.get(n) is called with an even number, return "EVEN"
		when(list.get( intThat(n -> n%2 == 0) )).thenReturn("EVEN");
		// if list.get(n) is called with a negative number, throw IllegalArgumentException
		when(list.get( intThat(n -> n < 0) )).thenThrow(IllegalArgumentException.class);

		assertEquals("odd", list.get(7));
		assertEquals("EVEN", list.get(4));
		assertEquals("EVEN", list.get(0));
		// in a real list, this would throw IndexOutOfBoundsException
		boolean exceptionNotThrown = true;
		try {
			String s = list.get(-1);
		}
		catch(IllegalArgumentException ex) {
			exceptionNotThrown = false;
		}
		if (exceptionNotThrown) fail("list.get(-1) should throw exception");
	}

	/**
	 * Program a mock to throw exception, based on a condition.
	 * You cannot call list.get(n) with n < 0.
	 * It should throw IllegalArgumentException.
	 */
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void testListIndexCannotBeNegative() {

		// use a Lambda expression as an ArgumentMatcher
		when(list.get(intThat(index -> index < 0)))
				.thenThrow(IllegalArgumentException.class);

		assertNotNull(list.get(-1));
	}

	@Test
	public void testFakeGetAnyValue() {

		// instead of "programming" get(n) for specific values,
		// we can program it to accept any value and always do the same thing.
		when(list.get(anyInt())).thenReturn("zero");
		when(list.get(1)).thenReturn("one");

		assertEquals("one", list.get(1));
		assertEquals("zero", list.get(0));
		// can we get the same value again?
		assertEquals("one", list.get(1));
	}

	/**
	 * Create a mock object using Mockito.mock() instead of annotations.
	 * Verify how many times a method was invoked.
	 *
	 * The verify method is overloaded:
	 * 1. verify a method was called at least once:
	 *    verify(mockobject).method(arg) 
	 * 2. verify using a condition on how many times method was called:
	 *    verify(mockobject, condition).method(arg)
	 *    verify(mockobject, never()).method()
	 *    verify(mockobject, times(3)).method()    -- invoked 3 times
	 *    verify(mockobject, atMost(2)).method()   -- at most 2 times
	 */
	@Test
	public void testVerifyMethodCalls() {
		// Create a mock using code:
		List<String> mocklist = Mockito.mock( java.util.List.class );
		// add() method has not been called yet
		verify(mocklist, never()).add(any());
		// ok, let's add some items
		mocklist.add("apple");
		mocklist.add("banana");

		// verify the 'add' method was called twice
		verify(mocklist, times(2)).add(any());

		// verify 'add' invoked with specific arguments
		verify(mocklist).add("apple");
		verify(mocklist).add("banana");
		// should have invoked add("apple") only 1 time
		verify(mocklist, times(1)).add("apple");
		// the code did not call 'remove'
		verify(mocklist, never()).remove(any());
	}

	/**
	 * "verify" something that did NOT occur.
	 * This test should fail.
	 */
	@Test
	public void testVerifyShouldFail() {
		list.add("coffee");
		// Of course you would NEVER add chocolate to a list.
		// this should cause the test to fail.
		verify(list).add("chocolate");
	}
}
