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
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.andromeda.ara.constants.User
import com.google.gson.JsonParser
import net.openid.appauth.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*


class LogIn {
    companion object{
    var authState: AuthState? = null
        var service: AuthorizationService? = null
    }


    fun logIn(act: Activity){
        if (!act.getPreferences(0).getBoolean("araAccount", true)) return
        if (act.getSharedPreferences("auth", 0).getString("authData", "") != ""){
            decoded(act.getSharedPreferences("auth", 0).getString("authData", "")!!)
            return
        }
        val mDiscoveryURI = "https://AraLogIn.b2clogin.com/AraLogIn.onmicrosoft.com?p=B2C_1_araMain"
        val issuerUri: Uri = Uri.parse(mDiscoveryURI)

        AuthorizationServiceConfiguration.fetchFromIssuer(issuerUri) { serviceConfiguration, ex ->
            if (ex != null) ex.printStackTrace()
            else startLogIn( serviceConfiguration, act)
        }

    }

    private fun startLogIn( serviceConfiguration: AuthorizationServiceConfiguration?, act: Activity) {
        println("go")
        val req: AuthorizationRequest = AuthorizationRequest.Builder(serviceConfiguration!!, "e4e16983-2565-496c-aa70-8fe0f1bf0907", ResponseTypeValues.CODE, Uri.parse("msale4e16983-2565-496c-aa70-8fe0f1bf0907://auth"))
                .setScope("openid")
                .setPrompt("login")
                .build()
        println(req.toUri())
        authState = AuthState(serviceConfiguration)
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
            if (resp != null) { // auth
                authState!!.update(resp, ex)
                service!!.performTokenRequest(
                        resp.createTokenExchangeRequest()
                ) { resp2, ex2 ->
                    if (resp2 != null) { // exchange succeeded
                        LogIn().decoded(resp2.accessToken!!)
                        getSharedPreferences("auth", 0).edit().putString("authData", resp2.accessToken).apply()
                    } else throw ex2!!
                    // authorization failed, check ex for more details
                }
                Toast.makeText(this, "logged in", Toast.LENGTH_LONG).show()

            } else { //
                Toast.makeText(this, ":( Ara  has run in to a problem an needs to restart" +
                        "error code: ${ex?.code}" +
                        "${ex?.error}", Toast.LENGTH_LONG).show()
                throw ex!!// authorization failed, check ex for more details
            }
            onBackPressed()

    }


    }
    @Throws(java.lang.Exception::class)
    fun decoded(JWTEncoded: String) {
        try {
            val split = JWTEncoded.split(".").toTypedArray()
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]))
            val body = getJson(split[1])
            val jsonParser = JsonParser()
            val id = jsonParser.parse(body).asJsonObject.get("sub").asString
            User.id = id
            val name = jsonParser.parse(body).asJsonObject.get("name").asString
            User.name= name
            com.andromeda.ara.client.util.User.config(name, "", id)
            Log.d("JWT_DECODED", "Body: $body")
        } catch (e: UnsupportedEncodingException) { //Error
        }
    }

    private fun getJson(strEncoded: String): String {
        val decodedBytes: ByteArray = android.util.Base64.decode(strEncoded, android.util.Base64.URL_SAFE)
        return String(decodedBytes, Charset.defaultCharset())
    }
}