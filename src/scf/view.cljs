(ns scf.view
  (:require [scf.state :as state]))


(defmulti ui-component
  (fn [config _]
    (:type config)))



(defmethod ui-component :text [config ui-state]
  [:div "TODO text"])

(defmethod ui-component :textarea [config ui-state]
  [:div "TODO textarea"])

(defmethod ui-component :range [config ui-state]
  [:div "TODO range"])

(defmethod ui-component :checkbox [config ui-state]
  [:div "TODO checkbox"])

(defmethod ui-component :radio [config ui-state]
  [:div "TODO radio"])

(defmethod ui-component :select [config ui-state]
  [:div "TODO select"])

(defmethod ui-component :file [config ui-state]
  [:div "TODO file"])


(defmethod ui-component :link [config ui-state]
  (let [href-path (conj (:path config) :href)
        text-path (conj (:path config) :text)]
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
              (map (fn [{sub-name :name :as sub-field}]
                     (let [sub-path (conj path idx sub-name)
                           sub-config (conj sub-field {:path sub-path})]
                       [ui-component sub-config ui-state]))
                   fields))])
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
