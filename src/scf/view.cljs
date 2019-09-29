(ns scf.view
  (:require [scf.state :as state]
            [scf.field :as field]))


(defmulti ui-component
  (fn [config _]
    (:type config)))



(defmethod ui-component :text [config ui-state]
  (let [config (conj config {:wrapper-class "scf-text"
                             :attr-preset :standard})]
    (field/layout config
                  [:input (field/attrs config ui-state {:type "text"})])))


(defmethod ui-component :textarea [config ui-state]
  (let [config (conj config {:wrapper-class "scf-textarea"
                             :attr-preset :standard})]
    (field/layout config
                  [:textarea (field/attrs config ui-state {:type "textarea"})])))


(defmethod ui-component :range [config ui-state]
  (let [path (conj (:path config) :value)]
    [:div.scf-range
     "TODO range"]))


(defmethod ui-component :checkbox [config ui-state]
  (let [config (conj config {:wrapper-class "scf-checkbox"
                             :attr-preset :checkbox})
        ; TODO abstraction for specifying/generating ids
        id (gensym "scf-checkbox-")
        label (or (:checkbox-label config) "Yes")]

    (field/layout config
                  [:input (field/attrs config ui-state {:type "checkbox"
                                                        :id id})]
                  [:label {:for id} label])))


(defmethod ui-component :radio [config ui-state]
  (let [config (conj config {:wrapper-class "scf-radio"
                             :attr-preset :standard})
        radio (fn [[value label]]
                (let [id (gensym "scf-radio-")]
                  ^{:key value}
                  [:div.scf-radio-option
                   [:input (field/attrs config
                                        ui-state
                                        {:name (:name config)
                                         :value value
                                         :type "radio"
                                         :id id})]
                   [:label {:for id} (field/label config)]]))
        inputs (doall (map radio (:options config)))]

    (apply field/layout config inputs)))


(defmethod ui-component :select [config ui-state]
  (let [config (conj config {:attr-preset :standard})
        path (conj (:path config) :value)
        selected (or (get-in @ui-state path) (:default config))
        options (doall
                  (map (fn [[value label]]
                         ^{:key (gensym)}
                         [:option {:value value} label])
                       (:options config)))]

    (field/layout config
                  [:select (field/attrs config ui-state {:value selected})
                   options])))


(defmethod ui-component :multiselect [config ui-state]
  "WORK IN PROGRESS multiselect"
  (let [path (conj (:path config) :value)
        attrs (or (:attrs config) {})
        options (:options config)
        selected (or (get-in @ui-state path) (:default config))]
    [:div.scf-select
     [:h2.scf-label (field/label config)]
     [:select (conj attrs {:multiple true
                           :value selected
                           :on-change (fn [event]
                                        (let [value (.-target.value event)]
                                          (js/console.log (clj->js selected) value)
                                          (swap! ui-state
                                                 update-in
                                                 path
                                                 (if (contains? selected value) disj conj)
                                                 value)))})
      (doall
        (map (fn [[value label]]
               ^{:key value}
               [:option {:value value} label])
             options))]]))


(defmethod ui-component :file [config ui-state]
  (let [path (conj (:path config) :value)
        attrs (or (:attrs config) {})]
    [:div.scf-file
     [:h2.scf-label (field/label config)]
     [:input {:type "file"
              :value (get-in @ui-state path)
              :on-change (state/emitter ui-state path)}]]))


(defmethod ui-component :link [config ui-state]
  (let [config (conj config {:attr-preset :standard})
        href-id (gensym "scf-link-href-")
        text-id (gensym "scf-link-text-")
        href-path (conj (:path config) :href)
        text-path (conj (:path config) :text)
        href-attrs (field/attrs config
                                ui-state
                                {:type "text"
                                 :value (get-in @ui-state href-path)
                                 :on-change (state/emitter ui-state href-path)
                                 :id href-id})
        text-attrs (field/attrs config
                                ui-state
                                {:type "text"
                                 :value (get-in @ui-state text-path)
                                 :on-change (state/emitter ui-state text-path)
                                 :id text-id})]
    (field/layout config
                  [:div.scf-link-href
                   [:label {:for href-id} "Link URL:"]
                   [:input href-attrs]]
                  [:div.scf-link-text
                   [:label {:for text-id} "Link Text:"]
                   [:input text-attrs]])))


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
                       ; TODO figure out focus state
                       ^{:key (gensym)}
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
  (let [path (conj (:path config) :active?)
        active? (get-in @ui-state path)]
    [:div.scf-debugger
     [:button {:on-click #(swap! ui-state update-in path not)}
      "Toggle Debugger"]
     (if active?
       [:pre (js/JSON.stringify (clj->js @ui-state) nil 2)])]))

