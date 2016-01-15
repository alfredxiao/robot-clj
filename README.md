# robot-clj

Solve toyrobot puzzle in Clojure

## Notes
- Used an approach to parse command different from that in Java solution. It does not rely much on regular expression.
- Each command processing is functional, accepting pre-state, returning post-state
- Multiple command processing is done via 'reduce'
- drop-while deals with ignoring commands before first PLACE
- Tested using Leiningen 2.5.1 and Maven 3.1.1

## Usage (Leiningen)
- To run tests: ./lein.sh test
- To run with an command file: ./lein.sh run -m robot-clj.core test-resources/commands_01.txt
- ./lein.sh will install Leiningen itself if you don't have it on your system. (Internet connection is required for installation)

## Usage (Maven)
- To compile: mvn clojure:compile
- To run tests: mvn clojure:test
- To run with an command file: mvn clojure:run -Dclojure.mainClass="robot_clj.core" -Dclojure.args="/home/alfred/xiaoyf/projects/robot-clj/test-resources/commands_01.txt" -e -X

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
