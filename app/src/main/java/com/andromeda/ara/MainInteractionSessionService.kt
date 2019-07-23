package com.andromeda.ara

import android.os.Build
import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.service.voice.VoiceInteractionSessionService

import androidx.annotation.RequiresApi

@RequiresApi(api = Build.VERSION_CODES.M)
class MainInteractionSessionService : VoiceInteractionSessionService() {
    override fun onNewSession(args: Bundle): VoiceInteractionSession {
        return VoiceUi(this)
    }
}
