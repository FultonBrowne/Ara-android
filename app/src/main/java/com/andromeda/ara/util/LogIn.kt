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
import android.util.Log
import android.widget.Toast
import com.andromeda.ara.constants.User
import com.google.gson.JsonParser
import com.microsoft.appcenter.auth.Auth
import com.microsoft.appcenter.auth.SignInResult
import com.nimbusds.jwt.JWTParser
import net.minidev.json.JSONArray
import net.openid.appauth.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*


class LogIn {
    companion object{
    var authState: AuthState? = null
        var service: AuthorizationService? = null
    }

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

    fun logIn(act: Activity){
        val mDiscoveryURI = "https://AraLogIn.b2clogin.com/AraLogIn.onmicrosoft.com?p=B2C_1_araMain"
        val issuerUri: Uri = Uri.parse(mDiscoveryURI)

        AuthorizationServiceConfiguration.fetchFromIssuer(issuerUri) { serviceConfiguration, ex ->
            if (ex != null) ex.printStackTrace()
            else startLogIn( serviceConfiguration, act)
        }

    }

    private fun startLogIn( serviceConfiguration: AuthorizationServiceConfiguration?, act: Activity) {
        println("go")
        val config1 = serviceConfiguration
        val req: AuthorizationRequest = AuthorizationRequest.Builder(config1!!, "e4e16983-2565-496c-aa70-8fe0f1bf0907", ResponseTypeValues.CODE, Uri.parse("msale4e16983-2565-496c-aa70-8fe0f1bf0907://auth"))
                .setScope("openid")
                .setPrompt("login")
                .build()
        println(req.toUri())
        authState = AuthState(config1)
        val generator = Random()
        service = AuthorizationService(act)
        val i = PendingIntent.getActivity(act, generator.nextInt(), Intent(act, GetData::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        service!!.performAuthorizationRequest(req, i)
    }

    class GetData : Activity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            println("run")
            super.onCreate(savedInstanceState)
            val resp = AuthorizationResponse.fromIntent(intent)
            val ex = AuthorizationException.fromIntent(intent)
            val redirectUri = intent.data

            if (resp != null) { // auth
                authState!!.update(resp, ex)
                service!!.performTokenRequest(
                        resp.createTokenExchangeRequest()
                ) { resp2, ex2 ->
                    if (resp2 != null) { // exchange succeeded
                        decoded(resp2.accessToken!!)
                    } else throw ex2!!
                    // authorization failed, check ex for more details
                }
                Toast.makeText(this, "logged in", Toast.LENGTH_LONG).show()

            } else { //
                Toast.makeText(this, "fail", Toast.LENGTH_LONG).show()
                throw ex!!// authorization failed, check ex for more details
            }
            onBackPressed()

    }
        @Throws(java.lang.Exception::class)
        fun decoded(JWTEncoded: String) {
            try {
                val split = JWTEncoded.split(".").toTypedArray()
                Log.d("JWT_DECODED", "Header: " + getJson(split[0]))
                val body = getJson(split[1])
                val jsonParser = JsonParser()
                User.id = jsonParser.parse(body).asJsonObject.get("sub").asString
                Log.d("JWT_DECODED", "Body: $body")
            } catch (e: UnsupportedEncodingException) { //Error
            }
        }

        private fun getJson(strEncoded: String): String {
            val decodedBytes: ByteArray = android.util.Base64.decode(strEncoded, android.util.Base64.URL_SAFE)
            return String(decodedBytes, Charset.defaultCharset())
        }
    }

}