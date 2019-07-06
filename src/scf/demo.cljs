(ns scf.demo
  (:require [reagent.core :as r]
            [scf.core :as scf]))

(def demo-config {:fields {:my-repeater {:label "My Repeater"
                                    :type :repeater
                                    :add-row-text "Add a Button"
                                    :fields {:button {:type :link}
                                             :button2 {:type :link}}}}})

(defonce demo-state (r/atom {}))
(defn demo-ui [config state]
  [:div#demo-ui
   (scf/ui config state)])

(defn mount-root []
  (r/render [demo-ui demo-config demo-state]
            (.getElementById js/document "demo")))

(defn init! []
  (mount-root))
