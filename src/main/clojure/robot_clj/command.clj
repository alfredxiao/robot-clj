(ns robot-clj.command
  (:require [clojure.string :refer [split trim join]]
            [robot-clj.constants :refer :all]))

(defn- next-direction
  "Returns a new direction based on current direction and number of hops/nexts"
  [direction hop]
  (let [index (.indexOf all-directions direction)]
    (nth all-directions
         (mod (+ index hop)
              (count all-directions)))))

(defn- is-valid-state [{:keys [x y]}]
  (and (>= x 0)
       (>= y 0)
       (<= x x-max)
       (<= y y-max)))

(defn- verified-update
  "Verify and update the state of a robot. If new state is invalid, pre-state
  is returned instead."
  [state update-fn state-field]
  (let [new-state (update-in state [state-field] update-fn)]
    (if (is-valid-state new-state)
        new-state
        state)))

(defn left [{direction :direction :as state}]
  (assoc state :direction (next-direction direction 3)))

(defn right [{direction :direction :as state}]
  (assoc state :direction (next-direction direction 1)))

(defn move [{direction :direction :as state}]
  (case direction
    :EAST  (verified-update state inc :x)
    :WEST  (verified-update state dec :x)
    :SOUTH (verified-update state dec :y)
    :NORTH (verified-update state inc :y)))

(defn report [{:keys [x y direction] :as state}]
  (do (prn (join "," [x y (name direction)]))
      state))

(defn place [state x y direction]
  (let [new-state {:x x :y y :direction direction}]
    (if (is-valid-state new-state)
      new-state
      state)))

(def ^:private processors {:LEFT   left
                           :RIGHT  right
                           :REPORT report
                           :MOVE   move
                           :PLACE  place })

(defn- process-command [state [cmd & args]]
  (let [processor (processors cmd)]
    (apply processor state args)))

(defn- is-place-command [command]
  (= :PLACE (first command)))

(defn run-commands
  "Runs a list of commands while ignoring commands before the first PLACE"
  [commands]
  (reduce process-command
          initial-state
          (drop-while (complement is-place-command) commands)))

