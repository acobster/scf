(ns scf.demo
  (:require [reagent.core :as r]
            [scf.core :as scf]))

(def demo-config {:fields [{:name :my-repeater
                            :label "My Repeater"
                            :type :repeater
                            :add-row-text "Add a Button"
                            :fields [{:name :button
                                      :type :link}
                                     {:name :button2
                                      :type :link}]}
                           {:name :my-link
                            :type :link}
                           {:name :my-debugger
                            :type :scf-debugger}]})

                           ; TODO
                           ;:my-textarea {:type :textarea}
                           ;:my-range {:type :range}
                           ;:my-checkbox {:type :checkbox}
                           ;:my-radio {:type :radio}
                           ;:my-select {:type :select}
                           ;:my-file {:type :file}

(defonce demo-state (r/atom {}))


(defn mount-root []
  (r/render [:div
             [:div#real-ui (scf/ui demo-config demo-state)]]
            (.getElementById js/document "demo")))

(defn init! []
  (mount-root))
