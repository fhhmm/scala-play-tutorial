package controllers

import javax.inject.Inject
import javax.inject.Singleton
import play.api.mvc.AbstractController
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.ControllerComponents
import play.api.mvc.Request
import scala.concurrent.Future

@Singleton
class UsersController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action { implicit request: Request[AnyContent] =>
    Ok("OK!!")
  }
}
