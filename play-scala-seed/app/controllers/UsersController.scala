package controllers

import scala.concurrent.ExecutionContext
import javax.inject.Inject
import javax.inject.Singleton
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import play.api.mvc.Request
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text
import play.api.data.Forms.number
import dao.UsersDao
import models.User

@Singleton
class UsersController @Inject()(dao: UsersDao, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action.async { implicit request =>
    dao.all().map {
      u => Ok(views.html.users(u))
    }
  }

  def create = Action.async { implicit request =>
    val user: User = userForm.bindFromRequest.get
    dao.insert(user).map(_ => Redirect(routes.UsersController.index))
  }

  val userForm = Form(
    mapping(
      "name" -> text,
      "age" -> number
    )((name, age) => User(None, name, age))
    (u => Some((u.name, u.age)))
  )
}
