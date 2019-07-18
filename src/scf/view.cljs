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
  [:div.scf-link
   [:input {:type "text"
            :value (get-in config [:field-state :href])
            :on-change #(swap! ui-state
                               assoc-in
                               (conj (:path config) :href)
                               (.-target.value %))}]
   [:input {:type "text"
            :value (get-in config [:field-state :text])
            :on-change #(swap! ui-state
                               assoc-in
                               (conj (:path config) :text)
                               (.-target.value %))}]])

(defmethod ui-component :repeater [config ui-state]
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
