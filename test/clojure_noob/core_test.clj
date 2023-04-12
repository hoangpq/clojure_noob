(ns clojure-noob.core-test
  (:require [clojure.test :refer :all]
            [clojure-noob.core :refer :all]))

(deftest test-fn
  (is (= 1 1)))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))
