[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=alert_status)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=bugs)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=coverage)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=sqale_index)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)

---


# NG-HASTE

NG-HASTE, standing for "Node Graph - Heuristic Axiom Search Tree Extraction", is an algorithm designed for the purpose of runtime code generation. The principle for this concept is to solve logical problems through various analytical means to generate solutions for "black-box" environments. At it's core, NG-HASTE is a search tree which attempts to solve problems by digging through a search tree of all possible solutions and then using various approachs of heuristic guesses and logical conclusions to prune the search tree down to a more reasonable size.

The algorithm also keeps a local database which it uses to store it's current progress with all problems it is working through, as well as keeping a record of how certain data types and functions can be used in order to solve future problems faster. 

When working with NG-HASTE, the purpose of this algorithm is designed specifically for generating algorithms to solve problems, rather than solving problems directly. An example of this would be working with a Rubiks Cube. Instead of solving the cube directly, the algorithm is designed for writing a new algorithm which is capable of solving the Rubiks Cube.

As the search space is essentially "brute forced" rather than aproximated, a solution is garenteed to be found eventually, provided a solution exists. This project aims to optimize the search techniques and reduce the number of steps required to find a solution. Some of these optimizations include:
* Tracking the range of values of data types as they pass through a solution in order to prune children which produce poor results.
* Keeping record of inputs can be provided to functions to produce what outputs, allowing shortcuts to be taken.
* Allowing using probability when working with data type ranges for better heuristics.

## Getting Started

### Maven
The project is primarily distributed through Maven. You can find the latest releases through the packages tab of this project. Simply add:
```
 <dependency>
  <groupId>net.whg</groupId>
  <artifactId>ng-haste</artifactId>
  <version>VERSION</version>
</dependency>
```
to your pom.xml file in the dependencies, replacing VERSION with the version of this project you wish to use. You can find the available versions within the packages tab.

### Jar
If you want to install the jar file directly, you can find the pre-compiled releases within the release tab of this project. Simply download the jar file and add them to your build path. The releases are also tagged with commit links if you wish to manually compile a release.

## Contributing
I would highly appreciate any and all contributions to this project. This project is being developed as a weekend hobby project and I don't have much free time to devote to developing it. All contributions are massive helps for allowing this project to reach its full potention.
