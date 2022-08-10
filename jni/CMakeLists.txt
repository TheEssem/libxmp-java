add_subdirectory(xmpsrc)
add_library(interface SHARED "")
target_sources(interface PRIVATE interface.c)
target_link_libraries(interface PRIVATE xmp_static)