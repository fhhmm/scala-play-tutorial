package controllers

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
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
import play.api.data.Forms.optional
import dao.UsersDao
import models.User

@Singleton
class UsersController @Inject()(dao: UsersDao, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action.async { implicit request =>
    dao.allWithCompany().map {
      result => Ok(views.html.users(result))
    }
  }

  def create = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest("Form input has errors"))
      },
      userData => {
        UserValidation.validate(userData) match {
          case Right(user) =>
            dao.insert(user).map(_ => Redirect(routes.UsersController.index))
          case Left(errors) =>
            Future.successful(BadRequest(errors.mkString("\n")))
        }
      }
    )
  }

  val userForm = Form(
    mapping(
      "name" -> text,
      "age" -> number,
      "companyId" -> optional(number)
    )((name, age, companyId) => User(None, name, age, companyId))
    (u => Some((u.name, u.age, u.companyId)))
  )
}
