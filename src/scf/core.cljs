(ns scf.core
  (:require [reagent.core :as r]
            [scf.view :as view]
            [scf.state :as state]))

(defn ui [config ui-state]
  (reset! ui-state (state/config->ui-state config))
  [:div.scf-fields-ui
   (map (fn [[k v]]
          ^{:key (gensym)}
          [view/ui-component
           (conj v {:field-name k
                    :path [k]
                    ; TODO field-specific emitters
                    :emitters {}})
           ui-state])
        (:fields config))
   [:pre (js/JSON.stringify (clj->js config) true)]])

