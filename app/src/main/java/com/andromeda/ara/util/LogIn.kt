/*
 * Copyright (c) 2020. Fulton Browne
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.andromeda.ara.util

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.andromeda.ara.constants.User
import com.microsoft.appcenter.auth.Auth
import com.microsoft.appcenter.auth.SignInResult
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.exception.MsalException
import com.nimbusds.jwt.JWTParser
import net.minidev.json.JSONArray
import net.openid.appauth.*
import java.util.*


class LogIn {
    var mFirstAccount: IAccount? = null

    fun logIn(mPrefs: SharedPreferences, ctx: Context) {

        Auth.setEnabled(true)
        Auth.signIn().thenAccept { signInResult: SignInResult ->
            if (signInResult.exception == null) { // Sign-in succeeded.
                try {
                    val accountId = signInResult.userInformation.accountId
                    val idToken = signInResult.userInformation.idToken
                    println(accountId)
                    val parsedToken = JWTParser.parse(idToken)
                    val claims = parsedToken.jwtClaimsSet.claims
                    print("check if null")
                    val emails = claims["emails"] as JSONArray?
                    val displayName = claims["given_name"] as String?
                    mPrefs.edit().putString("name", displayName).apply()
                    print(displayName)
                    if (emails != null && !emails.isEmpty()) {
                        User.name =displayName!!
                        User.id = accountId
                        User.email = emails[0].toString()
                        val firstEmail = emails[0].toString()
                        mPrefs.edit().putString("email", firstEmail).apply()
                        print(firstEmail)
                    } else print("emails null")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                signInResult.exception.printStackTrace()

            }
        }
    }

    private fun getAuthInteractiveCallback(): AuthenticationCallback? {
        return object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult) { /* Successfully got a token, use it to call a protected resource */
                val accessToken = authenticationResult.accessToken
                // Record account used to acquire token
                mFirstAccount = authenticationResult.account
                User.id = mFirstAccount!!.id
                println("got token")
            }

            override fun onError(exception: MsalException?) {
                throw exception!!
            }

            override fun onCancel() { /* User canceled the authentication */
            }
        }
    }
    fun logIn(act: Activity){
        val mDiscoveryURI = "https://AraLogIn.b2clogin.com/AraLogIn.onmicrosoft.com?p=B2C_1_AraLogIn"
        val issuerUri: Uri = Uri.parse(mDiscoveryURI)

        AuthorizationServiceConfiguration.fetchFromIssuer(issuerUri) { serviceConfiguration, ex ->
            if (ex != null) ex.printStackTrace()
            else startLogIn( serviceConfiguration, act)
        }

    }

    private fun startLogIn( serviceConfiguration: AuthorizationServiceConfiguration?, act: Activity) {
        println("go")
        val config1 = serviceConfiguration
        val req: AuthorizationRequest = AuthorizationRequest.Builder(config1!!, "e4e16983-2565-496c-aa70-8fe0f1bf0907", ResponseTypeValues.CODE, Uri.parse("msalfbc54802-e5ba-4a5d-9e02-e3a5dcf4922b://auth")).setScope("openid")
                .build()
        println(req.toUri())
        val generator = Random()
        val service = AuthorizationService(act)
        val i = PendingIntent.getActivity(act, generator.nextInt(), Intent(act, GetData::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        service.performAuthorizationRequest(req, i)
    }

    class GetData : Activity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val resp = AuthorizationResponse.fromIntent(intent)
            val ex = AuthorizationException.fromIntent(intent)
            if (resp != null) { // aut
                Toast.makeText(this, "logged in", Toast.LENGTH_LONG).show()
            } else { //
                throw ex!!// authorization failed, check ex for more details
            }

    }}

}