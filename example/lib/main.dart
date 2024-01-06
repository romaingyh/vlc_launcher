import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:vlc_launcher/vlc_launcher.dart';

void main() {
  runApp(const MyApp());
}

final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final VlcLauncher _vlcLauncher = VlcLauncher.instance;

  final TextEditingController _urlController = TextEditingController(
      text:
          "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");

  final TextEditingController _titleController =
      TextEditingController(text: "Big Buck Bunny");

  String _log = "";

  @override
  void initState() {
    super.initState();

    _vlcLauncher.setVlcResultListener((mediaKey, position, duration) {
      setState(() {
        _log = "Media key: $mediaKey Position: $position Duration: $duration";
      });
    });
  }

  @override
  void dispose() {
    _urlController.dispose();
    _titleController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FocusableActionDetector(
      shortcuts: <LogicalKeySet, Intent>{
        LogicalKeySet(LogicalKeyboardKey.select): const ActivateIntent(),
      },
      child: MaterialApp(
        themeMode: ThemeMode.dark,
        darkTheme: ThemeData.dark(),
        home: Scaffold(
          appBar: AppBar(
            title: const Text('Vlc Launcher example'),
          ),
          body: Center(
            child: Form(
              key: _formKey,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  _buildInstallStateText(),
                  TextFormField(
                    controller: _titleController,
                    decoration: const InputDecoration(
                      labelText: 'Enter url',
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Please enter some text';
                      }

                      try {
                        Uri.parse(value);
                      } catch (e) {
                        return 'Please enter a valid url';
                      }

                      return null;
                    },
                  ),
                  TextFormField(
                    controller: _urlController,
                    decoration: const InputDecoration(
                      labelText: 'Enter title',
                    ),
                  ),
                  ElevatedButton(
                    autofocus: true,
                    onPressed: () {
                      if (_formKey.currentState!.validate()) {
                        _vlcLauncher.launchVideo(
                          mediaKey: 'test',
                          url: _urlController.text,
                          title: _titleController.text,
                          position: 0,
                        );
                      }
                    },
                    child: const Text('Launch video in Vlc'),
                  ),
                  Text(_log),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildInstallStateText() {
    return FutureBuilder(
      future: _vlcLauncher.isVlcInstalled(),
      builder: (context, snapshot) {
        return switch (snapshot.connectionState) {
          ConnectionState.done => Text('Vlc installed: ${snapshot.data}'),
          _ when snapshot.hasError => Text('Error: ${snapshot.error}'),
          _ => const CircularProgressIndicator(),
        };
      },
    );
  }
}
