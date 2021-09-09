## Using Mockito in Jshell

**This needs elaboration.** When I use Mockito in Jshell it throws exceptions.
Apparently needs some additional "tool provider".


`jshell` is an interactive Java interpretter included in Java 9 and newer.

To try Mockito expressions interactively, download the mockito-core jar file
and include it on the classpath when you start jshell:
```
jshell --class-path /some/path/mockito-core.jar:.
```

you can include **all** jar files in a specified directory using:
```
jshell --class-path /some/path/*:.
```

To use both Mockito and JUnit:
```
jshell --class-path /path/to/mockito/*:/path/to/junit/*:.
jshell> import static org.junit.Assert.*;
jshell> import static org.mockito.Mockito.*;
```


