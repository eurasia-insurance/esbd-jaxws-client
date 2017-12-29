#!/bin/bash

cur=`pwd`

rel="`dirname $0`/../"
basedir=`realpath $rel`

targetdir="$basedir/src/main/resources/META-INF/wsdl/"
mkdir -p $targetdir

cd `dirname $0`

curl --config curl.config \
	--output $targetdir/IICWebService.wsdl https://web.mkb.kz/IICWebService.asmx?wsdl

cd $cur