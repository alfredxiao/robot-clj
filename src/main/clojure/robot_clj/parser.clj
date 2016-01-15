(ns robot-clj.parser
  (:require [clojure.string :refer [split trim join]]
            [robot-clj.constants :refer :all]
            [clojure.java.io :as io]))

(defn- is-valid-direction [dir-str]
  ((set all-directions) (keyword dir-str)))

(defn- split-to-parts
  "Split a string line into command parts.
  E.g. 'PLACE 1,2,EAST' -> ['PLACE' '1' '2' 'EAST']
  Two steps: 1. split into 'PLACE' and the rest;
             2. split '1,2,EAST' into argument parts, '1', '2', 'EAST'"
  [line]
  (let [[cmd rest] (split (trim line) #" " 2)]
    (cons cmd (when rest
                    (map trim
                         (remove empty? (split rest #",")))))))

(defn- is-integer [str]
  (when str
    (re-matches #"\d+" str)))

(defn- as-integer [str]
  (Integer/parseInt str))

(defn- place-command
  "generates a PLACE command. parameter destructuring is used such that
  it return nop command if number of args is not enough, (instead of runtime error)"
  [[x y direction]]
  (if (and (is-integer x)
           (is-integer y)
           (is-valid-direction direction))
    [:PLACE
     (as-integer x)
     (as-integer y)
     (keyword direction)]
    nil))

(defn parse-command-line
  "Parse a string line into a command vector starting with command name and followed
  by arguments, if any"
  [line]
  (let [[cmd & args] (split-to-parts line)]
    (case cmd
      "LEFT"   [:LEFT  ]
      "RIGHT"  [:RIGHT ]
      "MOVE"   [:MOVE  ]
      "REPORT" [:REPORT]
      "PLACE"  (place-command args)
      nil)))

(defn command-seq
  "Returns a sequence of commands via contents from a file at specified filepath"
  [filepath]
  (with-open [rdr (io/reader filepath)]
    (remove nil?
            (doall (map parse-command-line
                        (line-seq rdr))))))
