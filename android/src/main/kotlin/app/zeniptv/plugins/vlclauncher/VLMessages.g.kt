// Autogenerated from Pigeon (v15.0.2), do not edit directly.
// See also: https://pub.dev/packages/pigeon


import android.util.Log
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MessageCodec
import io.flutter.plugin.common.StandardMessageCodec
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

private fun wrapResult(result: Any?): List<Any?> {
  return listOf(result)
}

private fun wrapError(exception: Throwable): List<Any?> {
  if (exception is VLFlutterError) {
    return listOf(
      exception.code,
      exception.message,
      exception.details
    )
  } else {
    return listOf(
      exception.javaClass.simpleName,
      exception.toString(),
      "Cause: " + exception.cause + ", Stacktrace: " + Log.getStackTraceString(exception)
    )
  }
}

private fun createConnectionError(channelName: String): VLFlutterError {
  return VLFlutterError("channel-error",  "Unable to establish connection on channel: '$channelName'.", "")}

/**
 * Error class for passing custom error details to Flutter via a thrown PlatformException.
 * @property code The error code.
 * @property message The error message.
 * @property details The error details. Must be a datatype supported by the api codec.
 */
class VLFlutterError (
  val code: String,
  override val message: String? = null,
  val details: Any? = null
) : Throwable()

/** Generated class from Pigeon that represents data sent in messages. */
data class LaunchMessage (
  val url: String,
  val title: String,
  val position: Long

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): LaunchMessage {
      val url = list[0] as String
      val title = list[1] as String
      val position = list[2].let { if (it is Int) it.toLong() else it as Long }
      return LaunchMessage(url, title, position)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      url,
      title,
      position,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class VlcResultMessage (
  val position: Long,
  val duration: Long

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): VlcResultMessage {
      val position = list[0].let { if (it is Int) it.toLong() else it as Long }
      val duration = list[1].let { if (it is Int) it.toLong() else it as Long }
      return VlcResultMessage(position, duration)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      position,
      duration,
    )
  }
}
@Suppress("UNCHECKED_CAST")
private object VlcLauncherHostApiCodec : StandardMessageCodec() {
  override fun readValueOfType(type: Byte, buffer: ByteBuffer): Any? {
    return when (type) {
      128.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          LaunchMessage.fromList(it)
        }
      }
      else -> super.readValueOfType(type, buffer)
    }
  }
  override fun writeValue(stream: ByteArrayOutputStream, value: Any?)   {
    when (value) {
      is LaunchMessage -> {
        stream.write(128)
        writeValue(stream, value.toList())
      }
      else -> super.writeValue(stream, value)
    }
  }
}

/** Generated interface from Pigeon that represents a handler of messages from Flutter. */
interface VlcLauncherHostApi {
  fun isVlcInstalled(): Boolean
  fun launchVideo(message: LaunchMessage): Boolean

  companion object {
    /** The codec used by VlcLauncherHostApi. */
    val codec: MessageCodec<Any?> by lazy {
      VlcLauncherHostApiCodec
    }
    /** Sets up an instance of `VlcLauncherHostApi` to handle messages through the `binaryMessenger`. */
    @Suppress("UNCHECKED_CAST")
    fun setUp(binaryMessenger: BinaryMessenger, api: VlcLauncherHostApi?) {
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.zen_player.VlcLauncherHostApi.isVlcInstalled", codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            var wrapped: List<Any?>
            try {
              wrapped = listOf<Any?>(api.isVlcInstalled())
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.zen_player.VlcLauncherHostApi.launchVideo", codec)
        if (api != null) {
          channel.setMessageHandler { message, reply ->
            val args = message as List<Any?>
            val messageArg = args[0] as LaunchMessage
            var wrapped: List<Any?>
            try {
              wrapped = listOf<Any?>(api.launchVideo(messageArg))
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
    }
  }
}
@Suppress("UNCHECKED_CAST")
private object VlcLauncherFlutterApiCodec : StandardMessageCodec() {
  override fun readValueOfType(type: Byte, buffer: ByteBuffer): Any? {
    return when (type) {
      128.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          VlcResultMessage.fromList(it)
        }
      }
      else -> super.readValueOfType(type, buffer)
    }
  }
  override fun writeValue(stream: ByteArrayOutputStream, value: Any?)   {
    when (value) {
      is VlcResultMessage -> {
        stream.write(128)
        writeValue(stream, value.toList())
      }
      else -> super.writeValue(stream, value)
    }
  }
}

/** Generated class from Pigeon that represents Flutter messages that can be called from Kotlin. */
@Suppress("UNCHECKED_CAST")
class VlcLauncherFlutterApi(private val binaryMessenger: BinaryMessenger) {
  companion object {
    /** The codec used by VlcLauncherFlutterApi. */
    val codec: MessageCodec<Any?> by lazy {
      VlcLauncherFlutterApiCodec
    }
  }
  fun onVlcResult(messageArg: VlcResultMessage, callback: (Result<Unit>) -> Unit) {
    val channelName = "dev.flutter.pigeon.zen_player.VlcLauncherFlutterApi.onVlcResult"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(messageArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(VLFlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else {
          callback(Result.success(Unit))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
}
