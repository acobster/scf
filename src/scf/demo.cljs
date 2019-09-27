(ns scf.demo
  (:require [reagent.core :as r]
            [scf.core :as scf]))

(def demo-config {:fields {:my-repeater {:label "My Repeater"
                                         :type :repeater
                                         :add-row-text "Add a Button"
                                         :fields {:button {:type :link}
                                                  :button2 {:type :link}}}
                           :my-debugger {:type :scf-debugger}}})

(defonce demo-state (r/atom {}))

(defonce dummy-state (r/atom {}))

(defn link-ui [config ui-state]
  [:div.scf-link
   [:input {:type "text"
            :value (:val @ui-state)
            :on-change (fn [event]
                         (swap! ui-state assoc :val (.-target.value event)))}]
   [:h4 "Config"]
   [:pre (js/JSON.stringify (clj->js config) nil 2)]
   [:h4 "State"]
   [:pre (js/JSON.stringify (clj->js @ui-state) nil 2)]])

(defn mount-root []
  (r/render [:div
             [link-ui {:path [:foo]} dummy-state]
             [:div#real-ui (scf/ui demo-config demo-state)]]
            (.getElementById js/document "demo")))

(defn init! []
  (mount-root))
