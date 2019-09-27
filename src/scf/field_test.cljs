(ns scf.field-test
  (:require [cljs.test :refer [deftest is testing]]
            [scf.field :as subject]))


(deftest label

  (testing "With a label specified"
    (is (= "Hello, World!" (subject/label {:name :goodbye
                                           :label "Hello, World!"}))))

  (testing "Without a label specified"
    (is (= "Asdf Qwerty Foo" (subject/label {:name :asdf-qwerty_foo})))))
