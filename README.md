# default
Fun with type level defaults.

Scala already provides us with a nice language level capability to provide default values for parameters.

```scala
def greet(preamble: String = "Hello", name: String = "World") =
  s"$preamble $name"

greet()               // Hello World
greet(name = "Alice") // Hello Alice
```

But now we can also express those default values at the type level using the `default` type combined with literal types available in > 2.13. The `default` method can summon instances of default values for positional arguments.

```scala
def greet(preamble: String default "Hello", name: String default "World") =
  s"$preamble $name"

greet(default, default) // Hello World
greet(default, "Alice") // Hello Alice
```

Or we can combine the built in default parameter functionality with a type level default to avoid having to pass default parameters.

```scala
def greet(
    preamble: String default "Hello" = default,
    name: String default "World" = default
) = s"$preamble $name"

greet()               // Hello World
greet(name = "Alice") // Hello Alice
```

Why would we possibly want to do this? Well, the built in default functionality doesn't work everywhere. For example in tuple parameters you would need to provide a default for all elements of the tuple, or none at all. But with type level defaults we can specify a default on each element in the tuple.

```scala
def greet(
    nameAndPreamble: (String default "Hello", String default "World")
) = nameAndPreamble match {
  case (preamble, name) => s"$preamble $name"
}

greet((default, "Alice")) // Hello Alice
```

You can't use default arguments in function parameters either. Works just fine with type level defaults.

```scala
val greet = (
  preamble: String default "Hello", name: String default "World"
) => s"$preamble $name"

greet(default, "Alice") // Hello Alice
```

How about passing `Option` values to optional parameters that aren't an `Option` type? (just writing that sentence sounds funny)

```scala
def greet(preamble: String = "Hello", name: String = "World") =
  s"$preamble $name"

val greeting: Option[String]      = Some("Aloha")
val emptyGreeting: Option[String] = None

greeting.fold(greet())(greet(_))      // Aloha World
emptyGreeting.fold(greet())(greet(_)) // Hello World
greet(greeting.getOrElse("Hello"))    // what happens if you edit the default parameter value but forget to update the value in `getOrElse`?
```

It's a pain, and we can imagine how this would grow out of control with multiple parameters.   
With type level parameters we now have a `getOrDefault` for `Option` values when the target type has a default. We can also simply pass the `Option` value directly.

```scala
def greet(
    preamble: String default "Hello",
    name: String default "World" = default
) = s"$preamble $name"

val greeting: Option[String]      = Some("Aloha")
val emptyGreeting: Option[String] = None

greet(greeting.getOrDefault)  // Aloha World
greet(greeting, "Alice")      // Aloha Alice
greet(emptyGreeting, "Alice") // Hello Alice
```

What if the target type doesn't have a default, but the type inside the `Option` does? `getOrDefault` is also available if an `Option`s inner type has a default.

```scala
case class Example(foo: Option[String default "bar"])
val example = Example(None)
example.foo.getOrDefault // bar
```

Where else is it useful to have a type level default?
Maybe we are parsing a field from a JSON object and we want to provide a default if the field is absent.

```scala
def defaultFromJson[A: FromJson, V <: A : ValueOf]: FromJson[A default V] = 
  (maybeFieldValue: Option[Json]) => maybeFieldValue match {
    case fieldValue: Some[Json]   => FromJson[A](fieldValue)
    case None                     => default
  }
```

## Play with it for yourself

<script src="https://scastie.scala-lang.org/ENbCHGWET5ecoAJwiuucNg.js"></script>