package dao

import scala.concurrent.Future

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile
import models.User
import models.Company

class UsersDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
extends HasDatabaseConfigProvider[JdbcProfile] with Tables {
  import profile.api._

  def all()(implicit ec: ExecutionContext): Future[Seq[User]] = db.run(Users.result)

  def insert(u: User)(implicit ec: ExecutionContext): Future[Unit] = db.run(Users += u).map { _ => () }

  def allWithCompany()(implicit ec: ExecutionContext): Future[Seq[(User, Option[Company])]] = {
    val query = for {
      (user, company) <- Users joinLeft Companies on (_.companyId === _.id)
    } yield (user, company)
    db.run(query.result)
  }
}
