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
extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val Users = TableQuery[UsersTable]
  private val Companies = TableQuery[CompaniesTable]

  def all()(implicit ec: ExecutionContext): Future[Seq[User]] = db.run(Users.result)

  def insert(u: User)(implicit ec: ExecutionContext): Future[Unit] = db.run(Users += u).map { _ => () }

  def allWithCompany()(implicit ec: ExecutionContext): Future[Seq[(User, Option[Company])]] = {
    val query = for {
      (user, company) <- Users joinLeft Companies on (_.companyId === _.id)
    } yield (user, company)
    db.run(query.result)
  }

  private class UsersTable(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def age = column[Int]("AGE")
    def companyId = column[Option[Int]]("COMPANY_ID")

    def * = (id.?, name, age, companyId) <> ((User.apply _).tupled, User.unapply)
  }

  private class CompaniesTable(tag: Tag) extends Table[Company](tag, "COMPANIES") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def address = column[String]("ADDRESS")
    def * = (id.?, name, address) <> ((Company.apply _).tupled, Company.unapply)
  }
}
