


cd `dirname $0`

rm -f WebContent/WEB-INF/lib/*

mvn dependency:copy-dependencies -DoutputDirectory=WebContent/WEB-INF/lib
