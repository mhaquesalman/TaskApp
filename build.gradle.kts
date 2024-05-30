// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    extra.apply {
        set("compose_version", "1.7.0")
        set("navigation_version", "2.7.5")
        set("datastore_version", "1.0.0")
        set("room_version", "2.4.2")
        set("hilt_version", "2.48")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

