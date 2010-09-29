=== OPEN CMIS FileShare Repository  ==========================================


==== Installation ====

Dezipper l'archive opencmis_server.zip

Deployer l'application dans un tomcat 5.5 ou plus

Version de JRE qui doit être utilisée : 1.6 


==== Configuration du serveur ====

Le fichier repository.properties est le fichier de configuration du serveur.
Il se trouve dans :
opencmis_server/WEB-INF/classes/repository.properties


1/ Declarer la liste des comptes qui pourront se connecter au serveur

login.<numero> = <login>:<mot_de_passe>

-- exemple :

login.1 = test:test  



2/ Definir l'identifiant du repository

repository.<identifiant_du_repository> = ..

-- exemple :

repository.esupNews = ..



3/ Adresse physique du dossier racine du serveur

repository.<identifiant_du_repository> = <chemin_vers_la_racine>

-- exemple : 

repository.esupNews = E:\\Projects\\EsupNews\\test_base_pj



4/ Donner les droits d'acces aux comptes definis


En lecture/ecriture : 

repository.<identifiant_du_repository>.readwrite = <login>


En lecture seule :

repository.<identifiant_du_repository>.readonly = <login> 


-- exemple : 

repository.esupNews.readwrite = test
repository.esupNews.readonly = cmisuser, reader



==== Configuration des logs ====

Modifier le fichier log qui se trouve dans :
opencmis_server/WEB-INF/classes/log4j.properties



==============================================================





