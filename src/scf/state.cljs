(ns scf.state)


(defn config->ui-state [config]
  (into {}
        (map (fn [[k field]]
               [k (get {:text     {:value ""}
                        :textarea {:value ""}
                        :range    {:value 0}
                        :checkbox {:checked false}
                        :radio    {:value ""}
                        :select   {:value ""} ; TODO support multiple
                        :file     {} ; TODO
                        :link     {:href ""
                                   :text ""}
                        :repeater [(config->ui-state field)]}
                       (:type field))])
             (:fields config))))
