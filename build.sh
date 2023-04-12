export GRAALVM_HOME=/Users/hoangpq/.sdkman/candidates/java/22.0.0.2.r17-grl
export JAVA_HOME=$GRAALVM_HOME
export PATH=$GRAALVM_HOME/bin:$PATH

which java

lein with-profiles "+reflection,+uberjar" do clean, uberjar

native-image \
--language:js -H:Name=noob \
--diagnostics-mode --initialize-at-build-time \
--initialize-at-run-time=sun.awt.dnd.SunDropTargetContextPeer$EventDispatcher \
-H:ReflectionConfigurationFiles=reflection.json \
-H:+ReportExceptionStackTraces \
-Dgraal.LogFile=noob.log \
-jar target/uberjar/clojure-noob-0.1.0-SNAPSHOT-standalone.jar

#
#native-image \
#  --language:ruby \
#  --diagnostics-mode \
#  --initialize-at-build-time \
#  -H:Name=noob \
#  -H:+ReportExceptionStackTraces \
#  -H:+ReportUnsupportedElementsAtRuntime \
#  -J-Dclojure.spec.skip-macros=true \
#  -J-Dclojure.compiler.direct-linking=true \
#  -H:ReflectionConfigurationFiles=reflection.json \
#  -Dgraal.LogFile=noob.log \
#  -H:ServiceLoaderFeatureExcludeServices=javax.sound.sampled.spi.AudioFileReader \
#  -H:ServiceLoaderFeatureExcludeServices=javax.sound.midi.spi.MidiFileReader \
#  -H:ServiceLoaderFeatureExcludeServices=javax.sound.sampled.spi.MixerProvider \
#  -H:ServiceLoaderFeatureExcludeServices=javax.sound.sampled.spi.FormatConversionProvider \
#  -H:ServiceLoaderFeatureExcludeServices=javax.sound.sampled.spi.AudioFileWriter \
#  -H:ServiceLoaderFeatureExcludeServices=javax.sound.midi.spi.MidiDeviceProvider \
#  -H:ServiceLoaderFeatureExcludeServices=javax.sound.midi.spi.SoundbankReader \
#  -H:ServiceLoaderFeatureExcludeServices=javax.sound.midi.spi.MidiFileWriter \
#  -H:ServiceLoaderFeatureExcludeServices=java.net.ContentHandlerFactory \
#  -H:ServiceLoaderFeatureExcludeServices=java.nio.charset.spi.CharsetProvider \
#  -jar target/uberjar/clojure-noob-0.1.0-SNAPSHOT-standalone.jar