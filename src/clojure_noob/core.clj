(ns clojure-noob.core
  (:gen-class)
  (:import (com.sun.jna Native Memory)))

;; (set! *warn-on-reflection* true)

(defmacro arithmetic-if [n pos zero neg]
  (list 'cond (list 'pos? n) pos
        (list 'zero? n) zero
        :else neg))

(defmacro _and
  ([] true)
  ([x] x)
  ([x & next]
   `(let [and# ~x]
      (if and# (_and ~@next) and#))))

(defmacro our-defn [name args & body]
  `(def ~name (fn ~args ~@body)))

(defmacro mark-the-times []
  (println "This is code that runs when the macro is expanded.")
  `(println "This is the generated code."))

(defn average
  [numbers]
  (/ (apply + numbers) (count numbers)))

(defn t []
  (+ 1 1))

(defn
  test1 [a b]
  (+ a b))

(test1 1 2)

(defn random-ints
  "Returns a lazy seq of random integers in the range [0,limit]"
  [limit]
  (lazy-seq
    (println "Generating random numbers...")
    (cons (rand-int limit)
          (random-ints limit))))

;; meta programming
(def a ^{:created (System/currentTimeMillis)} [1 2 3])

(defn empty-board
  "Creates a rectangular board of the specified with
   and height."
  [w h]
  (vec (repeat w (vec (repeat h nil)))))

(defn populate
  "Turns :on each of the cells specified as [y, x] coordinates."
  [board living-cells]
  (reduce (fn [board coordinates]
            (assoc-in board coordinates :on))
          board
          living-cells))

(def glider (populate (empty-board 6 6) #{[2 0] [2 1] [2 2] [1 2] [0 1]}))

(defn neighbours
  [[x y]]
  (for [dx [-1 0 1] dy [-1 0 1] :when (not= 0 dx dy)]
    [(+ dx x) (+ dy y)]))

(defn count-neighbours
  [board loc]
  (count (filter #(get-in board %) (neighbours loc))))

(defmacro futures
  [n & exprs]
  (vec (for [_ (range n)
             expr exprs] `(future ~expr))))

(defmacro wait-futures [& args]
  `(doseq [f# (futures ~@args)] @f#))

(defn character
  [name & {:as opts}]
  (ref (merge {:name name :items #{} :health 500}
              opts)))

(def smaug (character "Smaug" :health 500 :strength 400 :items (set (range 50))))
(def bilbo (character "Bilbo" :health 100 :strength 100))
(def gandalf (character "Gandalf" :health 75 :mana 750))
(def vampire (character "Vampire" :health 100 :strength 100))

(defn loot
  [from to]
  (dosync
    (when-let [item (first (:items @from))]
      (alter to update-in [:items] conj item)
      (alter from update-in [:items] disj item))))

(defn test
  []
  (wait-futures 1
                (while (loot smaug bilbo))
                (while (loot smaug gandalf))
                (while (loot smaug vampire))))

(defn check
  []
  (apply + (map (comp count :items) [@bilbo @gandalf @vampire])))

(defn fixed-loot
  [from to]
  (dosync
   (when-let [item (first (:items @from))]
      ;; don't need to care about order in update
     (commute to update-in [:items] conj item)
     (alter from update-in [:items] disj item))))

(gen-interface
  :name jna.Haskell
  :extends [com.sun.jna.Library]
  :methods [[goroutine [] void]
            [eval [com.sun.jna.Pointer com.sun.jna.Pointer] void]])

(defn str-pointer [value]
  (doto (Memory. (inc (count value)))
    (.setString 0 value)))

(defn int-pointer
  ([] (Memory. 4))
  ([value] (doto (Memory. 4) (.setInt 0 value))))

(defn -main
  [& args]
  (System/setProperty "jna.library.path" "./src/libs")
  (let [lib (Native/load "Eval" jna.Haskell)
        s (str-pointer "1 + 2 * 1000")
        r (int-pointer)]
    (.eval lib s r)
    (println (.getInt r 0))
    (.goroutine lib)))

(defn test-jna []
  (System/setProperty "jna.library.path" "./src/libs")
  (let [lib (Native/load "Eval" jna.Haskell)
        s (str-pointer "1 + 2 * 1000")
        r (int-pointer)]
    (.eval lib s r)
    (.getInt r 0)))

(test-jna)
