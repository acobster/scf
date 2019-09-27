(ns scf.view
  (:require [scf.state :as state]))


(defmulti ui-component
  (fn [config _]
    (:type config)))



(defmethod ui-component :link [config ui-state]
  (let [href-path (conj (:path config) :href)
        text-path (conj (:path config) :text)
        focused (.-activeElement js/document)]
  [:div.scf-link
   [:input {:type "text"
            :value (get-in @ui-state href-path)
            :on-change #(swap! ui-state
                               assoc-in
                               href-path
                               (.-target.value %))}]
   [:input {:type "text"
            :value (get-in @ui-state text-path)
            :on-change #(swap! ui-state
                               assoc-in
                               text-path
                               (.-target.value %))}]]))


(defmethod ui-component :repeater [config ui-state]
  "WORK IN PROGRESS - Nested Repeater field"
  (let [{:keys [:path :fields]} config
        repeater-state (get-in @ui-state path)]
    [:div.scf-repeater
     [:h2.scf-label (:label config)]
     (doall
       (map-indexed
         (fn [idx row-state]
           ^{:key (gensym)}
           [:div.scf-repeater-row
            (doall
              (map (fn [[sub-key sub-state]]
                     (let [sub-path (conj path idx sub-key)
                           sub-config (conj
                                        (get-in config [:fields sub-key])
                                        {:path sub-path
                                         :field-state (get-in @ui-state sub-path)})]
                       ^{:key (gensym)}
                       [ui-component sub-config ui-state]))
                   row-state))
            ])
         repeater-state))
     [:div [:button.scf-repeater-add-btn
            {:on-click #(swap! ui-state
                               update-in
                               path
                               conj
                               (state/config->ui-state config))}
            (or (:add-row-text config) "Add a Row")]]]))

(defmethod ui-component :scf-debugger [config ui-state]
     [:pre (js/JSON.stringify (clj->js @ui-state) nil 2)])
