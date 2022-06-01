# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("nativelib")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        nativelib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        # Note: that the relative path is meant to be relative to the directory
        # where you kept the CMakeLists.txt build script.
        nativelib.cpp Matrix.cpp MyBitmapHelper.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Add the directories where the Cpp header files are to let CMake find
# them during compile time
include_directories(include)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        nativelib
        GLESv3
        assimp

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

#设置生成的so动态库最后输出的路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

# Set the third-party library header files directory.
# =====  ASSIMP  =====
add_library(assimp SHARED IMPORTED)

set_property(TARGET assimp PROPERTY IMPORTED_LOCATION
        ${CMAKE_LIBRARY_OUTPUT_DIRECTORY}/libassimp.so)

target_include_directories(${PROJECT_NAME} PUBLIC
        ${PROJECT_SOURCE_DIR}/vendors/assimp/include)

target_link_libraries(${sample} assimp)

add_custom_command(TARGET ${PROJECT_NAME} POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy
        ${CMAKE_CURRENT_SOURCE_DIR}/vendors/assimp/${ANDROID_ABI}/libassimp.so
        ${CMAKE_LIBRARY_OUTPUT_DIRECTORY})