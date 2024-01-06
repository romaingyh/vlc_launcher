import 'package:vlc_launcher/src/messages.g.dart';

typedef VlcResultListener = void Function(
    String mediaKey, int position, int duration);

class VlcLauncher implements VlcLauncherFlutterApi {
  static VlcLauncher? _instance;

  static VlcLauncher get instance {
    _instance ??= VlcLauncher._();
    return _instance!;
  }

  VlcLauncher._() {
    VlcLauncherFlutterApi.setup(this);
  }

  final VlcLauncherHostApi _api = VlcLauncherHostApi();

  String? _lastMediaKey;
  VlcResultListener? _vlcResultListener;

  void setVlcResultListener(VlcResultListener listener) {
    _vlcResultListener = listener;
  }

  Future<bool> isVlcInstalled() {
    return _api.isVlcInstalled();
  }

  Future<bool> launchVideo({
    required String mediaKey,
    required String url,
    required String title,
    int position = 0,
  }) {
    _lastMediaKey = mediaKey;

    final message = LaunchMessage(
      url: url,
      title: title,
      position: position,
    );

    return _api.launchVideo(message);
  }

  @override
  void onVlcResult(VlcResultMessage message) {
    _vlcResultListener?.call(
        _lastMediaKey!, message.position, message.duration);
  }
}
