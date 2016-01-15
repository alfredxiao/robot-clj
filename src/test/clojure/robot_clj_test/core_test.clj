(ns robot-clj-test.core-test
  (:require [clojure.test :refer :all]
            [robot-clj.core :as subject]
            [robot-clj-test.utilities :refer :all]))

(deftest run-command-files
  (are [filepath output]
       (= output (with-out-str (subject/-main filepath)))
       "test-resources/commands_01.txt"  (expected-output "0,1,NORTH")
       "test-resources/commands_02.txt"  (expected-output "0,0,WEST" )
       "test-resources/commands_03.txt"  (expected-output "3,3,NORTH")
       "test-resources/commands_04.txt"  (expected-output "3,2,WEST" )
       "test-resources/commands_05.txt"  (expected-output "3,2,EAST" )))
