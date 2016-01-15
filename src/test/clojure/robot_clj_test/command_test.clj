(ns robot-clj-test.command-test
  (:require [clojure.test :refer :all]
            [robot-clj.command :as subject]
            [robot-clj.constants :as consts]
            [robot-clj-test.utilities :refer :all]))

(deftest turn-left
  (are [pre-state post-state]
       (= post-state (subject/left pre-state))

       {:x 0 :y 1 :direction :EAST}  {:x 0 :y 1 :direction :NORTH}
       {:x 1 :y 2 :direction :SOUTH} {:x 1 :y 2 :direction :EAST}
       {:x 2 :y 3 :direction :WEST}  {:x 2 :y 3 :direction :SOUTH}
       {:x 3 :y 4 :direction :NORTH} {:x 3 :y 4 :direction :WEST}))


(deftest turn-right
  (are [pre-state post-state]
       (= post-state (subject/right pre-state))

       {:x 0 :y 1 :direction :EAST}  {:x 0 :y 1 :direction :SOUTH}
       {:x 1 :y 2 :direction :SOUTH} {:x 1 :y 2 :direction :WEST}
       {:x 2 :y 3 :direction :WEST}  {:x 2 :y 3 :direction :NORTH}
       {:x 3 :y 4 :direction :NORTH} {:x 3 :y 4 :direction :EAST}))


(deftest valid-move-forward
  (are [pre-state post-state]
       (= post-state (subject/move pre-state))

       {:x 0 :y 1 :direction :EAST}  {:x 1 :y 1 :direction :EAST}
       {:x 1 :y 2 :direction :SOUTH} {:x 1 :y 1 :direction :SOUTH}
       {:x 2 :y 3 :direction :WEST}  {:x 1 :y 3 :direction :WEST}
       {:x 3 :y 0 :direction :NORTH} {:x 3 :y 1 :direction :NORTH}))


(deftest invalid-move-is-ignored
  (are [pre-state post-state]
       (= post-state (subject/move pre-state))

       {:x 0 :y 0 :direction :SOUTH}  {:x 0 :y 0 :direction :SOUTH}
       {:x 0 :y 0 :direction :WEST}   {:x 0 :y 0 :direction :WEST}
       {:x 4 :y 0 :direction :EAST}   {:x 4 :y 0 :direction :EAST}
       {:x 4 :y 0 :direction :SOUTH}  {:x 4 :y 0 :direction :SOUTH}
       {:x 4 :y 4 :direction :EAST}   {:x 4 :y 4 :direction :EAST}
       {:x 4 :y 4 :direction :NORTH}  {:x 4 :y 4 :direction :NORTH}
       {:x 0 :y 4 :direction :WEST}   {:x 0 :y 4 :direction :WEST}
       {:x 0 :y 4 :direction :NORTH}  {:x 0 :y 4 :direction :NORTH}))


(deftest state-is-reported
  (let [output (with-out-str
                 (let [pre-state {:x 2 :y 3 :direction :WEST}
                       post-state (subject/report pre-state)]
                   (is (= post-state pre-state))))]
    (is (= (expected-output "2,3,WEST") output))))


(deftest valid-placement
  (is (= {:x 1 :y 2 :direction :EAST}
         (subject/place {:x 0 :y 0 :direction :SOUTH} 1 2 :EAST))))


(deftest invalid-placement-ignored
  (let [pre-state consts/initial-state]
    (is (= pre-state
           (subject/place pre-state 0 -1 :WEST)))
    (is (= consts/initial-state
           (subject/place pre-state 0 5 :EAST)))))

