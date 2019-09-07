<h1>The Cloud App Server System (ClApSeSy)</h1>

*Run applications with graphical user interface in the cloud/remote*


*Overwiev*

This system consists of several interconnected components.
One of the components, the _GuiServer_, runs on the local machine and handles the representation of graphical user interfaces with their interactions by mouse and keyboard..
The GuiServer is connected to another component, the _GuiAdapter_.
This GuiAdapter also runs locally.
With this GuiAdapter you can specify a kind of address similar to an internet address.
In addition to the GuiServer, the GuiAdapter is connected to another component, the _GuiAppServer_.
The GuiAppServer is a component that runs remotely or in the cloud and manages instances of applications running remotely or in the cloud.
This allows remote applications to control local graphical user interfaces.

GuiServer, GuiAppServer and GuiAdapter are described in more detail in separate documents.

In addition, three demo applications are available: Two client applications for local operation and one application for remote operation.

Reasons for the development of the CASS:

<ul>
<li>Application view:</li>
<ul>
<li>No longer restricted to the browser</li>
<li>Possibility to design with HTML and CSS</li>
<li>No local installation of applications necessary, as long as no local resources from memory and screen are used</li>
<li>Platform independent</li>
</ul>
<li>Software development view:</li>
<ul>
<li>Software development not necessary via "script bridges"</li>
<li>No separate web framework necessary for Software development</li>
<li>Software development with programming languages of your choice</li>
<li>Loose coupling of the Gui via a protocol, no Gui framework required for a development environment anymore</li>
<li>HTML and CSS design options for the graphical user interface</li>
<li>No operating system dependent style of the user interface</li>
<li>No "pure pressing" of applications into a document view for which the previous technology is designed (synchronous sequence of request - response)</li>
<li>Safer</li>
</ul>
</ul>


*Installation und Start of the system (folder _binary_):*

The _binary_ directory contains the execution files in the _GuiServer_, _GuiAdapter_, _GuiAppServer_, _ClientDemo1_, _ClientDemo2_, _ServerDemo1_ directories.
Depending on the platform, the files must be started with the extensions _.bat_, _.sh_, _.command_ (folder _win_, _lin_, _mac_).
These files contain placeholder of paths to Java 8 runtime environments.
The associated Java runtime environments for Java 8 must be installed separately.
The path specification for _cd_ in the files with the extension _.command_ serves as an example.
It was tested for Windows Build 1.8.0_73, Linux Build 1.8.0_201 and Mac Build 1.8.0_202.
The configuration expects a running web server under _localhost:8080_.
To do this, create a directory _clapsesy_ in the document root of the web server (e.g. _htdocs_ under _xampp_) and copy the directory _GuiElementPages_ into it.
In the current development state, the GuiServer must be started locally first, then the GuiAdapter. Remote, i.e. on the server side, first the remote application (here ServerDemo1) must be started, then the GuiAppServer. The local applications (here ClientDemo1 and ClientDemo2) must be started after the GuiServer in the current development status.



<h1>Das Cloud App Server System (ClApSeSy)</h1>

*Anwendungen mit graphischer Benutzeroberfläche in der Cloud/Remote laufen lassen*


*Übersicht*

Dieses System besteht aus mehreren miteinander in Verbindung stehenden Komponenten.
Eine Komponente, der _GuiServer_, läuft auf dem lokalen Rechner und behandelt die Darstellung von graphischen Benutzeroberflächem mit deren Interaktionen durch Maus und Tastatur.
Der GuiServer steht in Verbindung mit einer anderen Komponente, dem _GuiAdapter_.
Dieser GuiAdapter läuft ebenfalls lokal.
Mit diesem GuiAdapter kann man eine Art Adresse ähnlich einer Internet-Adresse angeben.
Der GuiAdapter steht zusätzlich zum GuiServer mit einer anderen Komponente, dem _GuiAppServer_, in Verbindung.
Der GuiAppServer ist eine Komponente, welche Remote bzw. in der Cloud läuft und Instanzen von remote bzw. in der Cloud laufenden Anwendungen verwaltet.

So können entfernt laufende Anwendungen lokale graphische Oberflächen steuern.

GuiServer, GuiAppServer und GuiAdapter sind in separaten Dokumenten genauer beschrieben.

Zusätzlich stehen drei Demoanwendungen zur Verfügung: Zwei Client-Anwendungen für den lokalen Betrieb und eine Anwendung für den Remote-Betrieb.

Gründe für die Entwicklung des CASS:

<ul>
<li>Anwendungssicht:</li>
<ul>
<li>Nicht mehr auf den Browser beschränkt</li>
<li>Möglichkeit der Gestaltung mit HTML und CSS</li>
<li>Keine lokalen Installationen von Anwendungen nötig, sofern nur die graphische Benutzeroberfläche verwendet wird</li>
<li>Plattformunabhängig</li>
</ul>
<li>Softwareentwicklungssicht:</li>
<ul>
<li>Softwareentwicklung nicht über "Script-Brücken" nötig</li>
<li>Kein separates Web-Framework nötig</li>
<li>Softwareentwicklung mit den Sprachen der Wahl leichter möglich</li>
<li>Lose Kopplung der Gui über ein Protokoll, kein für eine Entwicklungsumgebung abhängiges Gui-Framework mehr nötig</li>
<li>Gestaltungsmöglichkeiten durch HTML und CSS für die graphische Oberfläche</li>
<li>Kein vom Betriebssystem abhängiger Style der Oberfläche</li>
</li>Kein "Reinpressen" von Anwendungen in eine Dokumentansicht, auf die die bisherige Technik ausgelegt ist (synchroner Ablauf von Request - Response)</li>
<li>Sicherer</li>
</ul>
</ul>

*Installation und Start des Systems (Verzeichnis _binary_):*

Das Verzeichnis _CassDemo_ kann irgendwohin kopiert werden.
Je nach Plattform sind die darin enthaltenen Dateien mit der Endung _.bat_, _.sh_, _.command_ zu starten (Verzeichnisse _win_, _lin_, _mac_).
In diesen Dateien sind sind  die Pfade zu Java 8 Laufzeitumgebungen angegeben.
Die zugehörigen Java-Laufzeitumgebungen für Java 8 sind separat zu installieren.
Die Pfadangabe bei _cd_ in den Dateien mit der Endung _.command_ dient als Beispiel.
Die Konfiguration erwartet einen laufenden WebServer unter _localhost:8080_.
Hierfür ist ist in der Document Root des WebServers (z.B. _htdocs_ unter _xampp_) ein Verzeichnis _clapsesy_ anzulegen und darin das Verzeichnis _GuiElementSeiten_ zu kopieren.
Getestet wurde für Windows Build 1.8.0_73, Linux Build 1.8.0_201 und Mac Build 1.8.0_202.
Im aktuellen Entwicklungsstand muss lokal zunächst der GuiServer, dann der GuiAdapter gestartet werden. Remote, d.h. auf Server-Seite, muss zunächst die Remote-Anwendung (hier ServerDemo1) gestartet werden, dann der GuiAppServer. Die lokalen Anwendungen (hier ClientDemo1 und ClientDemo2) sind im aktuellen Entwicklungsstand nach dem GuiServer zu starten.
