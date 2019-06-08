package com.andromeda.ara;


import android.service.voice.AlwaysOnHotwordDetector;

import java.util.Locale;

abstract class HotWord {
    public abstract AlwaysOnHotwordDetector createAlwaysOnHotwordDetector (String keyphrase,
                                                                        Locale locale,
                                                                        AlwaysOnHotwordDetector.Callback callback);




}
