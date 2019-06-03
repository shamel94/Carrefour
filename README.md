# Carrefour
Solution apportée au projet Carrefour https://github.com/Carrefour-Group/phenix-challenge

# Exécution du job :

Veuillez suivre les étapes suivantes Pour exécuter ce programme :
## Etape 1 : 
Cloner le projet depuis mon github
```
git clone https://github.com/shamel94/Carrefour.git
```

Voici la structure du projet :
Carrefour
  - **bin**: Contient le package (livrable sous forme jar)
  - **data** : Contient deux (02) répertoires [**input | output**]
  - **phenix-challenge** : Les sources du projet
  - **script** : Le script de lancement du job : **_runIt.bat_**
## Etape 2 :
Accéder au répertoire script et exécuter le fichier runIt.bat en spécifiant le répertoire d’entrée et le répertoire de sortie comme suit :
```
cd Carrefour/script
```
```
runIt.bat ../data/input/ ../data/output/
```
## Remarques:  
   **1. Les paramètres ci-dessus sont des chemins relatifs, vous pouvez spécifier d’autres chemins absolus**
 
   **2. Si le répertoire ./data/output n'existe pas, il foudrait le créer avant d'excuter le job.**

Si vous souhaitez réexécuter ce programme une seconde fois en spécifiant les mêmes répertoires d’entrée (les mêmes données de transactions déjà traitées) et de sortie il va falloir supprimer les fichiers : **transactions_AAAAMMJJ.data.processed** générés lors de la première exécution.

## Annexe : Structure globale
```
├───bin
├───data
│   ├───input
│   └───output
├───phenix-challenge
│   └───src
│       └───main
│           ├───data
│           ├───resources
│           └───scala
│               ├───com
│               │   └───carrefour
│               │       └───pheonix
│               │           └───challenge
│               │               ├───config
│               │               ├───model
│               │               └───utils
│               └───META-INF
└───script
```
