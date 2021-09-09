## Add Mockito to a Project using Gradle

In `build.gradle` add

```
dependencies {
    testImplementation 'org.mockito:mockito-core:3.+'
}
```

This is for JUnit 5
```
dependencies {
    testImplementation 'org.mockito:mockito-inline:3.+'
    testImplementation 'org.mockito:mockito-junit-jupiter:3.+'
}
```

If you need a JAR file of mockito-core, download it from: <https://search.maven.org/search> and enter "mockito-core" (or whatever artifact you want).

Warning: download sites may modify jars! Do **not** download from these sites
* java2s.com
* findjar.com

Probably trustworthy:
* https://github.com/mockito/mockito/releases - source code
* https://mvnrepository.com - jars for all releases

## Creating Mock objects

There are two ways to create mock objects.

1. `Mockito.mock(class)`

```java
import org.mockito.Mockito;

public class CoffeeMakerTest {
    // test fixture objects
    Recipe recipe;
    List list;

    @Before
    public void setUp() {
        recipe = Mockito.mock(Recipe.class); // mock a concrete class
        list = Mockito.mock(List.class);     // mock using interface 
    }
```

Usage:

    Mockito.mock(Class<T> classToMock)
    returns: ClassToMock object (a mocked subclass)

2. `@Mock` annotation plus injection using `initMocks`.
```java
import org.mockito.Mock;
import static org.mockito.Mockito.*;

public class CoffeeMakerTest {
    @Mock
    Recipe recipe;

    @Before
    public void setUp() {
        // inject mock objects for each @Mock attribute
        MockitoAnnotations.initMocks(this);  // deprecated
    }
```

You can use the MockitoJUnitRunner instead of
calling `MockitoAnnotations.initMocks`:
```java
@Runwith(MockitoJUnitRunner.class)
public class CoffeeMakerTest {
    @Mock
    Recipe recipe;

    @Before
    public void setUp() {
         // nothing to do
    }
```

## Mocking Classes with Type Parameters (Generics)

To mock a class with a type parameter, use whichever of these applies:

```java
     // For mock objects created using annotations
     @Mock
     List<String> mocklist;
     
     // For mocks created using Mockito.mock() use a cast:
     List<String> anotherlist = (List<String>) Mockito.mock(List.class);
}
```

## Mocking Method Calls and Fake Return Value

Use the static `Mockito.when( )` method to "program" a method:

```java
import static org.mockito.Mockito.when;

mockList = Mockito.mock(java.util.List.class);

when(mockList.get(0)).thenReturn("Apple");
when(mockList.get(1)).thenReturn("Banana");

String fruit = mockList.get(0);
assertEqual("Apple", fruit);
// default return value is 0, false, or null
assertEqual(0, mockList.size());
```

Or, program a fake method to accept any value and return same result:

```java
mockList = Mockito.mock(java.util.List.class);

when(mockList.get(anyInt()).thenReturn("any fruit");

String fruit = mockList.get(123);
assertEqual("any fruit", fruit);
```

`anyInt` is a static method in [ArgumentMatchers][ArgumentMatchers]. It matches any integer value passed as an argument.

[ArgumentMatchers][ArgumentMatchers] static methods:

| Method         | matches       |
|:---------------|:--------------|
| any()          | match anything, including null |
| any(Class\<T\>)| match any arg of type T, not null |
| anyBoolean()   | boolean or non-null Boolean |
| anyDouble()    | double or non-null Double |
| anyCollection() | any non-null Collection    |
| anyCollectionOf(Class\<T\> clazz) | collection of type T |
| anyList(Class\<T\> clazz) | any non-null List    |
| argThat(matcher) | custom ArgumentMatcher    |
| intThat(matcher) | custom matcher for int argument |
| doubleThat(matcher\<Double\>) | custom ArgumentMatcher\<Double\>     |
| eq(value)                  | argument equals value                   |
| contains(String substring) | String argument containing substring    |
| endsWith(String suffix)    | String argument ending with suffix      |
| matches(String regex)      | String matches a regular expression     |
| startsWith(String prefix)  | String argument starting with prefix    |

If you use ArgumentMatchers for one argument to a function,
then you must use ArgumentMatchers for **all arguments**.

```java
// ILLEGAL
when(mockList.add(0, anyString()))
// OK: arg matcher for both arguments
when(mockList.add(eq(0), anyString()))
```

`org.mockito.Mockito` extends `ArgumentMatchers`, 
so you can get all the static ArgumentMatchers just by importing Mockito as in:
```
import static org.mockito.Mockito.*;
```

### ArgumentMatchers can be Lambda expressions

[ArgumentMatchers]: https://site.mockito.org/javadoc/current/org/mockito/ArgumentMatchers.html

Allow the price of a recipe to be any positive value,

```java
Recipe recipe = Mockito.mock(Recipe.class);

// Really Stupid API: price argument is a string containing an int value
when(recipe.setPrice(argThat(value -> Integer.parseInt(value) > 0)))
// or
when(recipe.setPrice(stringThat(value -> Integer.parseInt(value) > 0)))
```

### Other Results of `when()`

Throw exception:  `when(recipe.setPrice("0")).thenThrow(CoffeeMakerException.class)`

Call the real method: `when(recipe.setPrice(any(String.class))).thenCallRealMethod()`

Create smarter stub method: `when(recipe.something(anyArg())).thenAnswer(...)`

### Verify Method Calls

Verify a method was called.  If not, an exception is raised.

```java
mockList = mock(java.util.List);
mockList.add("apple");
mockList.add("grape");
mockList.size();  // returns 0
// these methods should have been called exactly once
verify(mockList).add("apple");
verify(mockList).add("grape");
verify(mockList).size();
```

Verify method call with a VerificationMode (2nd parameter)
```java
// add method was invoked twice
verify(mockList, times(2)).add(any());
// size() method was not invoked
verify(mockList, never()).size();
```

**VerificationModes** are:
| verifier      |  meaning                        |
|:--------------|:--------------------------------|
| `times(int)`  |  called exactly this many times |
| `atLeast(int)` | called at least this many times|
| `atMost(int)` | called at least this many times |
| `atLeastOnce()` | same as `atLeast(1)` |
| `never()` | never called |



## Resources

Best page for examples of using Mockito is the Mockito Javadoc:
<https://site.mockito.org/javadoc/current/org/mockito/Mockito.html>

Two tutorials with useful content:

- [Mockito Tutorial](https://www.softwaretestinghelp.com/mockito-tutorial/) on SoftwareTestingHelp.com
- [Mock with Mockito](https://javacodehouse.com/blog/mockito-tutorial/) at JavaCodeHouse.


---


