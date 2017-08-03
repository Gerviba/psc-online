PSeudoCode-Online Console
===

## Mi ez?

Ez egy online felület egy másik projektemhez (PSeudoCode-Compiler), ami egy mondatszerű leírás (aka. pseudo code) fordító és futási környezet.
Mivel (mobil kompatibilis) web felületről van szó, így a mondatszerű leírás fordítására lehetőség van iOS, Android és Windows Phone rendszerekről is. 

## Élő demó

Próbáld ki az élő demót [itt](http://psc.gerviba.hu/).

![DEMO](https://raw.githubusercontent.com/Gerviba/psc-online/master/screenshot.png)

## Tesztelve

 - Apache Tomcat 8.5 (Linux)
 - Web felület:
   - Opera, Firefox, Chromium (Linuxon)
   - Safari, Chrome és Firefox (iOS)
 - Build:
   - Eclipse Oxygen (4.7.0 M7), Debian8, Maven
   
## Telepítés

 > Legyen telepítve a céleszközön a PSeudoCompiler valamelyik verziója.
 
#### WAR letöltése

Töltsd le a legújabb Web application ARchive (`psc-online.war`) fájlt, majd másold az Apache Tomcat szerver `webapps` könyvtárába.

Opcionális: Nevezd át `ROOT.war`-nak a `psc-online.war` fájlt.

Indítsd el a Tomcat Servert. (Linux: `tomcat/bin/catalina.sh start`)

#### Buildelés forrásból

Cloneozd a repót vagy töltsd le tömörítve és csomagold ki.

Mavennel `clean install` góllal buildeld.

## TODO

 - Tesztek (!)
 - Responsive display fix
 - Kód mentése a szerveren
 - Konfiguráció
 - Korlátozások
 - Magyar felület
 
## Felhasznált 3rd party cuccok

ComeMirror - Syntax highlighter [SITE](https://codemirror.net/), [GIT](https://github.com/codemirror/codemirror), [License](https://github.com/codemirror/CodeMirror/blob/master/LICENSE)

Font Awesome - Icon font [SITE](http://fontawesome.io/), [GIT](https://github.com/FortAwesome/Font-Awesome), [License](http://fontawesome.io/license/)