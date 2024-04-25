package controllers

import models.User

object UserValidation {
  def validate(input: User): Either[Seq[String], User] = {
    val errors: Seq[String] = Seq(
      if (!nameStartWithUppercase(input.name)) Some("Name must start with uppercase.") else None,
      if (!nameLessThan256Chars(input.name)) Some("Name must be 256 characters or fewer.") else None
    ).flatten

    if (errors.isEmpty) Right(input)
    else Left(errors)
  }

  def nameStartWithUppercase(name: String): Boolean = {
    name.headOption.exists(_.isUpper)
  }

  def nameLessThan256Chars(name: String): Boolean = {
    name.length <= 256
  }
}
