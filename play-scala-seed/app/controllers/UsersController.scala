package controllers

import scala.concurrent.ExecutionContext
import javax.inject.Inject
import javax.inject.Singleton
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import play.api.mvc.Request
import dao.UsersDao
import models.User

@Singleton
class UsersController @Inject()(dao: UsersDao, cc: ControllerComponents)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action.async { implicit request =>
    dao.all().map {
      u => Ok(views.html.users(u))
    }
  }
}
