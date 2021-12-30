package nrktkt.default

import scala.language.implicitConversions
import scala.annotation.targetName

def default[A: ValueOf]: A default A = valueOf[A]

type default[A, V <: A] = A with Default.T[V]

object Default {
  type T[V]

  given [A, V <: A: ValueOf]: Conversion[Option[A], A default V] =
    _.getOrDefault

  given [A, V <: A]: Conversion[A, A default V] = _.asInstanceOf[A with T[V]]

}

extension [A](a: Option[A]) {
  @targetName("OptionWithInferredDefault")
  def getOrDefault[V <: A: ValueOf]: A default V = a.getOrElse(valueOf[V])
}

extension [A, V <: A: ValueOf](a: Option[A default V]) {
  @targetName("OptionWithTypedDefault")
  def getOrDefault: A default V =
    a.getOrElse(valueOf[V])
}

@main def main = {
  val d: Int default 1 = 5
  println(d)

  def default[A: ValueOf]: A default A = valueOf[A]

  val dd: Int default 2 = d
  println(dd)
  println(None.asInstanceOf[Option[Int default 6]].getOrDefault)

  def fun(param: (String default "Hello", String default "World")) =
    param._1 + " " + param._2

  println(fun(default, "Bob"))
}
