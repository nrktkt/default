package nrktkt

import scala.language.implicitConversions

package object default {
  def default[A: ValueOf]: A default A = valueOf[A]

  type default[A, V <: A] = A with Default.T[V]

  object Default {
    type T[V]
    implicit def apply[A, V <: A](a: A): A default V =
      a.asInstanceOf[A with T[V]]

    implicit def fromOption[A, V <: A: ValueOf](opt: Option[A]): A default V =
      opt.getOrDefault
  }

  implicit class OptionAnySyntax[A](val a: Option[A]) extends AnyVal {
    def getOrDefault[V <: A: ValueOf]: A default V = a.getOrElse(valueOf[V])
  }

  implicit class OptionDefaultSyntax[A, V <: A](val a: Option[A default V])
      extends AnyVal {
    def getOrDefault(implicit valueOf: ValueOf[V]): A default V =
      a.getOrElse(valueOf.value)
  }
}
