package com.bingediary.config

import org.jetbrains.amper.plugins.Configurable

@Configurable
interface Settings {
    /**
     * Properties file name (without extension) located in the module root.
     * Defaults to "config".
     */
    val propertiesFileName: String get() = "config"

    /**
     * Extra properties to generate in addition to/overriding the ones read from the properties file.
     */
    val additionalConfig: Map<String, String> get() = emptyMap()

    /**
     * buildType to use with the properties File Name to fetch the configuration file.
     * For example, if the buildType is "debug" and the propertiesFileName is "config", the plugin will look for "config-debug.properties" in the module root.
     */

    val buildType : String get() = ""

}
