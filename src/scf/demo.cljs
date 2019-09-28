(ns scf.demo
  (:require [reagent.core :as r]
            [scf.core :as scf]
            [scf.view :refer [ui-component]]))

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
                           {:name :my-text
                            :label "Label Above (Default)"
                            :type :text}
                           {:name :my-text
                            :label "Label Before"
                            :type :text
                            :layout :label-before}
                           {:name :my-text
                            :label "Label After"
                            :type :text
                            :layout :label-after}
                           {:name :my-textarea
                            :type :textarea
                            :attrs {:cols 50
                                    :rows 12}}
                           {:name :my-range
                            :type :range}
                           {:name :my-checkbox
                            :type :checkbox}
                           {:name :my-radio
                            :type :radio
                            :options [[1 "One"]
                                      [2 "Two"]
                                      [3 "Three"]]}
                           {:name :my-select
                            :type :select
                            :default "2"
                            ; TODO how to load options async?
                            :options [[1 "One"]
                                      [2 "Two"]
                                      [3 "Three"]]}
                           {:name :my-multi-select
                            :label "My Multi-Select"
                            :type :multiselect
                            :default #{"2" "4"}
                            ; TODO how to load options async?
                            :options [["1" "One"]
                                      ["2" "Two"]
                                      ["3" "Three"]
                                      ["4" "Four"]
                                      ["5" "Five"]]}
                           {:name :my-file
                            :type :file}
                           {:name :my-image
                            :type :image}
                           {:name :my-debugger
                            :type :scf-debugger}]})

(defonce demo-state (r/atom {}))

(defmethod ui-component :image [config ui-state]
  [:div "TODO IMAGE"])


(defn mount-root []
  (r/render [:div
             [:div#real-ui (scf/ui demo-config demo-state)]]
            (.getElementById js/document "demo")))

(defn init! []
  (mount-root))
