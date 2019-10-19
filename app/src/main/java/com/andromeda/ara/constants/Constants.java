/*
 * Copyright (c) 2019. Fulton Browne
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

package com.andromeda.ara.constants;

import android.media.AudioFormat;
import android.media.MediaRecorder;

public class Constants {
    public static final int REQUEST_RECORD_AUDIO = 13;

    public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    public static final int SAMPLE_RATE_HZ = 16000;
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    public static final String CHUNK_ID = "RIFF";
    public static final int CHUNK_SIZE = 36;
    public static final String FORMAT = "WAVE";
    public static final String SUB_CHUNK_ID_1 = "fmt ";
    public static final int SUB_CHUNK__SIZE_1 = 16;
    public static final String SUB_CHUNK_ID_2 = "data";
    public static final int BYTE_RATE = 44100 * 2;
    public static final short AUDIO_FORMAT_PCM = 1;
    public static final short NUMBER_OF_CHANNELS = 1;
    public static final short BLOCK_ALIGN = 2;
    public static final short BITS_PER_SAMPLE = 16;
}
