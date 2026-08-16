# FramedBlocks (Fabric)

**Port para Fabric de [FramedBlocks](https://github.com/XFactHD/FramedBlocks) para Minecraft 1.20.1.**

FramedBlocks añade una enorme colección de bloques decorativos con marco a Minecraft. Cada bloque
con marco acepta un camuflaje de cualquier otro bloque, lo que permite construir estructuras sin
juntas, pendientes, escaleras, paneles, puertas y mucho más sin romper el aspecto visual de tu
construcción.

> **Nota:** este es un port comunitario del mod original de XFactHD, publicado bajo la licencia
> [LGPL-3.0](./LICENSE). Todo el crédito del diseño y el código original pertenece a XFactHD —
> consulta [NOTICE](./NOTICE).

---

## Características

- **Sistema de camuflaje:** enmarca cualquier bloque con cualquier otro y reprodúcelo sin costuras.
- **Más de 120 tipos de bloques:** pendientes, escaleras, paneles, postes, puertas, trampillas,
  bloques plegables y de copia, y más.
- **Bloques dobles:** coloca un camuflaje diferente en cada mitad del bloque.
- Soporte para **render fantasma, blueprint y toolbox** para planificar construcciones grandes.
- Compatible con JEI, REI, EMI, Jade, Create, Athena y Supplementaries.

## Requisitos

| Dependencia  | Versión            |
|--------------|--------------------|
| Minecraft    | 1.20.1             |
| Fabric Loader| 0.15.10 o superior |
| Fabric API   | 0.92.8 o superior  |
| Java         | 17 o superior      |

## Descarga

Los jars precompilados para cada versión de Minecraft están disponibles en la página de
[Releases](https://github.com/Lowei-7/FramedBlocks-Fabric/releases).

## Instalación

1. Instala el [Fabric Loader](https://fabricmc.net/use/) para Minecraft 1.20.1.
2. Instala [Fabric API](https://modrinth.com/mod/fabric-api) en la carpeta `mods`.
3. Coloca el jar de FramedBlocks en la carpeta `mods`.
4. Inicia el juego.

## Compilación desde el código fuente

Se requiere un JDK 17 o superior.

```bash
./gradlew build
```

El jar generado se escribe en `build/libs/`.

## Versiones

Cada versión compatible de Minecraft vive en su propia rama. Las ramas actuales son:

| Rama    | Minecraft | Estado           |
|---------|-----------|------------------|
| `1.20.1`| 1.20.1    | Publicada        |

Las ramas se crean cuando se completa y prueba el port de cada versión de Minecraft.
Cada rama se compila automáticamente por CI en cada push.

## Licencia

Este proyecto se distribuye bajo la **GNU Lesser General Public License v3.0** — consulta el
archivo [LICENSE](./LICENSE). Es un port de [FramedBlocks](https://github.com/XFactHD/FramedBlocks)
de XFactHD (también LGPL-3.0); consulta [NOTICE](./NOTICE) para la atribución.

## Créditos

- **XFactHD** — mod original FramedBlocks (diseño, código, recursos).
- **Lowei-7** — port de Forge a Fabric para Minecraft 1.20.1.
- Todos los usuarios y contribuidores que informan de errores, sugieren funciones y ayudan a
  mejorar el proyecto.

## Documentación

- [English](./README.md)
- [Español](./README.es.md)
- [Français](./README.fr.md)
