## Test Doubles

In testing, "test doubles" refer to four kinds of substitute objects:

Dummy Objects
- Provide required dependencies that are otherwise irrelevant to the test

Test Stubs
- provide dummy input data sources. This means you can invoke them to get a result.

Fake Objects
- lightweight implementations of heavyweight processes like databases.

Mock Objects
- check indirect results produced by the SUT. 
- "Spy Objects" a wrapper for a real object so we can monitor interactions.
- monitor test outputs.


## Mock Object Frameworks (Libraries) for Java

* [Mockito](https://site.mockito.org) the most popular mock framework. Claims to be one of the top-10 testing frameworks for Java.
* [JMockit](https://jmockit.github.io/) 2nd most popular mock framework. Includes a code-coverage tool. People say "you can mock anything [using EasyMock]", but a slightly longer learning curve than Mockito.
* [JMock](https://jmock.org) the first major mock framework for Java. Has excellent documentation.
* [EasyMock](https://easymock.org)
* [PowerMock](https://github.com/powermock/powermock) extends other mocking frameworks, including Mockito and EasyMock.  PowerMock uses a custom classloader to enable mocking of static methods, constructors, final classes, private methods, and more -- things that some other frameworks can't do.


## Good Articles

* [Best Java Unit Testing Frameworks](https://dzone.com/articles/best-java-unit-testing-frameworks) article on DZone (Sep 2019) gives examples of using JUnit, AssertJ, Mockito, EasyMock, PowerMock, Spring Unit, JSONAssert.
  - 28 minute video for article: <https://www.youtube.com/watch?v=VG7ohV4weYw>

* [Introducing Mock Frameworks](https://freecontent.manning.com/introducing-mock-frameworks/) long article on Manning (book publisher) with examples for Mockito, EasyMock, and JMock.
