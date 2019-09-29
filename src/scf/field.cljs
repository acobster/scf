(ns scf.field
  (:require [clojure.string :as string]
            [scf.state :as state]))


(defn label [{field-label :label field-name :name}]
  (or
    field-label
    (as-> field-name v            ; :field-name
        (name v)                  ; "field-name"
        (string/split v #"[-_]")  ; ["field" "name"]
        (map string/capitalize v) ; ["Field" "Name"]
        (string/join " " v))))    ; "Field Name"

(defn wrap-in [html path]
  "Takes an element and a path, and returns the element wrapped in nested
  elements, each of which is one \"step\" in path"
  (cond
    (keyword? path) [path html]
    (fn? path) (path html)
    :else (reduce (fn [wrapped wrapper] [wrapper wrapped]) html (reverse path))))


(defmulti layout (fn [config _]
                   (:layout config)))


(defmethod layout :label-above [config & inputs]
  (apply conj
         ; TODO wrapper-class abstraction
         [:div {:class (or (:wrapper-class config) "scf-field")}
          ; TODO label-class abstraction
          [:h2 {:class (or (:label-class config) "scf-label")} (label config)]]
         inputs))

(defmethod layout :label-before [config & inputs]
  (apply conj
         [:div {:class (or (:wrapper-class config) "scf-field")}
          [:span {:class (or (:label-class config) "scf-label")} (label config)]]
         inputs))

(defmethod layout :label-after [config & inputs]
  (apply conj
         [:div {:class (or (:wrapper-class config) "scf-field")}]
         (conj (vec inputs) ; we get inputs as a list, which behaves
                            ; differently with conj
               [:span {:class (or (:label-class config) "scf-label")}
                (label config)])))

(defmethod layout :default [config & inputs]
  (apply layout (conj config {:layout :label-above}) inputs))



(defmulti attrs (fn [config _ _]
                  (:attr-preset config)))

(defmethod attrs :standard [config ui-state custom-attrs]
  (let [user-attrs (or (:attrs config) {})
        path (conj (:path config) :value)]
    (conj user-attrs
          {:value (get-in @ui-state path)
           :on-change (state/emitter ui-state path)}
          custom-attrs)))

(defmethod attrs :checkbox [config ui-state custom-attrs]
  (let [user-attrs (or (:attrs config) {})
        path (conj (:path config) :checked)]
    (conj user-attrs
          {:checked (get-in @ui-state path)
           :value (:value config)
           :on-change (state/toggle-emitter ui-state path)}
          custom-attrs)))
