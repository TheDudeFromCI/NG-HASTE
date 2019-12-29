[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=alert_status)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=bugs)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=coverage)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=TheDudeFromCI_NG-HASTE&metric=sqale_index)](https://sonarcloud.io/dashboard?id=TheDudeFromCI_NG-HASTE)

---

# NG-HASTE
NG-HASTE, standing for "Node Graph - Heuristic Axiom Search Tree Extraction", is an algorithm designed for the purpose of runtime code generation. The principle for this concept is to solve logical problems through various analytical means to generate solutions for "black-box" environments. At it's core, NG-HASTE is a search tree which attempts to solve problems by digging through a search tree of all possible solutions and then using various approachs of heuristic guesses and logical conclusions to prune the search tree down to a more reasonable size.

When working with NG-HASTE, the purpose of this algorithm is designed specifically for writing code or code-like solutions to problems, rather than solving problems directly. An example of this would be asking the algorithm to solve a Rubiks Cube. Instead of solving the cube directly, the algorithm is designed for writing a new algorithm which is capable of solving the Rubiks Cube. The algorithm also uses slight machine learning technques to solve future problems faster based on the observations it makes while solving problems.

As the search space is essentially "brute forced" rather than aproximated, a solution is garenteed to be found eventually, provided the solution exists within the search space. Here, the goal is designed specifically for optimizating the number of steps required to find the solution. The list of approaches used to narrow this search space is far too long to go over in this short read-me file. Head over to the wiki for an extensive break down of all steps and approaches used, as well as best practices for providing an environment for NG-HASTE to operate within.

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
I would highly appreciate any and all contributions to this project. This project is being developed as a weekend hobby project and I don't have much free time to devote to developing it. All contributions are massive helps for allowing this project to reach its full potention. If you're not sure where to begin, the best place to start is by browsing the issues tab for user suggested enhancements or simply general bug fixes. Larger goals for NG-HASTE are discussed in the projects tab.
