add_subdirectory(xmpsrc)

if (DEFINED ENV{DIST_DIR})
	set(CMAKE_LIBRARY_OUTPUT_DIRECTORY "$ENV{DIST_DIR}")
	set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_RELEASE "$ENV{DIST_DIR}")
endif()

add_library(xmp-jni SHARED "")
target_sources(xmp-jni PRIVATE interface.c)
target_link_libraries(xmp-jni PRIVATE xmp_static)