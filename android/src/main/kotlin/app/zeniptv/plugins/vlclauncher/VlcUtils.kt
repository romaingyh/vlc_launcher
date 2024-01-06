import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

const val VLC_PACKAGE = "org.videolan.vlc"
val VLC_COMPONENT: ComponentName = ComponentName(VLC_PACKAGE, "org.videolan.vlc.gui.video.VideoPlayerActivity")
const val VLC_REQUEST_CODE = 42

object VlcUtils {
    fun isInstalled(context: Context): Boolean {
        val packageManager = context.packageManager;

        return try {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(VLC_PACKAGE, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun launchVideo(activity: Activity, uri: Uri, title: String, position: Long) {
        val vlcIntent = Intent(Intent.ACTION_VIEW).apply {
            component = VLC_COMPONENT
            setDataAndTypeAndNormalize(uri, "video/*")
            putExtra("title", title)

            if (position > 0) {
                putExtra("from_start", false)
                putExtra("position", position)
            }

            else {
                putExtra("from_start", true)
            }
        }

        activity.startActivityForResult(vlcIntent, VLC_REQUEST_CODE);
    }
}