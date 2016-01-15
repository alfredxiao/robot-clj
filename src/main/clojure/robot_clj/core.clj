(ns robot-clj.core
  (require [robot-clj.command :refer [run-commands]]
           [robot-clj.parser :refer [command-seq]])
  (:gen-class))

(defn -main [filename & _]
  (when filename
        (run-commands (command-seq filename))))
