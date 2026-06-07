package com.bingediary.config

import com.squareup.kotlinpoet.*
import org.jetbrains.amper.plugins.*
import java.nio.file.Path
import java.util.Properties
import kotlin.io.path.*

@TaskAction
fun generateSources(
    @Input propertiesFile: Path,
    @Output generatedSourceDir: Path,
    additionalConfig: Map<String, String>,
) {
    generatedSourceDir.toFile().deleteRecursively()

    val properties = mutableMapOf<String, String>()

    if (propertiesFile.isRegularFile()) {
        propertiesFile.bufferedReader().use { reader ->
            val props = Properties()
            props.load(reader)

            for (name in props.stringPropertyNames()) {
                properties[name] = props.getProperty(name)
            }
        }
    }

    properties.putAll(additionalConfig)

    val buildConfigObject = TypeSpec.objectBuilder("BuildConfig")

    properties.forEach { (key, value) ->
        val propertySpec = when {
            value == "true" || value == "false" -> {
                PropertySpec.builder(key, Boolean::class)
                    .addModifiers(KModifier.CONST)
                    .initializer(value)
                    .build()
            }

            value.toIntOrNull() != null -> {
                PropertySpec.builder(key, Int::class)
                    .addModifiers(KModifier.CONST)
                    .initializer(value)
                    .build()
            }

            else -> {
                PropertySpec.builder(key, String::class)
                    .addModifiers(KModifier.CONST)
                    .initializer("%S", value)
                    .build()
            }
        }

        buildConfigObject.addProperty(propertySpec)
    }

    val fileSpec = FileSpec.builder(
        packageName = "com.bingediary.config",
        fileName = "BuildConfig"
    )
        .addType(buildConfigObject.build())
        .build()

    generatedSourceDir.createDirectories()

    fileSpec.writeTo(generatedSourceDir)
}