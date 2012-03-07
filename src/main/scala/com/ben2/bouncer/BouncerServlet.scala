package com.ben2.bouncer

import javax.servlet.http.HttpSession
import org.eclipse.egit.github.core.service.UserService
import org.scalatra._
import org.scribe.model.Token
import scalate.ScalateSupport

class BouncerServlet extends ScalatraServlet with ScalateSupport {
  val consumerKey = "6d53cfd10d66d8d0abdb"
  val consumerSecret = "8df99b3dabd30339fd6d2a71b8960f1a98eb6a71"
  val authCallback = "http://localhost:8080/auth/callback"

  val authService = new GitHubOAuthService(consumerKey, consumerSecret, authCallback)

  get("/") {
    val accessToken = sessionOption map { _("accessToken") }
    if (accessToken.isDefined) {
      redirect("/home")
    }
    else {
      contentType = "text/html"
      jade("/WEB-INF/views/index.jade")
    }
  }

  get("/auth") {
    val authURL = authService.authURL
    redirect(authURL)
  }

  get("/auth/callback") {
    session("accessToken") = authService.accessToken(params("code")).getToken
    redirect("/")
  }

  get("/home") {
    val accessToken = sessionOption map { _("accessToken").asInstanceOf[String] }
    if (!accessToken.isDefined) {
      redirect("/")
    }
    else {
      val client = GitHubOAuthService.client(accessToken.get)
      val users = new UserService(client)
      val user = users.getUser
      contentType = "text/html"
      jade("/WEB-INF/views/home.jade", "accessToken" -> accessToken.get, "user" -> user)
    }
  }
}
