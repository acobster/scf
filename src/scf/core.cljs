(ns scf.core
  (:require [reagent.core :as r]
            [scf.view :as view]
            [scf.state :as state]))

(defn ui [config ui-state]
  ; TODO make ui-state init more dynamic, less hacky
  (reset! ui-state (state/config->ui-state config))
  (let [global-config (dissoc config :fields)]
    [:div.scf-fields-ui
     (map (fn [{field-name :name :as field}]
            ^{:key field-name}
            [view/ui-component
             (conj global-config field {:path [field-name]
                                        ; TODO field-specific emitters
                                        :emitters {}})
             ui-state])
          (:fields config))]))

