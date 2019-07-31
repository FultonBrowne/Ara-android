package com.andromeda.ara

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.channels.FileChannel.MapMode.READ_ONLY
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
//import androidx.test.espresso.core.internal.deps.guava.collect.Iterables.getFirst
import java.util.*
import kotlin.collections.ArrayList


class voiceInput(list: List<String>, averageWindowDurationMs: Long, detectionThreshold: Float, suppressionMs: Int, minimumCount: Int, minimumTimeBetweenSamplesMs: Long) {

    private var averageWindowDurationMs: Long = 0
    private var detectionThreshold: Float = 0.toFloat()
    private var suppressionMs: Int = 0
    private var minimumCount: Int = 0
    private var minimumTimeBetweenSamplesMs: Long = 0
    var labels: List<String>? = null

    // Working variables.
    private var previousResults = ArrayDeque<Pair<Long, FloatArray>>()
    private var previousTopLabel: String = "null"
    private var labelsCount: Int = 0
    private var previousTopLabelTime: Long = 0
    private var previousTopLabelScore: Float = 0.toFloat()

    private var SILENCE_LABEL = "_silence_"
    private var MINIMUM_TIME_FRACTION: Long = 4


    @Throws(IOException::class)

    protected fun loadModelFile(assets: AssetManager, modelFilename: String, ctx: Context): MappedByteBuffer {
        val fileDescriptor = assets.openFd(modelFilename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
    fun main(Act: Context, am: AssetManager){
        var assetManager = am
        val iss1 = am.open("conv_actions_frozen.tflite")

        Toast.makeText(Act,am.list("tf").toString(), Toast.LENGTH_LONG).show()



    }
       fun voiceInput(
inLabels:List<String>,
inAverageWindowDurationMs:Long,
inDetectionThreshold:Float,
inSuppressionMS:Int,
inMinimumCount:Int,
inMinimumTimeBetweenSamplesMS:Long) {

averageWindowDurationMs = inAverageWindowDurationMs
detectionThreshold = inDetectionThreshold
suppressionMs = inSuppressionMS
minimumCount = inMinimumCount
labelsCount = inLabels.size
previousTopLabel = SILENCE_LABEL
previousTopLabelTime = java.lang.Long.MIN_VALUE
previousTopLabelScore = 0.0f
minimumTimeBetweenSamplesMs = inMinimumTimeBetweenSamplesMS
}

  /** Holds information about what's been recognized.  */


  private class ScoreForSorting( val score:Float,  val index:Int):Comparable<ScoreForSorting> {

override fun compareTo(other:ScoreForSorting):Int {
if (this.score > other.score)
{
return -1
}
else return if (this.score < other.score) {
    1
} else {
    0
}
}
}

   fun processLatestResults(currentResults:FloatArray, currentTimeMS:Long): RecognitionResult? {
if (currentResults.size != labelsCount)
{
throw RuntimeException(
        "The results for recognition should contain "
        + labelsCount
        + " elements, but there are "
        + currentResults.size)
}

if ((!previousResults.isEmpty()) && (currentTimeMS < previousResults.getFirst().first))
{
throw RuntimeException(
("You must feed results in increasing time order, but received a timestamp of "
+ currentTimeMS
+ " that was earlier than the previous one of "
+ previousResults.first.first))
}

       var howManyResults = 0
       howManyResults = previousResults.size

 // Ignore any results that are coming in too frequently.
    if (howManyResults > 1)
{
val timeSinceMostRecent = currentTimeMS - previousResults.last.first
if (timeSinceMostRecent < minimumTimeBetweenSamplesMs)
{
return RecognitionResult(previousTopLabel, previousTopLabelScore, false)
}
}

 // Add the latest results to the head of the queue.
    previousResults.addLast(Pair<Long, FloatArray>(currentTimeMS, currentResults))

 // Prune any earlier results that are too old for the averaging window.
    val timeLimit = currentTimeMS - averageWindowDurationMs
while (previousResults.getFirst().first < timeLimit)
{
previousResults.removeFirst()
}

 // If there are too few results, assume the result will be unreliable and
    // bail.
    val earliestTime = previousResults.getFirst().first
val samplesDuration = currentTimeMS - earliestTime
if (((howManyResults < minimumCount) || (samplesDuration < (averageWindowDurationMs / MINIMUM_TIME_FRACTION))))
{
Log.v("RecognizeResult", "Too few results")
return RecognitionResult(previousTopLabel, 0.0f, false)
}

 // Calculate the average score across all the results in the window.
    val averageScores = FloatArray(labelsCount)
for (previousResult in previousResults)
{
val scoresTensor = previousResult.second
var i = 0
while (i < scoresTensor.size)
{
averageScores[i] += scoresTensor[i] / howManyResults
++i
}
}

 // Sort the averaged results in descending score order.
    val sortedAverageScores = arrayOfNulls<ScoreForSorting>(labelsCount)
for (i in 0 until labelsCount)
{
sortedAverageScores[i] = ScoreForSorting(averageScores[i], i)
}
Arrays.sort(sortedAverageScores)

 // See if the latest top score is enough to trigger a detection.
    val currentTopIndex = sortedAverageScores[0]!!.index

       val currentTopLabel = labels?.get(currentTopIndex)
val currentTopScore = sortedAverageScores[0]?.score
 // If we've recently had another label trigger, assume one that occurs too
    // soon afterwards is a bad result.
    val timeSinceLastTop:Long
if (previousTopLabel.equals(SILENCE_LABEL) || (previousTopLabelTime === java.lang.Long.MIN_VALUE))
{
timeSinceLastTop = java.lang.Long.MAX_VALUE
}
else
{
timeSinceLastTop = currentTimeMS - previousTopLabelTime
}
val isNewCommand:Boolean
if ((currentTopScore!! > detectionThreshold) && (timeSinceLastTop > suppressionMs))
{
    if (currentTopLabel != null) {
        previousTopLabel = currentTopLabel
    }
previousTopLabelTime = currentTimeMS
    if (currentTopScore != null) {
        previousTopLabelScore = currentTopScore
    }
isNewCommand = true
}
else
{
isNewCommand = false
}
return currentTopLabel?.let { RecognitionResult(it, currentTopScore, isNewCommand) }
}
    class RecognitionResult(val foundCommand: String, val score: Float, val isNewCommand: Boolean)
}
