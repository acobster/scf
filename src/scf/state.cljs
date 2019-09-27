(ns scf.state)


(defn config->ui-state [config]
  (into {}
        (map (fn [field]
               ; TODO defaults???
               [(:name field) (get {:text     {:value ""}
                                    :textarea {:value ""}
                                    :range    {:value 0}
                                    :checkbox {:checked false}
                                    :radio    {:value ""}
                                    :select   {:value (or (:default field) "")} ; TODO support multiple
                                    :file     {:value nil} ; TODO
                                    :link     {:href ""
                                               :text ""}
                                    :repeater [(config->ui-state field)]
                                    :debugger {:active? true}}
                                   (:type field))])
             (:fields config))))

(defn emitter [ratom path]
  "Returns a function that takes a JavaScript Event and updates ratom at path"
  #(swap! ratom assoc-in path (.-target.value %)))

(defn toggle-emitter [ratom path]
  "Returns a function that toggles the boolean value at path within ratom"
  #(swap! ratom update-in path not))
