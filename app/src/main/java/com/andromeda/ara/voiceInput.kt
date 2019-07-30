package com.andromeda.ara

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.channels.FileChannel.MapMode.READ_ONLY
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel


class voiceInput {


    @Throws(IOException::class)

    private fun loadModelFile(assets: AssetManager, modelFilename: String): MappedByteBuffer {
        val fileDescriptor = assets.openFd(modelFilename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
    fun main(Act: Context){
        var assetManager = Act.assets
        var mp = "conv_actions_frozen"

        val tflite = Interpreter(loadModelFile(assetManager, mp))
        


    }
}
