# FramedBlocks (Fabric)

**Port Fabric de [FramedBlocks](https://github.com/XFactHD/FramedBlocks) pour Minecraft 1.20.1.**

FramedBlocks ajoute à Minecraft une vaste collection de blocs décoratifs encadrables. Chaque bloc
encadrable accepte un camouflage de n'importe quel autre bloc, ce qui permet de construire des
structures sans jointure, des pentes, des escaliers, des panneaux, des portes et bien plus sans
casser le rendu visuel de votre construction.

> **Remarque :** il s'agit d'un port communautaire du mod original de XFactHD, publié sous licence
> [LGPL-3.0](./LICENSE). Le design et le code original appartiennent à XFactHD — voir
> [NOTICE](./NOTICE).

---

## Fonctionnalités

- **Système de camouflage :** encadrez n'importe quel bloc avec n'importe quel autre et rendez-le
  sans jointure.
- **Plus de 120 types de blocs :** pentes, escaliers, panneaux, poteaux, portes, trappes, blocs
  pliables et copycat, et bien plus.
- **Blocs doubles :** placez un camouflage différent sur chaque moitié du bloc.
- Prise en charge du **ghost rendering, du blueprint et de la toolbox** pour planifier de grandes
  constructions.
- Compatible avec JEI, REI, EMI, Jade, Create, Athena et Supplementaries.

## Prérequis

| Dépendance    | Version            |
|---------------|--------------------|
| Minecraft     | 1.20.1             |
| Fabric Loader | 0.15.10 ou plus    |
| Fabric API    | 0.92.8 ou plus     |
| Java          | 17 ou plus         |

## Installation

1. Installez le [Fabric Loader](https://fabricmc.net/use/) pour Minecraft 1.20.1.
2. Installez [Fabric API](https://modrinth.com/mod/fabric-api) dans le dossier `mods`.
3. Déposez le jar de FramedBlocks dans le dossier `mods`.
4. Lancez le jeu.

## Compilation depuis les sources

Un JDK 17 ou plus est requis.

```bash
./gradlew build
```

Le jar compilé est écrit dans `build/libs/`.

## Licence

Ce projet est distribué sous la **GNU Lesser General Public License v3.0** — voir le fichier
[LICENSE](./LICENSE). Il s'agit d'un port de [FramedBlocks](https://github.com/XFactHD/FramedBlocks)
de XFactHD (également en LGPL-3.0) ; voir [NOTICE](./NOTICE) pour l'attribution.

## Crédits

- **XFactHD** — mod FramedBlocks original (design, code, ressources).
- **Lowei-7** — port Forge → Fabric pour Minecraft 1.20.1.
- Tous les utilisateurs et contributeurs qui signalent des bugs, suggèrent des fonctionnalités et
  aident à améliorer le projet.

## Documentation

- [English](./README.md)
- [Español](./README.es.md)
- [Français](./README.fr.md)
