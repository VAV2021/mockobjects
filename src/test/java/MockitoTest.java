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


// @RunWith is so Mockito can process it's annotations.
// Instead I use MockitoAnnotations.initMocks
// @RunWith(MockitoJUnitRunner.class)
public class MockitoTest {
	@Mock
	List<String> list;

	@Before
	public void setUp() {
		// initialize annotated @Mock objects before each test.
		// this method is now deprecated
		MockitoAnnotations.initMocks(this);
	}
	/**
	 * Initialize a mock object using annotations.
	 * This test uses a Mock list (above).
	 */
	@Test
	public void testFakeGet() {
        // it really was mocked
		assertNotNull(list);
		
		when(list.get(0)).thenReturn("zero");
		when(list.get(1)).thenReturn("one");
		
		assertEquals("one", list.get(1));
		assertEquals("zero", list.get(0));
		// can we get the same value again?
		assertEquals("one", list.get(1));
		// we didn't program this, so the default value is 0
		assertEquals(0, list.size());
		// we didn't program this, so the default value is false
		assertFalse(list.contains("one"));

		// verify that methods were called (any number of times)
		verify(list).get(0);
		verify(list).size();
	}

	/**
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
	 * Verify how many times a method was invoked
	 */
	public void testVerifyMethodCalls() {
		// you would probably do this in a setUp method
		// Create a mock using code:
		List<String> list2 = Mockito.mock( java.util.List.class );
	}
}