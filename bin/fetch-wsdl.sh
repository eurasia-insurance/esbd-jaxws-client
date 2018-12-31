#!/bin/bash

basedir="$(cd "$(dirname "$1")/../"; pwd)"
targetdir="$basedir/src/main/resources/META-INF/wsdl/"

mkdir -p $targetdir

curl --config curl.config https://web2.mkb.kz/IICWebService.asmx?wsdl \
	| dos2nix \
	> $targetdir/IICWebService.wsdl
