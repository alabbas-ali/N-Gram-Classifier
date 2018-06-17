#!/usr/bin/env bash

# Additional repos
yum install \
    epel-release.noarch \
-y

# Various tools
yum install \
    wget \
    curl \
    pcre-tools \
    git \
    zlib-devel \
    gcc-c++ \
    mc \
-y