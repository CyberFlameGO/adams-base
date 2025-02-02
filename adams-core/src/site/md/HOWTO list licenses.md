HOWTO list licenses
===================

The "net.surguy:maven-displaylicense-plugin" plugin allows the listing of
licenses for modules and all dependencies.

The following command lists all licenses:

```
mvn displaylicense:displaylicense
```

Unique, sorted list of libraries/modules and their licenses:

```
mvn displaylicense:displaylicense | grep ":" | grep -v ":compile" | grep -i "no license\|(" | sed s/"\[INFO\][\ ]*"//g | sort -u
```

Unique, sorted list of libraries/modules and their licenses (excluding ADAMS, WEKA and MOA):

```
mvn displaylicense:displaylicense | grep ":" | grep -v ":compile" | grep -i "no license\|(" | sed s/"\[INFO\][\ ]*"//g | sort -u | grep -v "^nz.ac.waikato.cms"
```


$Revision$
