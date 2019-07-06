(ns scf.state-test
  (:require [cljs.test :refer [deftest is testing]]
            [scf.state :as subject]
            [reagent.core :as r]))


(deftest test-config->ui-state

  (testing "With a few simple fields"
    (is (= {:my-button {:href "" :text ""}
            :my-input  {:value ""}}
           (subject/config->ui-state {:fields {:my-button {:type :link}
                                               :my-input  {:type :text}}}))))

  (testing "With nested fields"
    (is (= {:my-button   {:href "" :text ""}
            :my-repeater [{:nested-btn  {:href "" :text ""}
                           :nested-text {:value ""}}]}
           (subject/config->ui-state
             {:fields
              {:my-button {:type :link}
               :my-repeater {:type :repeater
                             :fields {:nested-btn {:type :link}
                                      :nested-text {:type :text}}}}})))))
