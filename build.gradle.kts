plugins {
    id("java")
    id("antlr")
}

group = "n7.mcdalang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    named("main") {
        java {
            srcDir(layout.buildDirectory.dir("/generated-src/antlr/main"))
        }
    }
}

dependencies {
    antlr("org.antlr:antlr4:4.13.2") // ajouter par GPT
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.antlr:antlr4:4.13.2")
    implementation("org.antlr:antlr4-runtime:4.13.2")

    implementation("com.miglayout:miglayout-swing:11.0")

    implementation("com.formdev:flatlaf:3.1.1")

}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.generateGrammarSource {
    arguments.addAll(listOf("-visitor", "-listener"))
    outputDirectory = file(layout.buildDirectory.dir("/generated-src/antlr/main").get().asFile)
}



