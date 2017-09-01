# Life

Life is an embeddable scripting language. Heavily based on Java.

Todo|Status
---|---


It's super easy to use this. See [Test.life](https://github.com/xaanit/Life/blob/master/Test.life) for a sample input.

```java
public class Methods {
 
  public static void main(String[] args) {
    Parser parser = new Parser();
    parser.register(new Methods());
    parser.execute("Test.life", new String[] {"And this prints from args[0]!"});
  }
  
  @LifeExecutable
  public static void log(String str) {
    System.out.println(str);
  }
 
}
```

This will print out `Logging!` to the console (can you guess what else?), based on the file listed above.

# Maven

[![](https://jitpack.io/v/xaanit/life.svg)](https://jitpack.io/#xaanit/life)


Add these to your pom.xml (no ellipses, replace `@VERSION@` with the version you'd like):

```xml
	<repositories>
    ...
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
    ...
	</repositories>
```

```xml
  <dependencies>
    ...
  	<dependency>
  	    <groupId>com.github.xaanit</groupId>
  	    <artifactId>life</artifactId>
	    <version>@VERSION@</version>
  	</dependency>
    ...
  </dependencies>
```

# Gradle

Add these to your build.gradle (replace `@VERSION@` with the version you'd like):
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```gradle
	dependencies {
	        compile 'com.github.xaanit:life:@VERSION@'
	}
```
