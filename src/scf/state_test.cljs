(ns scf.state-test
  (:require [cljs.test :refer [deftest is testing]]
            [scf.state :as subject]
            [reagent.core :as r]))


(deftest test-config->ui-state

  (testing "With a few simple fields"
    (is (= {:my-button {:href "" :text ""}
            :my-input  {:value ""}}
           (subject/config->ui-state {:fields [{:name :my-button :type :link}
                                               {:name :my-input  :type :text}]}))))

  (testing "With nested fields"
    (is (= {:my-button   {:href "" :text ""}
            :my-repeater [{:nested-btn  {:href "" :text ""}
                           :nested-text {:value ""}}]}
           (subject/config->ui-state
             {:fields
              [{:name :my-button :type :link}
               {:name :my-repeater
                :type :repeater
                :fields [{:name :nested-btn :type :link}
                         {:name :nested-text :type :text}]}]})))))
