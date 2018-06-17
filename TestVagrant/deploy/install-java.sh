#!/bin/bash

## Latest JDK8 version is JDK8u144 released on 26th July, 2017.
BASE_URL_8=http://download.oracle.com/otn-pub/java/jdk/8u144-b01/090f390dda5b47b9b721c7dfaa008135/jdk-8u144

JDK_VERSION=`echo $BASE_URL_8 | rev | cut -d "/" -f1 | rev`

##declare -a PLATFORMS=(
##	"-linux-arm32-vfp-hflt.tar.gz" 
##	"-linux-arm64-vfp-hflt.tar.gz" 
##	"-linux-i586.rpm" 
##	"-linux-i586.tar.gz" 
##	"-linux-x64.rpm" 
##	"-linux-x64.tar.gz" 
##	"-macosx-x64.dmg" 
##	"-solaris-sparcv9.tar.Z" 
##	"-solaris-sparcv9.tar.gz" 
##	"-solaris-x64.tar.Z" 
##	"-solaris-x64.tar.gz" 
##	"-windows-i586.exe" 
##	"-windows-x64.exe" 
##	"-docs-all.zip"
##)

declare -a PLATFORMS=("-linux-x64.rpm" "-docs-all.zip")

for platform in "${PLATFORMS[@]}"
do
    wget -c --header "Cookie: oraclelicense=accept-securebackup-cookie" "${BASE_URL_8}${platform}"
    ### curl -C - -L -O -# -H "Cookie: oraclelicense=accept-securebackup-cookie" "${BASE_URL_8}${platform}"
done

yum install -y jdk-8u144-linux-x64.rpm
 
rm jdk-8u144-linux-x64.rpm