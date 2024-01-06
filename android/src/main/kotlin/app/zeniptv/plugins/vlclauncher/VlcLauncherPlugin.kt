package app.zeniptv.plugins.vlclauncher

import VLFlutterError
import LaunchMessage
import VlcResultMessage
import VLC_REQUEST_CODE
import VlcLauncherFlutterApi
import VlcLauncherHostApi
import VlcUtils
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import android.util.Log
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry

/** VlcLauncherPlugin */
class VlcLauncherPlugin : FlutterPlugin, VlcLauncherHostApi, ActivityAware,
  PluginRegistry.ActivityResultListener {

  private val TAG = "VlcLauncherPlugin"

  private var context: Context? = null
  private var flutterApi: VlcLauncherFlutterApi? = null
  private var activity: Activity? = null

  /// Mark: FlutterPlugin related

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    context = binding.applicationContext
    flutterApi = VlcLauncherFlutterApi(binding.binaryMessenger)
    VlcLauncherHostApi.setUp(binding.binaryMessenger, this)
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    context = null
    flutterApi = null
    VlcLauncherHostApi.setUp(binding.binaryMessenger, null)
  }

  /// Mark: Activities related

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    Log.d(TAG, "onAttachedToActivity")
    activity = binding.activity
    binding.addActivityResultListener(this)
  }
  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    Log.d(TAG, "onReattachedToActivityForConfigChanges")
    activity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivity() {
    Log.d(TAG, "onDetachedFromActivity")
    activity = null
  }
  override fun onDetachedFromActivityForConfigChanges() {
      Log.d(TAG, "onDetachedFromActivityForConfigChanges")
      activity = null
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    Log.d(TAG, "onActivityResult, requestCode: $requestCode, resultCode: $resultCode")

    if (requestCode != VLC_REQUEST_CODE || resultCode != Activity.RESULT_OK) {
      return false
    }

    val position = data?.getLongExtra("extra_position", -1) ?: -1
    val duration = data?.getLongExtra("extra_duration", -1) ?: -1
    Log.d(TAG, "VLC activity result: resultCode $resultCode, position $position, duration: $duration")

    flutterApi?.onVlcResult(VlcResultMessage(position = position, duration = duration)) { result ->
      Log.d(TAG, "onVlcResult: $result")
    }

    return true
  }

  /// Mark: VlcLauncherApi implementation

  override fun isVlcInstalled(): Boolean {
    if (context == null) {
      throw VLFlutterError("INVALID_CONTEXT", "Context is null")
    }

    return VlcUtils.isInstalled(context!!)
  }

  override fun launchVideo(message: LaunchMessage): Boolean {
    val uri = Uri.parse(message.url)

    return try {
      VlcUtils.launchVideo(activity!!, uri, message.title, message.position)
      true
    } catch (e: Exception) {
      e.printStackTrace()
      throw VLFlutterError("VLC_LAUNCH_FAILED", "Failed to launch VLC")
    }
  }
}
