(ns scf.view
  (:require [scf.state :as state]))


(defonce ui-components (atom {}))

; TODO achieve this with a protocol?
(defn type->component
  ([t]
   (type->component t {}))
  ([t custom-type-mappings]
   (let [all-components (conj @ui-components custom-type-mappings)]
     (get all-components t))))

(defn declare-ui-component! [t f]
  (swap! ui-components conj {t f}))



(defn ui-component [config state]
  ; emitter callbacks should come down thru config transparently,
  ; we shouldn't have to do anything special for them to propagate...
  (let [t (:type config)
        f (type->component t)
        id (gensym "scf_field__")]
    (if (fn? f)
      [f config state]
      (do
        (js/console.error "no rendering function found for field type " t)
        ; TODO do something more elegant here?
        [:div.scf-placeholder]))))



(defn text-ui []
  [:div "TODO text"])

(defn textarea-ui []
  [:div "TODO textarea"])

(defn range-ui []
  [:div "TODO range"])

(defn checkbox-ui []
  [:div "TODO checkbox"])

(defn radio-ui []
  [:div "TODO radio"])

(defn select-ui []
  [:div "TODO select"])

(defn file-ui []
  [:div "TODO file"])

(defn link-ui []
  [:div "TODO link"])

(defn repeater-ui [config ui-state]
  (let [{:keys [:path :fields]} config
        field-state (get-in @ui-state path)]
    [:div.scf-repeater
     [:pre (js/JSON.stringify (clj->js @ui-state) nil 2)]
     [:h2.scf-label (:label config)]
     (map (fn [[k v]]
            ^{:key (gensym)}
            [:div.scf-repeater-row
             [ui-component v ui-state]])
          fields)
     [:div [:button.scf-repeater-add-btn
            {:on-click #(swap! ui-state
                               update-in
                               path
                               conj
                               (state/config->ui-state config))}
            (or (:add-row-text config) "Add a Row")]]]))

(declare-ui-component! :text     text-ui)
(declare-ui-component! :textarea textarea-ui)
(declare-ui-component! :range    range-ui)
(declare-ui-component! :checkbox checkbox-ui)
(declare-ui-component! :radio    radio-ui)
(declare-ui-component! :select   select-ui)
(declare-ui-component! :file     file-ui)
(declare-ui-component! :link     link-ui)
(declare-ui-component! :repeater repeater-ui)

