plugins {
    application
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("application.Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
