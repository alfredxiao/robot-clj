(ns robot-clj-test.parser-test
  (:require [clojure.test :refer :all]
            [robot-clj.parser :as subject]))

(deftest parse-valid-simple-command
  (are [line command]
       (= command (subject/parse-command-line line))

       "MOVE"   [:MOVE]
       "LEFT"   [:LEFT]
       "REPORT" [:REPORT]
       "RIGHT"  [:RIGHT]
       " MOVE " [:MOVE]))


(deftest parse-invalid-simple-command
  (are [line command]
       (= command (subject/parse-command-line line))

       "Hello"  nil
       "Right"  nil
       ""       nil))


(deftest parse-valid-place-command-with-all-directions
  (are [line command]
       (= command (subject/parse-command-line line))

       "PLACE 2,3,EAST"  [:PLACE 2 3  :EAST]
       "PLACE 9,22,WEST" [:PLACE 9 22 :WEST]
       "PLACE 3,1,SOUTH" [:PLACE 3 1  :SOUTH]
       "PLACE 0,3,NORTH" [:PLACE 0 3  :NORTH]))


(deftest parse-valid-place-command-with-extra-spaces
  (are [line command]
       (= command (subject/parse-command-line line))

       "PLACE 2 , 3 , EAST"    [:PLACE 2 3  :EAST]
       " PLACE 9,22,WEST "     [:PLACE 9 22 :WEST]
       " PLACE  3 ,1  ,SOUTH " [:PLACE 3 1  :SOUTH]))


(deftest parse-invalid-place-command-with-non-integer-positions-or-invalid-direction
  (are [line command]
       (= command (subject/parse-command-line line))

       "PLACE 2a,2,EAST" nil
       "PLACE a,22,west" nil
       "PLACE 3,1,where" nil))


(deftest parse-command-file
  (is (= '([:PLACE 0 0 :NORTH]
           [:MOVE]
           [:REPORT])
         (subject/command-seq "test-resources/commands_01.txt"))))

(deftest parse-command-file-ignoring-invalid-ones
  (is (= '([:PLACE 0 0 :NORTH]
           [:LEFT]
           [:REPORT])
         (subject/command-seq "test-resources/commands_02.txt"))))
