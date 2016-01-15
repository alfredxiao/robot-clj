(ns robot-clj-test.utilities)

(def line-separator (System/getProperty "line.separator"))

(defn expected-output [string]
  (str "\"" string "\"" line-separator))