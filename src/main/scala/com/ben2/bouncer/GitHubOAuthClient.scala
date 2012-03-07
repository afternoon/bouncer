package com.ben2.bouncer

import org.eclipse.egit.github.core.client.GitHubClient
import org.scribe.builder.ServiceBuilder
import org.scribe.model.{Token, Verifier}
import org.scribe.oauth.OAuthService

class GitHubOAuthService(
    val consumerKey: String,
    val consumerSecret: String,
    val authCallback: String) {
  val scribe = new ServiceBuilder()
    .provider(classOf[GitHubApi])
    .apiKey(consumerKey)
    .apiSecret(consumerSecret)
    .callback(authCallback)
    .build

  val requestToken = new Token(consumerKey, consumerSecret)

  def authURL = scribe.getAuthorizationUrl(requestToken)

  def accessToken(verifierCode: String) = {
    val verifier: Verifier = new Verifier(verifierCode);
    scribe.getAccessToken(requestToken, verifier);
  }
}

object GitHubOAuthService {
  def client(accessToken: String) = {
    val client = new GitHubClient()
    client.setOAuth2Token(accessToken)
  }
}
