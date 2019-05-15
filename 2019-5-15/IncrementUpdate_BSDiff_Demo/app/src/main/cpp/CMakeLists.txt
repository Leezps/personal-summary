cmake_minimum_required(VERSION 3.4.1)   #cmake版本号

# 查找文件系统中指定模式的路径，如
file(GLOB bzip_source ${CMAKE_SOURCE_DIR}/bzip/*.c)

# 添加/设置本地动态库，编译生成动态库
add_library(
        native-lib # 模块名

        # 动态库/分享库
        SHARED

        # 源文件
        native-lib.cpp
        bspatch.c
        ${bzip_source})

# 查找系统库，日志输出log
find_library(
        log-lib

        log)

# 需要链接或者编译的库
target_link_libraries(
        native-lib

        ${log-lib})