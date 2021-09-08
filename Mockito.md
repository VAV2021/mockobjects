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

If you need a JAR file of mockito-core, download from: <https://search.maven.org/search> and enter "mockito-core" (or whatever artifact you want).

Warning: download sites may modify jars! Do **not** download from sites such as 
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
    List list;
    Recipe recipe;

    @Before
    public void setUp() {
        recipe = Mockito.mock(Recipe.class); // mock a concrete class
        list = Mockito.mock(List.class);     // mock an interface implementation
    }
```

Usage:
```
Mockito.mock(Class<T> classToMock)

returns: ClassToMock object (a mocked subclass)
```

2. `@Mock` annotation plus activation.  It's not enough to simply write `@Mock`.
```java
import org.mockito.Mock;
import static org.mockito.Mockito.*;

public class CoffeeMakerTest {
    @Mock
    List list;
    @Mock
    Recipe recipe;

    @Before
    public void setUp() {
        // inject mock objects for each @Mock attribute
        MockitoAnnotations.initMocks(this);  // deprecated
    }
```

Instead of calling `MockitoAnnotations.initMocks` you can 
add this annotation to the class:

```java
// @RunWith is so Mockito can process it's annotations.
@Runwith(MockitoJUnitRunner.class)
public class CoffeeMakerTest {
    @Mock
    List list;
    @Mock
    Recipe recipe;

    @Before
    public void setUp() {
         // nothing to do
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

`anyInt` is a static method in [ArgumentMatchers][]. It matches any integer value passed as an argument.

[ArgumentMatcher][] static methods:

| Method         | matches       |
|:---------------|:--------------|
| any()          | match anything |
| anyBoolean()   | boolean or non-null Boolean |
| anyDouble()    | double or non-null Double |
| anyCollection() | any non-null Collection    |
| anyCollectionOf(Class\<T\> clazz) | collection of type T |
| anyList(Class\<T\> clazz) | any non-null List    |
| argThat(matcher) | custom ArgumentMatcher    |
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

You usually don't need to `import static org.mockito.ArgumentMatcher.*`

`org.mockito.Mockito` extends `ArgumentMatcher`, so it is enough to write:
```
import static org.mockito.Mockito.*;
```

### ArgumentMatchers can be Lambda expressions

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
// these methods should have been called
verify(mockList).add(0);
verify(mockList).add(1);
verify(mockList).size();
```

Verify method call with a `VerificationMode` 
```java
verify(mockList

`VerificationModes` are:
* `times(int)` - called exactly this many times
* `atLeast(int)` - called at least this many times
* `atMost(int)` - called at least this many times
* `atLeastOnce()` - same as `atLeast(1)`
* `never()`



## Resources

Best page to learn mocking by example in the Mockito Javadoc:

<https://site.mockito.org/javadoc/current/org/mockito/Mockito.html>

Two tutorials with useful content:

- [Mockito Tutorial](https://www.softwaretestinghelp.com/mockito-tutorial/) on SoftwareTestingHelp.com
- [Mock with Mockito](https://javacodehouse.com/blog/mockito-tutorial/) at JavaCodeHouse.


---
[ArgumentMatchers]: https://site.mockito.org/javadoc/current/org/mockito/ArgumentMatchers.html


