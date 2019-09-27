(ns scf.field-test
  (:require [cljs.test :refer [deftest is testing]]
            [scf.field :as subject]))


(deftest label

  (testing "With a label specified"
    (is (= "Hello, World!" (subject/label {:name :goodbye
                                           :label "Hello, World!"}))))
  (testing "Without a label specified"
    (is (= "Asdf Qwerty Foo" (subject/label {:name :asdf-qwerty_foo})))))

(deftest wrap-in

  (testing "With a keyword path"
    (is (= [:div#wrapper [:a "Hello, World!"]]
           (subject/wrap-in [:a "Hello, World!"] :div#wrapper))))


  (testing "With a vector path"
    (is (= [:div#one [:p.two [:span#three [:a "Hello, World!"]]]]
           (subject/wrap-in [:a "Hello, World!"] [:div#one :p.two :span#three]))))

  (testing "With a function path"
    (is (= [:span.dynamic [:a "Hello, World!"]]
           (subject/wrap-in [:a "Hello, World!"] (fn [elem]
                                                   [:span.dynamic elem]))))))
