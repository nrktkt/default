package nrktkt.default

object Example extends App {
  {
    def greet(preamble: String = "Hello", name: String = "World") =
      s"$preamble $name"

    greet()               // Hello World
    greet(name = "Alice") // Hello Alice
  }

  {
    def greet(preamble: String default "Hello", name: String default "World") =
      s"$preamble $name"

    greet(default, default) // Hello World
    greet(default, "Alice") // Hello Alice
  }

  {
    def greet(
        preamble: String default "Hello" = default,
        name: String default "World" = default
    ) = s"$preamble $name"

    greet()               // Hello World
    greet(name = "Alice") // Hello Alice
  }

  {
    def greet(
        nameAndPreamble: (String default "Hello", String default "World")
    ) = nameAndPreamble match {
      case (preamble, name) => s"$preamble $name"
    }

    greet((default, "Alice")) // Hello Alice
  }

  {
    val greet =
      (
          preamble: String default "Hello",
          name: String default "World"
      ) => s"$preamble $name"

    greet(default, "Alice") // Hello Alice
  }

  {
    def greet(preamble: String = "Hello", name: String = "World") =
      s"$preamble $name"

    val greeting: Option[String]      = Some("Aloha")
    val emptyGreeting: Option[String] = None

    greeting.fold(greet())(greet(_))      // Aloha World
    emptyGreeting.fold(greet())(greet(_)) // Hello World
  }

  {
    def greet(
        preamble: String default "Hello",
        name: String default "World" = default
    ) = s"$preamble $name"

    val greeting: Option[String]      = Some("Aloha")
    val emptyGreeting: Option[String] = None

    greet(greeting.getOrDefault)  // Aloha World
    greet(greeting, "Alice")      // Aloha Alice
    greet(emptyGreeting, "Alice") // Hello Alice

    case class Example(foo: Option[String default "bar"])
    val example = Example(None)
    example.foo.getOrDefault // bar
  }
}
