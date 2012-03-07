package com.ben2.bouncer

import org.scribe.builder.api.DefaultApi20
import org.scribe.extractors.{AccessTokenExtractor, TokenExtractor20Impl}
import org.scribe.model.OAuthConfig
import org.scribe.utils.{Preconditions, OAuthEncoder}

class GitHubApi extends DefaultApi20 {
  val authURL = "https://github.com/login/oauth/authorize?client_id=%s&response_type=code&redirect_uri=%s"

  override val getAccessTokenEndpoint = "https://github.com/login/oauth/access_token"

  override def getAuthorizationUrl(config: OAuthConfig): String = {
    Preconditions.checkValidUrl(config.getCallback(), "Invalid callback URL")
    authURL.format(config.getApiKey(), OAuthEncoder.encode(config.getCallback()))
  }

  override def getAccessTokenExtractor: AccessTokenExtractor = new TokenExtractor20Impl()
}

