# TP_RESEAU
<h2>Les différents répo</h2>
<ul>
     <li>
       Classes : endroit ou sont compilés les fichier .java
     </li>
     <li>
       dataBase : historique des chats du TCPServer si vous lancez le TCP server via le repo exe ou src
     </li>
    </ul>
<h2>TCP Client/Server</h2>
<p>Nous disposons d'un server TCPMultithreaded et de 2 clients TCP,
 un en ligne de commande "TCPClient" et un avec un interface graphique disponible dans le repertoire lib/TCPClientChatGUIApplication.
 L'historique persistant du chat à aussi été implémenté sur le TCPServer</p>
 <h3>Comment lancer le server ? </h3>
 <ul>
  <li>
    Allez dans le repo lib/TCPMultithreaded_jar/jar
  </li>
  <li>
    Ouvrez un shell et tapez la commande : java -jar TCPServerMultiThreaded [param0 numéro de port]
  </li>
 </ul>
 <h3>Comment lancer le client ligne de commande ? </h3>
   <ul>
     <li>
       Allez dans le repo lib/TCPClient_jar/jar
     </li>
     <li>
       Ouvrez un shell et tapez la commande : java -jar TCPClient [param0 address ip du server] [param1 numéro de port du server] [param2 nom du client]
     </li>
    </ul>
 <h3>Comment lancer le client GUI ? </h3>
 <ul>
      <li>
        Allez dans le repo lib/TCPClientChatGUIApplication_jar
      </li>
      <li>
        lancez le script .bat ou recopiez la commande sous linux 
      </li>
     </ul>
<h2>UDP Client/Server</h2>
<h3>Comment lancer le server ? </h3>
<ul>
      <li>
        Allez dans le repo lib/UDPServer_jar
      </li>
      <li>
        Ouvrez un shell et tapez la commande : java -jar UDPServer [param0 numéro de port du server]
      </li>
     </ul>
<h3>Comment lancer le Client ? </h3>
<ul>
      <li>
        Allez dans le repo lib/UDPClient_jar
      </li>
      <li>
        Ouvrez un shell et tapez la commande : java -jar UDPClient [param0 address ip du server] [param1 numéro de port du server] [param2 nom du client]
      </li>
     </ul>
<h2>UDP Multicast</h2>
<h3>Comment lancer le Multicast ? </h3>
<ul>
      <li>
        Allez dans le repo lib/Multicast_jar
      </li>
      <li>
        Ouvrez un shell et tapez la commande : java -jar Multicast [param0 nom du client]
      </li>
     </ul>

