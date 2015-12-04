package com.hotpodata.baconforkotlin.network.model

/**
 * Created by jdrotos on 12/4/15.
 */
class AccessTokenResponse {
    var access_token: String? = null
    var token_type: String? = null
    var expires_in: Long = 0
    var scope: String? = null
}