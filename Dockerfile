FROM ghcr.io/graalvm/jdk:java17-22.0.0

RUN curl -O https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein && \
    chmod +x lein && \
    mv lein /usr/bin/lein && \
    lein upgrade

WORKDIR /usr/src/app
COPY . .

RUN gu install native-image

RUN chmod +x build.sh
CMD ["./build.sh"]
