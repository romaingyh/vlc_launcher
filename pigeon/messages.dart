import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(
  PigeonOptions(
    dartOut: 'lib/src/messages.g.dart',
    dartOptions: DartOptions(),
    kotlinOut:
        'android/src/main/kotlin/app/zeniptv/plugins/vlclauncher/VLMessages.g.kt',
    kotlinOptions: KotlinOptions(errorClassName: "VLFlutterError"),
    dartPackageName: 'zen_player',
  ),
)
class LaunchMessage {
  const LaunchMessage({
    required this.url,
    required this.title,
    this.position = 0,
  });

  final String url;
  final String title;
  final int position;
}

class VlcResultMessage {
  const VlcResultMessage({
    required this.position,
    required this.duration,
  });

  final int position;
  final int duration;
}

@HostApi()
abstract class VlcLauncherHostApi {
  bool isVlcInstalled();

  bool launchVideo(LaunchMessage message);
}

@FlutterApi()
abstract class VlcLauncherFlutterApi {
  void onVlcResult(VlcResultMessage message);
}
