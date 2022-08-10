add_subdirectory(xmpsrc)
add_library(xmp-jni SHARED "")
target_sources(xmp-jni PRIVATE interface.c)
target_link_libraries(xmp-jni PRIVATE xmp_static)