export GRAALVM_HOME=/Users/hoangpq/.sdkman/candidates/java/22.0.0.2.r17-grl
export JAVA_HOME=$GRAALVM_HOME
export PATH=$GRAALVM_HOME/bin:$PATH

javac AccessJavaFromJS.java
native-image --language:ruby --initialize-at-build-time --diagnostics-mode \
 -H:ReflectionConfigurationFiles=reflect.json \
 -cp . AccessJavaFromJS
