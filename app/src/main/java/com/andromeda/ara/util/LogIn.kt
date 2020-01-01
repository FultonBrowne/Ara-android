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

import android.content.Context
import android.content.SharedPreferences
import com.microsoft.appcenter.auth.Auth
import com.microsoft.appcenter.auth.SignInResult
import com.nimbusds.jwt.JWTParser
import net.minidev.json.JSONArray

class LogIn {
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
}