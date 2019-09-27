(ns scf.core
  (:require [reagent.core :as r]
            [scf.view :as view]
            [scf.state :as state]))

(defn ui [config ui-state]
  (reset! ui-state (state/config->ui-state config))
  [:div.scf-fields-ui
   (map (fn [{field-name :name :as field}]
          ^{:key field-name}
          [view/ui-component
           (conj field {:path [field-name]
                        ; TODO field-specific emitters
                        :emitters {}})
           ui-state])
        (:fields config))
   [:pre (js/JSON.stringify (clj->js config) nil 2)]])

