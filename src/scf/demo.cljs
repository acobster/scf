(ns scf.demo
  (:require [reagent.core :as r]
            [scf.core :as scf]))

(def demo-config {:fields {:my-repeater {:label "My Repeater"
                                         :type :repeater
                                         :add-row-text "Add a Button"
                                         :fields {:button {:type :link}
                                                  :button2 {:type :link}}}
                           :my-link {:type :link}
                           :my-debugger {:type :scf-debugger}}})

(defonce demo-state (r/atom {}))


(defn mount-root []
  (r/render [:div
             [:div#real-ui (scf/ui demo-config demo-state)]]
            (.getElementById js/document "demo")))

(defn init! []
  (mount-root))
